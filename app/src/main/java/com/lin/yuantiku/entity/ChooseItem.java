package com.lin.yuantiku.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Colin.Zhang on 2017/3/29.
 */

public class ChooseItem implements Parcelable {
    public int position;
    public Map<Integer, String> answer = new HashMap<>();
    public String catName;
    public int total;
    public int index;
    public String body;
    public String cat_name;
    public boolean isChoosed;
    public boolean isAnswer;
    public ChooseItem() {
    }

    public ChooseItem(int index, boolean isChoosed) {
        this.index = index;
        this.isChoosed = isChoosed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeInt(this.answer.size());
        for (Map.Entry<Integer, String> entry : this.answer.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.catName);
        dest.writeInt(this.total);
        dest.writeInt(this.index);
        dest.writeString(this.body);
        dest.writeString(this.cat_name);
        dest.writeByte(this.isChoosed ? (byte) 1 : (byte) 0);
    }

    protected ChooseItem(Parcel in) {
        this.position = in.readInt();
        int answerSize = in.readInt();
        this.answer = new HashMap<Integer, String>(answerSize);
        for (int i = 0; i < answerSize; i++) {
            Integer key = (Integer) in.readValue(Integer.class.getClassLoader());
            String value = in.readString();
            this.answer.put(key, value);
        }
        this.catName = in.readString();
        this.total = in.readInt();
        this.index = in.readInt();
        this.body = in.readString();
        this.cat_name = in.readString();
        this.isChoosed = in.readByte() != 0;
    }

    public static final Creator<ChooseItem> CREATOR = new Creator<ChooseItem>() {
        @Override
        public ChooseItem createFromParcel(Parcel source) {
            return new ChooseItem(source);
        }

        @Override
        public ChooseItem[] newArray(int size) {
            return new ChooseItem[size];
        }
    };
}
