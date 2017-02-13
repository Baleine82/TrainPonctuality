package me.arbogast.trainponctuality.sncfapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import me.arbogast.trainponctuality.dbaccess.CalendarDAO;
import me.arbogast.trainponctuality.dbaccess.DAOImportBase;
import me.arbogast.trainponctuality.dbaccess.RoutesDAO;
import me.arbogast.trainponctuality.dbaccess.StopTimesDAO;
import me.arbogast.trainponctuality.dbaccess.StopsDAO;
import me.arbogast.trainponctuality.dbaccess.TripsDAO;
import me.arbogast.trainponctuality.R;

/**
 * Created by excelsior on 14/01/17.
 * Task to synchronize SNCF GTFS data
 */

public class InitializeSncfData extends AsyncTask<URL, Integer, String> {
    private static final String TAG = "InitializeSncfData";

    private Context myContext;
    private View statusView;

    public InitializeSncfData(Context context, View status) {
        super();

        myContext = context;
        statusView = status;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected String doInBackground(URL... params) {
        File zipSncfInfo = new File(myContext.getCacheDir(), myContext.getString(R.string.zipSncf));
        File outputDir = new File(myContext.getCacheDir(), myContext.getString(R.string.unzipSncf));

        try {
            SharedPreferences prefs = myContext.getSharedPreferences(myContext.getString(R.string.sharedPrefs), Context.MODE_PRIVATE);
            Date lastUpdate = new Date(prefs.getLong(myContext.getString(R.string.prefsLastUpdateSncf), 0));

            GtfsInfo myInfo = getGTFSinfo();
            if(myInfo == null || myInfo.getLastUpdate().compareTo(lastUpdate) <= 0)
                return null;

            downloadGTFSfile(myInfo.getFileId(), zipSncfInfo.getPath());

            //noinspection ResultOfMethodCallIgnored
            outputDir.mkdir();
            unpackZip(zipSncfInfo, outputDir.getPath());

            try (RoutesDAO dbRoutes = new RoutesDAO(myContext)) {
                InsertData(new File(outputDir, myContext.getString(R.string.sncfRouteFile)), dbRoutes, ',', '\"');
            }
            try (TripsDAO dbTrips = new TripsDAO(myContext)) {
                InsertData(new File(outputDir, myContext.getString(R.string.sncfTripsFile)), dbTrips, ',', '\"');
            }
            try (StopsDAO dbStops = new StopsDAO(myContext)) {
                InsertData(new File(outputDir, myContext.getString(R.string.sncfStopsFile)), dbStops, ',', '\"');
            }
            try (StopTimesDAO dbStopTimes = new StopTimesDAO(myContext)) {
                InsertData(new File(outputDir, myContext.getString(R.string.sncfStopTimesFile)), dbStopTimes, ',', '\"');
            }
            try (CalendarDAO dbCalendar = new CalendarDAO(myContext)) {
                InsertData(new File(outputDir, myContext.getString(R.string.sncfSCalendarFile)), dbCalendar, ',', '\"');
            }


            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(myContext.getString(R.string.prefsLastUpdateSncf), myInfo.getLastUpdate().getTime());
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: IOException", e);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: ParseException", e);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: JSONException", e);
        } finally {
            if (zipSncfInfo.exists())
                zipSncfInfo.delete();
            if (outputDir.exists()) {
                for (File file : outputDir.listFiles()) {
                    file.delete();
                }
                outputDir.delete();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        statusView.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("ThrowFromFinallyBlock")
    private void InsertData(File file, DAOImportBase bdd, char separator, char quote) throws IOException {
        if (!file.exists())
            return;

        BufferedReader bis = null;
        CSVReader reader = null;
        try {
            bis = new BufferedReader(new InputStreamReader((new FileInputStream(file))));
            reader = new CSVReader(bis, separator, quote);
            // ignoring first line which contains header
            reader.readNext();
            String[] nextLine;
            bdd.beginTransaction(true);

            bdd.truncate();
            while ((nextLine = reader.readNext()) != null) {
                bdd.insert(nextLine);
            }
        } finally {
            if (bdd != null && bdd.inTransaction())
                bdd.endTransaction(true);
            if (reader != null)
                reader.close();
            if (bis != null)
                bis.close();
        }
    }

    private void unpackZip(File zipFile, String outDir) throws IOException {
        InputStream is;
        ZipInputStream zis;

        String filename;
        is = new FileInputStream(zipFile.getPath());
        zis = new ZipInputStream(new BufferedInputStream(is));
        ZipEntry ze;
        byte[] buffer = new byte[1024];
        int count;

        while ((ze = zis.getNextEntry()) != null) {
            // zapis do souboru
            filename = ze.getName();

            // Need to create directories if not exists, or
            // it will generate an Exception...
            if (ze.isDirectory()) {
                File fmd = new File(outDir, filename);
                //noinspection ResultOfMethodCallIgnored
                fmd.mkdirs();
                continue;
            }

            FileOutputStream fout = new FileOutputStream(new File(outDir, filename));

            // cteni zipu a zapis
            while ((count = zis.read(buffer)) != -1) {
                fout.write(buffer, 0, count);
            }

            fout.close();
            zis.closeEntry();
        }

        zis.close();
    }

    @SuppressWarnings("ThrowFromFinallyBlock")
    private void downloadGTFSfile(String fileId, String filename) throws IOException {
        HttpURLConnection httpclient = null;
        InputStream reader = null;
        FileOutputStream resFile = null;
        try {
            httpclient = (HttpURLConnection) new URL(String.format(myContext.getString(R.string.sncfDownloadDataset), fileId)).openConnection();
            httpclient.setRequestMethod("GET");
            reader = httpclient.getInputStream();
            resFile = new FileOutputStream(filename);

            byte[] buffer = new byte[1024];
            int nbRead;
            while ((nbRead = reader.read(buffer)) != -1) {
                resFile.write(buffer, 0, nbRead);
            }
        } finally {
            if (resFile != null)
                resFile.close();
            if (reader != null)
                reader.close();
            if (httpclient != null)
                httpclient.disconnect();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private GtfsInfo getGTFSinfo() throws JSONException, ParseException, IOException {
        HttpURLConnection httpclient;
        httpclient = (HttpURLConnection) new URL(myContext.getString(R.string.sncfGetGtfsDatasets)).openConnection();
        httpclient.setRequestMethod("GET");
        BufferedReader read = new BufferedReader(new InputStreamReader(httpclient.getInputStream()));
        String line;
        StringBuilder fullData = new StringBuilder();
        while ((line = read.readLine()) != null) {
            fullData.append(line);
        }

        JSONObject parser = new JSONObject(fullData.toString());
        JSONArray records = parser.getJSONArray(myContext.getString(R.string.sncfGtfsRecord));

        for (int i = 0; i < records.length(); i++) {
            GtfsInfo data = new GtfsInfo();

            JSONObject first = records.getJSONObject(i);
            data.setLastUpdate(new SimpleDateFormat(myContext.getString(R.string.sncfJsonDateFormat)).parse(first.getString(myContext.getString(R.string.sncfJsonTimestamp))));    // date format is 2017-01-13T07:16:03+00:00

            JSONObject file = first.getJSONObject(myContext.getString(R.string.sncfGtfsFields)).getJSONObject(myContext.getString(R.string.sncfGtfsFile));
            data.setFileId(file.getString(myContext.getString(R.string.sncfGtfsId)));
            data.setFileName(file.getString(myContext.getString(R.string.sncfGtfsFilename)));
            if (data.getFileName().equals(myContext.getString(R.string.sncfGtfsDatasetUsed)))
                return data;
        }

        return null;
    }
}
