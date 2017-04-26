package com.github.trukach000.androidselectbox;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.github.trukach000.androidselectbox.Adapter.IOnItemSelectedListener;
import com.github.trukach000.androidselectbox.Adapter.SelectListAdapter;

import java.util.ArrayList;
import java.util.List;

public class WidgetSelectActivity extends AppCompatActivity {

    private RecyclerView mListView;
    private SelectListAdapter mListAdapter;
    private String mTitle = "Select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_select);
        Log.d("WidgetSelectActivity","Activity created");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        List<ISelectableIItem> data = new ArrayList<>();
        if(savedInstanceState ==null) {
            Intent intent = getIntent();
            String selectedItemId = null;
            Log.d("WidgetSelectActivity","savedInstanceState is null");
            if (intent != null) {
                Log.d("WidgetSelectActivity","Intent is not null");
                data = intent.getParcelableArrayListExtra(SelectBoxControl.KEY_DATA_ARRAY);
                if (data == null) {
                    data = new ArrayList<ISelectableIItem>();
                }
                Log.d("WidgetSelectActivity","Data is " + data.toString());
                mTitle = intent.getStringExtra(SelectBoxControl.KEY_ACTIVITY_TITLE);
                selectedItemId = intent.getStringExtra(SelectBoxControl.KEY_SELECTED_ITEM_ID);

            }

            mListAdapter = new SelectListAdapter(data,this);
            mListAdapter.selectById(selectedItemId);
        }else{
            mListAdapter = SelectListAdapter.restoreInstanceState(savedInstanceState,this);
            mTitle = savedInstanceState.getString(SelectBoxControl.KEY_ACTIVITY_TITLE);

        }

        if(actionBar!=null) {
            if(mTitle == null){
                mTitle = "Select";
            }
            actionBar.setTitle(mTitle);
        }

        mListAdapter.setOnItemSelectedListener(new IOnItemSelectedListener() {
            @Override
            public void itemSelected(ISelectableIItem item) {
                if(item!=null) {
                    Intent intent = new Intent();
                    intent.putExtra(SelectBoxControl.KEY_SELECTED_ITEM_ID, item.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        mListView = (RecyclerView) findViewById(R.id.list);
        mListView.setAdapter(mListAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(llm);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListAdapter.saveInstanceState(outState);
        outState.putString(SelectBoxControl.KEY_ACTIVITY_TITLE,mTitle);
    }
}
