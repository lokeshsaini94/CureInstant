package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 31-03-2017.
 */

public class DoctorEduDetail implements Parcelable {

    private String courseName, instituteName, startDate, endDate;
    private int courseID, instituteID;

    public DoctorEduDetail(String courseName, String instituteName, String startDate, String endDate, int courseID, int instituteID) {
        this.courseName = courseName;
        this.instituteName = instituteName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseID = courseID;
        this.instituteID = instituteID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
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

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getInstituteID() {
        return instituteID;
    }

    public void setInstituteID(int instituteID) {
        this.instituteID = instituteID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseName);
        dest.writeString(this.instituteName);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeInt(this.courseID);
        dest.writeInt(this.instituteID);
    }

    protected DoctorEduDetail(Parcel in) {
        this.courseName = in.readString();
        this.instituteName = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.courseID = in.readInt();
        this.instituteID = in.readInt();
    }

    public static final Parcelable.Creator<DoctorEduDetail> CREATOR = new Parcelable.Creator<DoctorEduDetail>() {
        @Override
        public DoctorEduDetail createFromParcel(Parcel source) {
            return new DoctorEduDetail(source);
        }

        @Override
        public DoctorEduDetail[] newArray(int size) {
            return new DoctorEduDetail[size];
        }
    };
}
