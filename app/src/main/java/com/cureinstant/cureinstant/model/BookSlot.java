package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 11-04-2017.
 */

public class BookSlot implements Parcelable {

    private int slotID;
    private String startTime, endTime;

    public BookSlot(int slotID, String startTime, String endTime) {
        this.slotID = slotID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.slotID);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
    }

    protected BookSlot(Parcel in) {
        this.slotID = in.readInt();
        this.startTime = in.readString();
        this.endTime = in.readString();
    }

    public static final Parcelable.Creator<BookSlot> CREATOR = new Parcelable.Creator<BookSlot>() {
        @Override
        public BookSlot createFromParcel(Parcel source) {
            return new BookSlot(source);
        }

        @Override
        public BookSlot[] newArray(int size) {
            return new BookSlot[size];
        }
    };
}
