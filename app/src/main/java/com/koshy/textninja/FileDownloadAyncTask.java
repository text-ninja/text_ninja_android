package com.koshy.textninja;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloadAyncTask extends AsyncTask<Void, Void, String> {
    String path;



    @Override
    protected String doInBackground(Void... voids) {
        try {
            // Create a URL for the desired page
            URL url = new URL("ksite.com/thefile.txt");

            // Read all the text returned by the server
            InputStream in1 = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(in1));
            String str;
            StringBuilder sb = new StringBuilder("");
            while ((str = in.readLine()) != null) {
                // str is one line of text; readLine() strips the newline character(s)

            }
            in.close();
//            return str
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

    }
}
