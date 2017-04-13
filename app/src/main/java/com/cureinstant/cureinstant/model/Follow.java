package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 04-04-2017.
 */

public class Follow implements Parcelable {

    public static final Parcelable.Creator<Follow> CREATOR = new Parcelable.Creator<Follow>() {
        @Override
        public Follow createFromParcel(Parcel source) {
            return new Follow(source);
        }

        @Override
        public Follow[] newArray(int size) {
            return new Follow[size];
        }
    };
    private int followID, userID;
    private String name, username, speciality, picture;
    private boolean following;

    public Follow(int followID, int userID, String name, String username, String speciality, String picture, boolean following) {
        this.followID = followID;
        this.userID = userID;
        this.name = name;
        this.username = username;
        this.speciality = speciality;
        this.picture = picture;
        this.following = following;
    }

    protected Follow(Parcel in) {
        this.followID = in.readInt();
        this.userID = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.speciality = in.readString();
        this.picture = in.readString();
        this.following = in.readByte() != 0;
    }

    public int getFollowID() {
        return followID;
    }

    public void setFollowID(int followID) {
        this.followID = followID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.followID);
        dest.writeInt(this.userID);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.speciality);
        dest.writeString(this.picture);
        dest.writeByte(this.following ? (byte) 1 : (byte) 0);
    }
}
