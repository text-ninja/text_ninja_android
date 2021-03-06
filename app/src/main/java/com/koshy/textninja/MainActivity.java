package com.koshy.textninja;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Utils.OnFileDownloadComplete {
    public static final String DEFAULT_FILE_NETWORK_PATH = "https://raw.githubusercontent.com/text-ninja/text-ninja/master/test_file.txt";
    public static final int REQUEST_CODE_DOC = 101;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String FILE_DATA = "fileData";
    public static final int PERMISSION_REQUEST_CODE = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText mEditText = findViewById(R.id.enter_url_edit_text);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
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

                String path;
                if (TextUtils.isEmpty(mEditText.getText().toString())) {
                    path = DEFAULT_FILE_NETWORK_PATH;
                } else {
                    path = mEditText.getText().toString();
                }
                FileDownloadAyncTask fileDownloadAyncTask = new FileDownloadAyncTask(path, MainActivity.this);
                fileDownloadAyncTask.execute();
            }
        });

    }

    private void browseDocuments() {

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain",
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
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), REQUEST_CODE_DOC);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.d(TAG, "onActivityResult: ");
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_DOC:
                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: " + uri.getPath());
                try {
                    String stringPath = FileUtils.getPath(this, uri);
                    Log.d(TAG, "onActivityResult: " + stringPath);

                    ArrayList<String> stringArrayList = Utils.readFile(stringPath);
                    if (stringArrayList == null || stringArrayList.size() == 0) {
                        Toast.makeText(this, R.string.enter_valid_path, Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.d(TAG, "onActivityResult: " + stringArrayList.size());
                    startNextActivity(stringArrayList);
                } catch (Exception e) {
                    Toast.makeText(this, R.string.file_failed_to_fetch, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onActivityResult: " + e.getMessage());
                }
        }
    }

    public void startNextActivity(ArrayList<String> stringData) {
        Intent intent = new Intent(MainActivity.this, DocViewerActivity.class);
        intent.putStringArrayListExtra(FILE_DATA, stringData);
        MainActivity.this.startActivity(intent);
    }


    @Override
    public void onFileDownloadComplete(ArrayList<String> data) {
        Log.d(TAG, "onFileDownloadComplete: Data : " + data.size());

        if (data == null || data.size() == 0) {
            Toast.makeText(this, R.string.enter_valid_path, Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(TAG, "onFileDownloadComplete: " + data);
        startNextActivity(data);
    }
}
