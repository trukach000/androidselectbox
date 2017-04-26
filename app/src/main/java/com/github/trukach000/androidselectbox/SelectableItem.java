package com.github.trukach000.androidselectbox;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 16.04.2017.
 */

public class SelectableItem implements ISelectableIItem{

    private String id;
    private String title;

    public SelectableItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }



    /********************* Parcelable implementation *************************/

    public SelectableItem(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
    }

    public static final Creator<SelectableItem> CREATOR = new Creator<SelectableItem>() {
        @Override
        public SelectableItem createFromParcel(Parcel in) {
            return new SelectableItem(in);
        }

        @Override
        public SelectableItem[] newArray(int size) {
            return new SelectableItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
    }
}
