package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 06-04-2017.
 */

public class SearchProfile implements Parcelable {
    public static final Parcelable.Creator<SearchProfile> CREATOR = new Parcelable.Creator<SearchProfile>() {
        @Override
        public SearchProfile createFromParcel(Parcel source) {
            return new SearchProfile(source);
        }

        @Override
        public SearchProfile[] newArray(int size) {
            return new SearchProfile[size];
        }
    };
    private int id;
    private String name, username, speciality, picture;

    public SearchProfile(int id, String name, String username, String speciality, String picture) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.speciality = speciality;
        this.picture = picture;
    }

    protected SearchProfile(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.speciality = in.readString();
        this.picture = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.speciality);
        dest.writeString(this.picture);
    }
}
