package com.github.trukach000.androidselectbox.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.trukach000.androidselectbox.ISelectableIItem;
import com.github.trukach000.androidselectbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobi on 26.04.2017.
 */

public class SelectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String KEY_DATA = "KEY_DATA";
    public static final String KEY_CURRENT_POS = "KEY_CURRENT_POS";

    private List<ISelectableIItem> mData;
    private int mCurrentPos;
    private Context mCtx;
    private IOnItemSelectedListener mOnItemSelectedListener;

    public SelectListAdapter(List<ISelectableIItem> data, Context ctx) {
        this.mData = data;
        this.mCtx = ctx;
        this.mCurrentPos = -1;
    }

    public SelectListAdapter(List<ISelectableIItem> data, Context ctx, int selectedPosition) {
        this.mData = data;
        this.mCtx = ctx;
        this.mCurrentPos = selectedPosition;
    }

    public static SelectListAdapter restoreInstanceState(Bundle bndl, Context ctx) {
        List<ISelectableIItem> dataList = bndl.getParcelableArrayList(KEY_DATA);
        if (dataList != null) {
            SelectListAdapter adapter = new SelectListAdapter(
                    new ArrayList<ISelectableIItem>(dataList),
                    ctx,
                    bndl.getInt(KEY_CURRENT_POS, -1)
            );
            return adapter;
        } else {
            return new SelectListAdapter(
                    new ArrayList<ISelectableIItem>(),
                    ctx
            );
        }
    }

    public void saveInstanceState(Bundle bndl) {
        bndl.setClassLoader(mData.getClass().getClassLoader());
        bndl.putParcelableArrayList(KEY_DATA, new ArrayList<ISelectableIItem>(mData));
        bndl.putInt(KEY_CURRENT_POS, mCurrentPos);
    }

    public IOnItemSelectedListener getOnItemSelectedListener() {
        return mOnItemSelectedListener;
    }

    public void setOnItemSelectedListener(IOnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mCtx)
                .inflate(R.layout.activity_widget_select_default_item, parent, false);
        return new DefaultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DefaultViewHolder) {
            DefaultViewHolder h = (DefaultViewHolder) holder;
            h.mBackground.setSelected(mCurrentPos == position);
            ISelectableIItem item = getItemByPos(position);
            if (item != null) {
                String title = item.getTitle();
                if (title != null) {
                    h.mTextView.setText(title);
                } else {
                    h.mTextView.setText(R.string.unkhown);
                }
            } else {
                h.mTextView.setText(R.string.unkhown);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private void setCurrentPos(int mCurrentPos) {
        this.mCurrentPos = mCurrentPos;
        notifyDataSetChanged();
    }

    public ISelectableIItem getItemByPos(int pos) {
        return mData.get(pos);
    }

    /**
     * @return -1 if nothing is selected
     */
    public int getCurrentItemPos() {
        return mCurrentPos;
    }

    public void selectById(@Nullable String id) {
        if (id == null) return;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId().equals(id)) {
                mCurrentPos = i;
            }
        }
    }

    public int getPositionBy(String id) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public List<ISelectableIItem> getData() {
        return mData;
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public View mBackground;

        private View.OnClickListener clickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBackground.setSelected(true);
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    SelectListAdapter.this.setCurrentPos(pos);
                    if (SelectListAdapter.this.mOnItemSelectedListener != null) {
                        mOnItemSelectedListener.itemSelected(SelectListAdapter.this.getItemByPos(pos));
                    }
                }
            }
        };

        public DefaultViewHolder(View itemView) {
            super(itemView);
            mBackground = (View) itemView.findViewById(R.id.background);
            mTextView = (TextView) itemView.findViewById(R.id.item_text);
            mBackground.setOnClickListener(clickHandler);
        }
    }

}
