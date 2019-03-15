package com.koshy.textninja;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class FileDownloadAyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
    private static final String TAG = FileDownloadAyncTask.class.getSimpleName();
    String path;
    Utils.OnFileDownloadComplete mOnFileDownloadComplete;

    public FileDownloadAyncTask(String path, Utils.OnFileDownloadComplete mOnFileDownloadComplete) {
        this.path = path;
        this.mOnFileDownloadComplete = mOnFileDownloadComplete;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
//        String filePath = downloadFile();
        ArrayList<String> stringArrayList = new ArrayList<>();
        Log.d(TAG, "doInBackground: " + path);
        try {
            // Create a URL for the desired page
            URL url = new URL(path);

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//            StringBuilder sb = new StringBuilder("");
            stringArrayList = Utils.getParagraphs(in);
        } catch (MalformedURLException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        }
        return stringArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s) {
        mOnFileDownloadComplete.onFileDownloadComplete(s);
    }
}
