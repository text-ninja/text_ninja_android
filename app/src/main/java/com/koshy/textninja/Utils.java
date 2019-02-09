package com.koshy.textninja;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.CursorLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public static String readFile(String path) {
        try {
            File file = new File(path);
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            FileInputStream in = null;

            in = new FileInputStream(file);

            try {
                in.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
            String contents = new String(bytes);
            return contents;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
