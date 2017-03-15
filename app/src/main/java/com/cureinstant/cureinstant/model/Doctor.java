package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 15-03-2017.
 */

public class Doctor implements Parcelable {

    private String name, username, sex, email, number, accountType, accountSubType, speciality, picture;

    public Doctor(String name, String username, String sex, String email, String number, String accountType, String accountSubType, String speciality, String picture) {
        this.name = name;
        this.username = username;
        this.sex = sex;
        this.email = email;
        this.number = number;
        this.accountType = accountType;
        this.accountSubType = accountSubType;
        this.speciality = speciality;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountSubType() {
        return accountSubType;
    }

    public void setAccountSubType(String accountSubType) {
        this.accountSubType = accountSubType;
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
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.sex);
        dest.writeString(this.email);
        dest.writeString(this.number);
        dest.writeString(this.accountType);
        dest.writeString(this.accountSubType);
        dest.writeString(this.speciality);
        dest.writeString(this.picture);
    }

    protected Doctor(Parcel in) {
        this.name = in.readString();
        this.username = in.readString();
        this.sex = in.readString();
        this.email = in.readString();
        this.number = in.readString();
        this.accountType = in.readString();
        this.accountSubType = in.readString();
        this.speciality = in.readString();
        this.picture = in.readString();
    }

    public static final Parcelable.Creator<Doctor> CREATOR = new Parcelable.Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel source) {
            return new Doctor(source);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };
}
