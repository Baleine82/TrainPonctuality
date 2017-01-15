package me.arbogast.trainponctuality.SncfApi;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

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

import me.arbogast.trainponctuality.DBAccess.CalendarDAO;
import me.arbogast.trainponctuality.DBAccess.DAOImportBase;
import me.arbogast.trainponctuality.DBAccess.RoutesDAO;
import me.arbogast.trainponctuality.DBAccess.StopTimesDAO;
import me.arbogast.trainponctuality.DBAccess.StopsDAO;
import me.arbogast.trainponctuality.DBAccess.TripsDAO;
import me.arbogast.trainponctuality.R;

/**
 * Created by excelsior on 14/01/17.
 */

public class InitializeSncfData extends AsyncTask<URL, Integer, String> {
    private static final String TAG = "InitializeSncfData";

    private Context myContext;

    public InitializeSncfData(Context context) {
        super();
        myContext = context;
    }

    @Override
    protected String doInBackground(URL... params) {
        try {
            Date lastUpdate = new Date(myContext.getSharedPreferences(myContext.getString(R.string.sharedPrefs), Context.MODE_PRIVATE).getLong(myContext.getString(R.string.prefsLastUpdateSncf), 0));
            File zipSncfInfo = new File(myContext.getCacheDir(), myContext.getString(R.string.zipSncf));
            File outputDir = new File(myContext.getCacheDir(), myContext.getString(R.string.unzipSncf));
            String gtfsInfo = getGTFSinfo();
            String fileId = getGTFSid(gtfsInfo, lastUpdate);

            if (fileId == null)
                return null;

            downloadGTFSfile(fileId, zipSncfInfo.getPath());

            outputDir.mkdir();
            unpackZip(zipSncfInfo, outputDir.getPath());

            InsertData(new File(outputDir, myContext.getString(R.string.sncfRouteFile)), new RoutesDAO(myContext), ',', '\"');
            InsertData(new File(outputDir, myContext.getString(R.string.sncfTripsFile)), new TripsDAO(myContext), ',', '\"');
            InsertData(new File(outputDir, myContext.getString(R.string.sncfStopsFile)), new StopsDAO(myContext), ',', '\"');
            InsertData(new File(outputDir, myContext.getString(R.string.sncfStopTimesFile)), new StopTimesDAO(myContext), ',', '\"');
            InsertData(new File(outputDir, myContext.getString(R.string.sncfSCalendarFile)), new CalendarDAO(myContext), ',', '\"');


        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: IOException", e);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: ParseException", e);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: JSONException", e);
        }

        return null;
    }

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

    private String getGTFSid(String gtfsInfo, Date lastUpdate) throws JSONException, ParseException {
        JSONObject parser = new JSONObject(gtfsInfo);
        JSONArray records = parser.getJSONArray(myContext.getString(R.string.sncfGtfsRecord));

        for (int i = 0; i < records.length(); i++) {
            JSONObject first = records.getJSONObject(i);
            Date dateUpdate = new SimpleDateFormat(myContext.getString(R.string.sncfJsonDateFormat)).parse(first.getString(myContext.getString(R.string.sncfJsonTimestamp)));    // date format is 2017-01-13T07:16:03+00:00
            if (lastUpdate.compareTo(dateUpdate) <= 0)
                return null;

            JSONObject file = first.getJSONObject(myContext.getString(R.string.sncfGtfsFields)).getJSONObject(myContext.getString(R.string.sncfGtfsFile));
            String fileId = file.getString(myContext.getString(R.string.sncfGtfsId));
            String fileName = file.getString(myContext.getString(R.string.sncfGtfsFilename));
            if (fileName.equals(myContext.getString(R.string.sncfGtfsDatasetUsed)))
                return fileId;
        }

        return null;
    }

    @NonNull
    private String getGTFSinfo() throws IOException {
        HttpURLConnection httpclient;
        httpclient = (HttpURLConnection) new URL(myContext.getString(R.string.sncfGetGtfsDatasets)).openConnection();
        httpclient.setRequestMethod("GET");
        BufferedReader read = new BufferedReader(new InputStreamReader(httpclient.getInputStream()));
        String line;
        StringBuilder fullData = new StringBuilder();
        while ((line = read.readLine()) != null) {
            fullData.append(line);
        }
        return fullData.toString();
    }
}
