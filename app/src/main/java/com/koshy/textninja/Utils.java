package com.koshy.textninja;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    public interface OnFileDownloadComplete {
        void onFileDownloadComplete(ArrayList<String> data);
    }

    public interface OnApiFetchComplete {
        void onApiFetchComplete(String data);
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

    public static ArrayList<String> readFile(String path) {
//        StringBuilder text = new StringBuilder();
        ArrayList<String> paragraphs = new ArrayList<>();
        try {
//                File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(path);

            BufferedReader br = new BufferedReader(new FileReader(file));
            paragraphs = getParagraphs(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paragraphs;
    }

    public static ArrayList<String> getParagraphs(BufferedReader br) throws IOException {
        ArrayList<String> paragraphs = new ArrayList<>();
        String line;
        int i = -1;
        while ((line = br.readLine()) != null) {
            if (!TextUtils.isEmpty(line)) {
                line = line.trim();
//                if (paragraphs.size() > 0) {
//                    String lastChar = paragraphs.get(i).substring(paragraphs.get(i).length() - 1);
//                    if (!lastChar.equals(".") && !lastChar.equals("!") && !lastChar.equals("?") && !lastChar.equals("\"")) {
//                        String p = paragraphs.get(i) + " " + line;
//                        Log.d(TAG, "getParagraphs: " + p);
//                        paragraphs.add(i, p);
//                    } else{
//                        paragraphs.add(line);
//                        i++;
//                    }
//                } else {
//                    paragraphs.add(line);
//                    i++;
//                }
                 paragraphs.add(line);

            }
//                    text.append(line);
//                    text.append('\n');
        }
        br.close();
        return paragraphs;
    }

    public static String getFileName(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
