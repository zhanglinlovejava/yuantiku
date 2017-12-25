package com.lin.yuantiku.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Colin.Zhang on 2017/3/28.
 */

public class ChooseItemParent implements Parcelable {
    public int index;
    public List<ChooseItem> list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeTypedList(this.list);
    }

    public ChooseItemParent() {
    }

    protected ChooseItemParent(Parcel in) {
        this.index = in.readInt();
        this.list = in.createTypedArrayList(ChooseItem.CREATOR);
    }

    public static final Creator<ChooseItemParent> CREATOR = new Creator<ChooseItemParent>() {
        @Override
        public ChooseItemParent createFromParcel(Parcel source) {
            return new ChooseItemParent(source);
        }

        @Override
        public ChooseItemParent[] newArray(int size) {
            return new ChooseItemParent[size];
        }
    };
}
