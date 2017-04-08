package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cureinstant.cureinstant.model.doctorDetails.DoctorWorkPlace;

/**
 * Created by lokeshsaini94 on 08-04-2017.
 */

public class BookDoctor implements Parcelable {

    private int userID, workID;
    private String name, username, speciality, picture, fee;
    private DoctorWorkPlace doctorWorkPlace;

    public BookDoctor(int userID, int workID, String name, String username, String speciality, String picture, String fee, DoctorWorkPlace doctorWorkPlace) {
        this.userID = userID;
        this.workID = workID;
        this.name = name;
        this.username = username;
        this.speciality = speciality;
        this.picture = picture;
        this.fee = fee;
        this.doctorWorkPlace = doctorWorkPlace;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getWorkID() {
        return workID;
    }

    public void setWorkID(int workID) {
        this.workID = workID;
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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public DoctorWorkPlace getDoctorWorkPlace() {
        return doctorWorkPlace;
    }

    public void setDoctorWorkPlace(DoctorWorkPlace doctorWorkPlace) {
        this.doctorWorkPlace = doctorWorkPlace;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userID);
        dest.writeInt(this.workID);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.speciality);
        dest.writeString(this.picture);
        dest.writeString(this.fee);
        dest.writeParcelable(this.doctorWorkPlace, flags);
    }

    protected BookDoctor(Parcel in) {
        this.userID = in.readInt();
        this.workID = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.speciality = in.readString();
        this.picture = in.readString();
        this.fee = in.readString();
        this.doctorWorkPlace = in.readParcelable(DoctorWorkPlace.class.getClassLoader());
    }

    public static final Creator<BookDoctor> CREATOR = new Creator<BookDoctor>() {
        @Override
        public BookDoctor createFromParcel(Parcel source) {
            return new BookDoctor(source);
        }

        @Override
        public BookDoctor[] newArray(int size) {
            return new BookDoctor[size];
        }
    };
}
