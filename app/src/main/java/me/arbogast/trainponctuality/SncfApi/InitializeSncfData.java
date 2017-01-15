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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import me.arbogast.trainponctuality.DBAccess.DAOImportBase;
import me.arbogast.trainponctuality.DBAccess.RoutesDAO;

/**
 * Created by excelsior on 14/01/17.
 */

public class InitializeSncfData extends AsyncTask<URL, Integer, String> {
    private static final String TAG = "InitializeSncfData";
    private static final String GET_GTFS_URL = "https://ressources.data.sncf.com/api/records/1.0/search/?dataset=sncf-transilien-gtfs";
    private static final String GET_GTFS_OBJECT_URL = "https://ressources.data.sncf.com/explore/dataset/sncf-transilien-gtfs/files/%1s/download/";

    private Context myContext;

    public InitializeSncfData(Context context) {
        super();
        myContext = context;
    }

    @Override
    protected String doInBackground(URL... params) {
        try {
            File zipSncfInfo = new File(myContext.getCacheDir(), "sncfData.zip");
            File outputDir = new File(myContext.getCacheDir(), "outSncf");
//            String gtfsInfo = getGTFSinfo();
//            String fileId = getGTFSid(gtfsInfo);
//
//            if (fileId == null)
//                return null;
//
//            downloadGTFSfile(fileId, zipSncfInfo.getPath());

//            outputDir.mkdir();
//            unpackZip(zipSncfInfo, outputDir.getPath());

            InsertData(new File(outputDir.getPath(), "routes.txt"), new RoutesDAO(myContext), ',', '\"' );


        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: IOException", e);
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TAG, "doInBackground: JSONException", e);
//        }

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
            httpclient = (HttpURLConnection) new URL(String.format(GET_GTFS_OBJECT_URL, fileId)).openConnection();
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

    private String getGTFSid(String gtfsInfo) throws JSONException {
        JSONObject parser = new JSONObject(gtfsInfo);
        JSONArray records = parser.getJSONArray("records");

        for (int i = 0; i < records.length(); i++) {
            JSONObject first = records.getJSONObject(i);
            String dateUpdate = first.getString("record_timestamp");
            // TODO Check if date is newer than in db

            JSONObject file = first.getJSONObject("fields").getJSONObject("file");
            String fileId = file.getString("id");
            String fileName = file.getString("filename");
            if (fileName.equals("gtfs-lines-last.zip"))
                return fileId;
        }

        return null;
    }

    @NonNull
    private String getGTFSinfo() throws IOException {
        HttpURLConnection httpclient;
        httpclient = (HttpURLConnection) new URL(GET_GTFS_URL).openConnection();
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
