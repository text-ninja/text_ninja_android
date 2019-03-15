package com.koshy.textninja;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DocViewerActivity extends AppCompatActivity implements Utils.OnApiFetchComplete {
    public static final String TAG = DocViewerActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    ArrayList<ArrayList<String>> mVersionsList;
    DocViewAdapter mDocViewAdapter;
    int mCurrentVersion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewer);
        if(mVersionsList == null) {
            mVersionsList = new ArrayList<>();
            ArrayList<String> stringData = getIntent().getStringArrayListExtra(MainActivity.FILE_DATA);
            mVersionsList.add(stringData);
        }
//        setTitle("");
//        RelativeLayout relativeLayout = findViewById(R.id.doc_activity_relative_layout);
        mDocViewAdapter = new DocViewAdapter(mVersionsList.get(0), this);
        mRecyclerView = findViewById(R.id.doc_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mDocViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(Build.VERSION.SDK_INT > 11) {
            invalidateOptionsMenu();
            menu.findItem(R.id.menu_version).setTitle("V:" + (mCurrentVersion + 1));
            menu.findItem(R.id.menu_paras).setTitle("P:" + mVersionsList.get(mCurrentVersion).size());

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                Log.d(TAG, "1onOptionsItemSelected: " + mCurrentVersion + " " + mVersionsList.size());
                mCurrentVersion = 0;
                mVersionsList.subList(1, mVersionsList.size()).clear();
                mDocViewAdapter.setNewVersion(mVersionsList.get(mCurrentVersion));
                Log.d(TAG, "2onOptionsItemSelected: " + mCurrentVersion + " " + mVersionsList.size());
                invalidateOptionsMenu();
                return true;
            case R.id.menu_back:
                Log.d(TAG, "1onOptionsItemSelected: " + mCurrentVersion + " " + mVersionsList.size());

                if (mCurrentVersion == 0) {
                    Toast.makeText(this, R.string.cannot_go_back, Toast.LENGTH_LONG).show();
                    return true;
                }
                mCurrentVersion--;
                mDocViewAdapter.setNewVersion(mVersionsList.get(mCurrentVersion));
                Log.d(TAG, "2onOptionsItemSelected: " + mCurrentVersion + " " + mVersionsList.size());
                invalidateOptionsMenu();

                return true;
            case R.id.menu_forward:
                Log.d(TAG, "1onOptionsItemSelected: " + mCurrentVersion + " " + mVersionsList.size());

                if (mCurrentVersion == mVersionsList.size() - 1) {
                    makeApiCall();
                } else {
                    mCurrentVersion++;
                    mDocViewAdapter.setNewVersion(mVersionsList.get(mCurrentVersion));
                    invalidateOptionsMenu();
                }
                Log.d(TAG, "2onOptionsItemSelected: " + mCurrentVersion + " " + mVersionsList.size());

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeApiCall() {
        ArrayList<String> stringArrayList = mVersionsList.get(mCurrentVersion);
        JSONArray indexesJsonArray = getIndexesJsonArray(mDocViewAdapter.mSelected);
        if (indexesJsonArray != null && stringArrayList.size() > 0) {
            JSONArray jsonArray = new JSONArray(stringArrayList);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("paras", jsonArray);
                jsonObject.put("index", indexesJsonArray);
                ServerApiAsyncTask mServerApiAsyncTask = new ServerApiAsyncTask(this);
                mServerApiAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, R.string.select_paras, Toast.LENGTH_LONG).show();
        }

    }

    private JSONArray getIndexesJsonArray(boolean[] mSelected) {
        List<Integer> integerList = new ArrayList<>();
        for(int i = 0; i < mSelected.length; i++) {
            if (mSelected[i]) {
                integerList.add(i);
            }
        }
        if (integerList.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray(integerList);
        return jsonArray;
    }

    @Override
    public void onApiFetchComplete(String data) {
        Log.d(TAG, "onApiFetchComplete: " + data);
        if (data == null) {
            Toast.makeText(this, R.string.invalid_paras_selected, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(data);
            if (jsonArray.length() == 0) {
                Toast.makeText(this, R.string.invalid_paras_selected, Toast.LENGTH_LONG).show();
                return;
            }

            // Extract numbers from JSON array.
            ArrayList<String> newParas = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                int x = jsonArray.optInt(i);
                ArrayList<String> stringArrayList = mVersionsList.get(mCurrentVersion);
                newParas.add(stringArrayList.get(x));
            }
            mVersionsList.add(newParas);
            mCurrentVersion++;
            mDocViewAdapter.setNewVersion(mVersionsList.get(mCurrentVersion));
            invalidateOptionsMenu();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.invalid_paras_selected, Toast.LENGTH_LONG).show();

        }
    }
}
