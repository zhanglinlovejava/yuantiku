package com.colin.tiankong.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Colin.Zhang on 2017/3/30.
 */

public class ResultEntity implements Parcelable {
    public int index;
    public boolean isAnswer;

    public ResultEntity(int index, boolean isAnswer) {
        this.index = index;
        this.isAnswer = isAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeByte(this.isAnswer ? (byte) 1 : (byte) 0);
    }

    public ResultEntity() {
    }

    protected ResultEntity(Parcel in) {
        this.index = in.readInt();
        this.isAnswer = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ResultEntity> CREATOR = new Parcelable.Creator<ResultEntity>() {
        @Override
        public ResultEntity createFromParcel(Parcel source) {
            return new ResultEntity(source);
        }

        @Override
        public ResultEntity[] newArray(int size) {
            return new ResultEntity[size];
        }
    };
}
