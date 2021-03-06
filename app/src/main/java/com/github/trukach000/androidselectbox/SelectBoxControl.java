package com.github.trukach000.androidselectbox;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trukach000@gmail.com on 16.04.2017.
 */

public class SelectBoxControl extends AppCompatTextView {
    private static final String TAG = SelectBoxControl.class.getSimpleName();

    private ItemSelectedListener selectedListener;

    public final int DEFAULT_REQUEST_CODE = 1234;

    public static final String KEY_ACTIVITY_TITLE = "KEY_ACTIVITY_TITLE";
    public static final String KEY_SELECTED_ITEM_ID = "KEY_SELECTED_ITEM_ID";
    public static final String KEY_DATA_ARRAY = "KEY_DATA_ARRAY";

    /**
     * If fragment is using for handling onActivityResult set this parameter to link of your fragment
     * for using mParentFragment.startActivityForResult
     * If is null then will be used getActivity().startActivityForResult
     */
    private Fragment mParentFragment;
    /**
     * Text of control when nothing selected. Can be set via xml attrs
     */
    private String mEmptySelectedTitle;
    /**
     * Select activity title. Can be set via xml attrs
     */
    private String mActivityTitle;
    /**
     * Request code for `startActivityForResult` call. Can be set via xml attrs
     */
    private int mRequestCode;
    /**
     * Selected item id, if nothing selected it is null
     */
    private String mSelectedItemId = null;
    private List<ISelectableIItem> mData = new ArrayList<ISelectableIItem>();


    public SelectBoxControl(Context context) {
        super(context);
        initAttrs(context, null);
        init(context);
    }

    public SelectBoxControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init(context);
    }

    public SelectBoxControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            mEmptySelectedTitle = "Nothing selected";
            mActivityTitle = "Select";
            mRequestCode = DEFAULT_REQUEST_CODE;
        } else {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.SelectBoxControl,
                    0, 0);

            try {
                mEmptySelectedTitle = a.getString(R.styleable.SelectBoxControl_emptySelectedTitle);
                if (mEmptySelectedTitle == null || mEmptySelectedTitle.isEmpty()) {
                    mEmptySelectedTitle = "N1othing selected";
                }
                mActivityTitle = a.getString(R.styleable.SelectBoxControl_activityTitle);
                if (mActivityTitle == null || mActivityTitle.isEmpty()) {
                    mActivityTitle = "Select";
                }
                mRequestCode = a.getInt(R.styleable.SelectBoxControl_requestCode, DEFAULT_REQUEST_CODE);
            } finally {
                a.recycle();
            }
        }
    }

    private void init(Context context) {
        setText(mEmptySelectedTitle);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mParentFragment != null) {
                    Activity act = getActivity();
                    if (act != null) {
                        mParentFragment.startActivityForResult(fillIntentForSelectActivity(), mRequestCode);
                        getActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    }
                } else {
                    Activity act = getActivity();
                    if (act != null) {
                        act.startActivityForResult(fillIntentForSelectActivity(), mRequestCode);
                    }
                }
            }
        });
    }

    private Intent fillIntentForSelectActivity() {
        Intent intent = new Intent(getContext(), WidgetSelectActivity.class);
        intent.putExtra(KEY_ACTIVITY_TITLE, mActivityTitle);
        intent.putExtra(KEY_SELECTED_ITEM_ID, mSelectedItemId);
        intent.setExtrasClassLoader(mData.getClass().getClassLoader());
        intent.putParcelableArrayListExtra(KEY_DATA_ARRAY, (ArrayList<ISelectableIItem>) mData);
        return intent;
    }

    public void setOnSelectListener(ItemSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public void setActivityTitle(String activityTitle) {
        this.mActivityTitle = activityTitle;
    }

    public void setParentFragment(Fragment frg) {
        this.mParentFragment = frg;
    }

    public void setData(List<ISelectableIItem> mData) {
        if(mData == null) {
            mData = new ArrayList<>();
        }
        this.mData = mData;
    }

    public void setRequestCode(int mRequestCode) {
        this.mRequestCode = mRequestCode;
    }

    /**
     * Set item with that id as the selected one if mData array contains it
     * do not rise ItemSelectedListener.onItemSelect
     * @param selectedItemId
     * @return
     */
    public boolean selectItemById(String selectedItemId) {
        for (ISelectableIItem it : mData) {
            if (it.getId().equals(selectedItemId)) {
                setText(it.getTitle());
                this.mSelectedItemId = selectedItemId;
                return true;
            }
        }
        return false;
    }

    /**
     * Return selected item id
     *
     * @return null if selected nothing
     */
    public String getSelectedItemId() {
        return mSelectedItemId;
    }

    public void submit(int requestCode, int resultCode, Intent data) {
        Log.d("RES","Request code :" + requestCode);
        Log.d("RES","Result code :" + resultCode);
        if (requestCode == this.mRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                String selectedId = data.getStringExtra(KEY_SELECTED_ITEM_ID);
                Log.d("RES","selectedId :" + selectedId);
                if(selectedId != null) {
                    if (selectItemById(selectedId)) {
                        if (selectedListener != null) {
                            selectedListener.onItemSelect(selectedId);
                        }
                    }
                }

            }
        }
    }


    @Nullable
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public interface ItemSelectedListener {
        void onItemSelect(String itemId);
    }

}