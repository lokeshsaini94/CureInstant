package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 20-03-2017.
 */

public class User implements Parcelable {

    String name, username, sex, email, number, dob, picture;

    public User(String name, String username, String sex, String email, String number, String dob, String picture) {
        this.name = name;
        this.username = username;
        this.sex = sex;
        this.email = email;
        this.number = number;
        this.dob = dob;
        this.picture = picture;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.sex);
        dest.writeString(this.email);
        dest.writeString(this.number);
        dest.writeString(this.dob);
        dest.writeString(this.picture);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.username = in.readString();
        this.sex = in.readString();
        this.email = in.readString();
        this.number = in.readString();
        this.dob = in.readString();
        this.picture = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
