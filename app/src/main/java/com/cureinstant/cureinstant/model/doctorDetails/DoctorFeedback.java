package com.cureinstant.cureinstant.model.doctorDetails;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 31-03-2017.
 */

public class DoctorFeedback implements Parcelable {
    public static final Parcelable.Creator<DoctorFeedback> CREATOR = new Parcelable.Creator<DoctorFeedback>() {
        @Override
        public DoctorFeedback createFromParcel(Parcel source) {
            return new DoctorFeedback(source);
        }

        @Override
        public DoctorFeedback[] newArray(int size) {
            return new DoctorFeedback[size];
        }
    };
    private String feedback, name, username, picture;

    public DoctorFeedback(String feedback, String name, String username, String picture) {
        this.feedback = feedback;
        this.name = name;
        this.username = username;
        this.picture = picture;
    }

    protected DoctorFeedback(Parcel in) {
        this.feedback = in.readString();
        this.name = in.readString();
        this.username = in.readString();
        this.picture = in.readString();
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.feedback);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.picture);
    }
}
