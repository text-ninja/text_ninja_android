package com.koshy.textninja;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public interface OnFileDownloadComplete {
        void onFileDownloadComplete(String data);
    }

    public static String loadJSONFromAsset(Context context) {
        String data = null;
        try {
            InputStream is = context.getAssets().open("dummy_text.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            data = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return data;
    }
}
