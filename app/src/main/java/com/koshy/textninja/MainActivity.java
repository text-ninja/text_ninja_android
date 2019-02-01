package com.koshy.textninja;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends AppCompatActivity implements Utils.OnFileDownloadComplete {

    public static final int REQUEST_CODE_DOC = 101;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText mEditText = findViewById(R.id.enter_url_edit_text);
        Button uploadDocButton = findViewById(R.id.upload_doc_button);
        uploadDocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseDocuments();
            }
        });
        Button fetchDocButton = findViewById(R.id.fetch_doc_button);
        fetchDocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DocViewerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


    }
    private void browseDocuments(){

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain",
                        "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), REQUEST_CODE_DOC);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            Log.d(TAG, "onActivityResult: ");
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_DOC:
                Uri uri = data.getData();
                String src = uri.getPath();
                File source = new File(src);
                String filename = uri.getLastPathSegment();
                File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CustomFolder/" + filename);
        }
    }


    @Override
    public void onFileDownloadComplete(String data) {

    }
}
