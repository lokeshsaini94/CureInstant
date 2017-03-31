package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 31-03-2017.
 */

public class DoctorAchievement implements Parcelable {
    private String title, desc, link, image;

    public DoctorAchievement(String title, String desc, String link, String image) {
        this.title = title;
        this.desc = desc;
        this.link = link;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.desc);
        dest.writeString(this.link);
        dest.writeString(this.image);
    }

    protected DoctorAchievement(Parcel in) {
        this.title = in.readString();
        this.desc = in.readString();
        this.link = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<DoctorAchievement> CREATOR = new Parcelable.Creator<DoctorAchievement>() {
        @Override
        public DoctorAchievement createFromParcel(Parcel source) {
            return new DoctorAchievement(source);
        }

        @Override
        public DoctorAchievement[] newArray(int size) {
            return new DoctorAchievement[size];
        }
    };
}
