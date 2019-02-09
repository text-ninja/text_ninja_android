package com.koshy.textninja;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class FileDownloadAyncTask extends AsyncTask<Void, Void, String> {
    public static final String TAg = FileDownloadAyncTask.class.getSimpleName();
    String path;
    Utils.OnFileDownloadComplete mOnFileDownloadComplete;

    public FileDownloadAyncTask(String path, Utils.OnFileDownloadComplete mOnFileDownloadComplete) {
        this.path = path;
        this.mOnFileDownloadComplete = mOnFileDownloadComplete;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String filePath = downloadFile();
        try {
            // Create a URL for the desired page
            URL url = new URL(path);

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder("");
            String str;
            while ((str = in.readLine()) != null) {
                // str is one line of text; readLine() strips the newline character(s)
                sb.append(str);
            }
            in.close();
            return sb.toString();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        if (filePath != null) return filePath;
        return null;
    }

    private String downloadFile() {
        int count;

        try {
            URL url = new URL(path);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            String filePath = Environment
                    .getExternalStorageDirectory().toString()
                    + "/" + this.path.hashCode() + ".txt";
            OutputStream output = new FileOutputStream(filePath);

            byte data[] = new byte[1024];

            long total = 0;
//            Log.d(TAG, "doInBackground: ");
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
//                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            Log.d(TAg, "doInBackground: " + filePath);

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            return filePath;
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        mOnFileDownloadComplete.onFileDownloadComplete(s);
    }
}
