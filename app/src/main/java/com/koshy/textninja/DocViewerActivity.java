package com.koshy.textninja;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class DocViewerActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewer);

        String stringData = Utils.loadJSONFromAsset(this);
        String[] paras = stringData.split("\n");
        String[] ar = new String[]{paras[paras.length - 3], paras[paras.length - 2], paras[paras.length - 1]};
        DocViewAdapter docViewAdapter = new DocViewAdapter(ar, this);
        mRecyclerView = findViewById(R.id.doc_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(docViewAdapter);
    }
}
