package com.github.trukach000.androidselectbox;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import java.util.ArrayList;
import java.util.List;

public class WidgetSelectActivity extends AppCompatActivity {

    private RecyclerView mListView;
    private String mSelectedItemId = null;
    private String mTitle = "Select";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState ==null) {
            Intent intent = getIntent();
            if (intent != null) {
                /*data = intent.getParcelableArrayListExtra(SelectWithListActivity.KEY_DATA_ARRAY);
                if (data == null) {
                    data = new ArrayList<>();
                }*/
                mTitle = intent.getStringExtra(SelectBoxControl.KEY_ACTIVITY_TITLE);
                if(mTitle == null){
                    mTitle = "Select";
                }
                mSelectedItemId = intent.getStringExtra(SelectBoxControl.KEY_SELECTED_ITEM_ID);

            }
            //mAdapter = new FlexibleAdapter<SelectItem>(data);
        }else{
            //data = savedInstanceState.getParcelableArrayList(SelectWithListActivity.KEY_DATA_ARRAY);
           // mAdapter = new FlexibleAdapter<SelectItem>(data);
            //mAdapter.onRestoreInstanceState(savedInstanceState);
            mTitle = savedInstanceState.getString(SelectBoxControl.KEY_ACTIVITY_TITLE);
        }

        actionBar.setTitle(mTitle);

    }
}
