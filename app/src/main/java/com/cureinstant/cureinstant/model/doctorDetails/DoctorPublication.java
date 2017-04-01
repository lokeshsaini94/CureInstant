package com.cureinstant.cureinstant.model.doctorDetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 31-03-2017.
 */

public class DoctorPublication implements Parcelable {
    public static final Parcelable.Creator<DoctorPublication> CREATOR = new Parcelable.Creator<DoctorPublication>() {
        @Override
        public DoctorPublication createFromParcel(Parcel source) {
            return new DoctorPublication(source);
        }

        @Override
        public DoctorPublication[] newArray(int size) {
            return new DoctorPublication[size];
        }
    };
    private String title, content;

    public DoctorPublication(String title, String content) {
        this.title = title;
        this.content = content;
    }

    protected DoctorPublication(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
    }
}
