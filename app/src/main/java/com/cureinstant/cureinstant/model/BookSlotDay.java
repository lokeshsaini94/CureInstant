package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 11-04-2017.
 */

public class BookSlotDay implements Parcelable {

    public static final Creator<BookSlotDay> CREATOR = new Creator<BookSlotDay>() {
        @Override
        public BookSlotDay createFromParcel(Parcel source) {
            return new BookSlotDay(source);
        }

        @Override
        public BookSlotDay[] newArray(int size) {
            return new BookSlotDay[size];
        }
    };
    private int dayIndex;
    private String day, date, startTime, endTime, interval;
    private int availID;
    private ArrayList<BookSlot> bookSlots;

    public BookSlotDay(int dayIndex, String day, String date, String startTime, String endTime, String interval, int availID, ArrayList<BookSlot> bookSlots) {
        this.dayIndex = dayIndex;
        this.day = day;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.interval = interval;
        this.availID = availID;
        this.bookSlots = bookSlots;
    }

    protected BookSlotDay(Parcel in) {
        this.dayIndex = in.readInt();
        this.day = in.readString();
        this.date = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.interval = in.readString();
        this.availID = in.readInt();
        this.bookSlots = in.createTypedArrayList(BookSlot.CREATOR);
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public int getAvailID() {
        return availID;
    }

    public void setAvailID(int availID) {
        this.availID = availID;
    }

    public ArrayList<BookSlot> getBookSlots() {
        return bookSlots;
    }

    public void setBookSlots(ArrayList<BookSlot> bookSlots) {
        this.bookSlots = bookSlots;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dayIndex);
        dest.writeString(this.day);
        dest.writeString(this.date);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.interval);
        dest.writeInt(this.availID);
        dest.writeTypedList(this.bookSlots);
    }
}
