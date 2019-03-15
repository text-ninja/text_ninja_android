package com.koshy.textninja;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ServerApiAsyncTask extends AsyncTask<String, Void, String> {
    public static final String TAG = ServerApiAsyncTask.class.getSimpleName();
    Utils.OnApiFetchComplete mOnApiFetchComplete;

    public ServerApiAsyncTask(Utils.OnApiFetchComplete onApiFetchComplete) {
        mOnApiFetchComplete = onApiFetchComplete;
    }

    @Override
    protected String doInBackground(String ...params) {
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL("https://text-ninja-230402.appspot.com/create");
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(params[0]);
            osw.flush();
            osw.close();
            os.close();  //don't forget to close the OutputStream
            urlConnection.connect();

            //read the inputstream and print it
            String result;
            BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result2 = bis.read();
            while(result2 != -1) {
                buf.write((byte) result2);
                result2 = bis.read();
            }
            result = buf.toString();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: ");
        mOnApiFetchComplete.onApiFetchComplete(s);
    }
}
