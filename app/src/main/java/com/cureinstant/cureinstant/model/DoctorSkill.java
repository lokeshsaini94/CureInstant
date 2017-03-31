package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 31-03-2017.
 */

public class DoctorSkill implements Parcelable {
    private String skill, description;

    public DoctorSkill(String skill, String description) {
        this.skill = skill;
        this.description = description;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.skill);
        dest.writeString(this.description);
    }

    protected DoctorSkill(Parcel in) {
        this.skill = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<DoctorSkill> CREATOR = new Parcelable.Creator<DoctorSkill>() {
        @Override
        public DoctorSkill createFromParcel(Parcel source) {
            return new DoctorSkill(source);
        }

        @Override
        public DoctorSkill[] newArray(int size) {
            return new DoctorSkill[size];
        }
    };
}
