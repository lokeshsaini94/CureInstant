package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 31-03-2017.
 */

public class DoctorWorkDetail implements Parcelable {

    private int workID;
    private String specialty, position, fee, startDate, endDate;
    private DoctorWorkPlace doctorWorkPlace;

    public DoctorWorkDetail(int workID, String specialty, String position, String fee, String startDate, String endDate, DoctorWorkPlace doctorWorkPlace) {
        this.workID = workID;
        this.specialty = specialty;
        this.position = position;
        this.fee = fee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.doctorWorkPlace = doctorWorkPlace;
    }

    public int getWorkID() {
        return workID;
    }

    public void setWorkID(int workID) {
        this.workID = workID;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
        dest.writeInt(this.workID);
        dest.writeString(this.specialty);
        dest.writeString(this.position);
        dest.writeString(this.fee);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeParcelable(this.doctorWorkPlace, flags);
    }

    protected DoctorWorkDetail(Parcel in) {
        this.workID = in.readInt();
        this.specialty = in.readString();
        this.position = in.readString();
        this.fee = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.doctorWorkPlace = in.readParcelable(DoctorWorkPlace.class.getClassLoader());
    }

    public static final Parcelable.Creator<DoctorWorkDetail> CREATOR = new Parcelable.Creator<DoctorWorkDetail>() {
        @Override
        public DoctorWorkDetail createFromParcel(Parcel source) {
            return new DoctorWorkDetail(source);
        }

        @Override
        public DoctorWorkDetail[] newArray(int size) {
            return new DoctorWorkDetail[size];
        }
    };
}
