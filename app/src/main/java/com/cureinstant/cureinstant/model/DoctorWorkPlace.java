package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 31-03-2017.
 */

public class DoctorWorkPlace implements Parcelable {
    private String name, longitude, latitude, locality, sublocality, city, country, postalCode;

    public DoctorWorkPlace(String name, String longitude, String latitude, String locality, String sublocality, String city, String country, String postalCode) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locality = locality;
        this.sublocality = sublocality;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.locality);
        dest.writeString(this.sublocality);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.postalCode);
    }

    protected DoctorWorkPlace(Parcel in) {
        this.name = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.locality = in.readString();
        this.sublocality = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        this.postalCode = in.readString();
    }

    public static final Parcelable.Creator<DoctorWorkPlace> CREATOR = new Parcelable.Creator<DoctorWorkPlace>() {
        @Override
        public DoctorWorkPlace createFromParcel(Parcel source) {
            return new DoctorWorkPlace(source);
        }

        @Override
        public DoctorWorkPlace[] newArray(int size) {
            return new DoctorWorkPlace[size];
        }
    };
}
