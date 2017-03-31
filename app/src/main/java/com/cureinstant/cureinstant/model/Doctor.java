package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lokeshsaini94 on 15-03-2017.
 */

public class Doctor implements Parcelable {
    private String accountType, name, username, sex, email, number, address, summary, speciality, profilePicture;
    private int followers, followings;
    private ArrayList<String> album;
    private ArrayList<DoctorEduDetail> doctorEduDetails;
    private ArrayList<DoctorSkill> doctorSkills;
    private ArrayList<DoctorWorkDetail> doctorWorkDetails;
    private ArrayList<DoctorAchievement> doctorAchievements;
    private ArrayList<DoctorPublication> doctorPublications;
    private ArrayList<DoctorFeedback> doctorFeedbacks;


    public Doctor(String accountType, String name, String username, String sex, String email, String number, String address, String summary, String speciality, String profilePicture, int followers, int followings, ArrayList<String> album, ArrayList<DoctorEduDetail> doctorEduDetails, ArrayList<DoctorSkill> doctorSkills, ArrayList<DoctorWorkDetail> doctorWorkDetails, ArrayList<DoctorAchievement> doctorAchievements, ArrayList<DoctorPublication> doctorPublications, ArrayList<DoctorFeedback> doctorFeedbacks) {
        this.accountType = accountType;
        this.name = name;
        this.username = username;
        this.sex = sex;
        this.email = email;
        this.number = number;
        this.address = address;
        this.summary = summary;
        this.speciality = speciality;
        this.profilePicture = profilePicture;
        this.followers = followers;
        this.followings = followings;
        this.album = album;
        this.doctorEduDetails = doctorEduDetails;
        this.doctorSkills = doctorSkills;
        this.doctorWorkDetails = doctorWorkDetails;
        this.doctorAchievements = doctorAchievements;
        this.doctorPublications = doctorPublications;
        this.doctorFeedbacks = doctorFeedbacks;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public ArrayList<String> getAlbum() {
        return album;
    }

    public void setAlbum(ArrayList<String> album) {
        this.album = album;
    }

    public ArrayList<DoctorEduDetail> getDoctorEduDetails() {
        return doctorEduDetails;
    }

    public void setDoctorEduDetails(ArrayList<DoctorEduDetail> doctorEduDetails) {
        this.doctorEduDetails = doctorEduDetails;
    }

    public ArrayList<DoctorSkill> getDoctorSkills() {
        return doctorSkills;
    }

    public void setDoctorSkills(ArrayList<DoctorSkill> doctorSkills) {
        this.doctorSkills = doctorSkills;
    }

    public ArrayList<DoctorWorkDetail> getDoctorWorkDetails() {
        return doctorWorkDetails;
    }

    public void setDoctorWorkDetails(ArrayList<DoctorWorkDetail> doctorWorkDetails) {
        this.doctorWorkDetails = doctorWorkDetails;
    }

    public ArrayList<DoctorAchievement> getDoctorAchievements() {
        return doctorAchievements;
    }

    public void setDoctorAchievements(ArrayList<DoctorAchievement> doctorAchievements) {
        this.doctorAchievements = doctorAchievements;
    }

    public ArrayList<DoctorPublication> getDoctorPublications() {
        return doctorPublications;
    }

    public void setDoctorPublications(ArrayList<DoctorPublication> doctorPublications) {
        this.doctorPublications = doctorPublications;
    }

    public ArrayList<DoctorFeedback> getDoctorFeedbacks() {
        return doctorFeedbacks;
    }

    public void setDoctorFeedbacks(ArrayList<DoctorFeedback> doctorFeedbacks) {
        this.doctorFeedbacks = doctorFeedbacks;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountType);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.sex);
        dest.writeString(this.email);
        dest.writeString(this.number);
        dest.writeString(this.address);
        dest.writeString(this.summary);
        dest.writeString(this.speciality);
        dest.writeString(this.profilePicture);
        dest.writeInt(this.followers);
        dest.writeInt(this.followings);
        dest.writeStringList(this.album);
        dest.writeTypedList(this.doctorEduDetails);
        dest.writeTypedList(this.doctorSkills);
        dest.writeTypedList(this.doctorWorkDetails);
        dest.writeTypedList(this.doctorAchievements);
        dest.writeTypedList(this.doctorPublications);
        dest.writeTypedList(this.doctorFeedbacks);
    }

    protected Doctor(Parcel in) {
        this.accountType = in.readString();
        this.name = in.readString();
        this.username = in.readString();
        this.sex = in.readString();
        this.email = in.readString();
        this.number = in.readString();
        this.address = in.readString();
        this.summary = in.readString();
        this.speciality = in.readString();
        this.profilePicture = in.readString();
        this.followers = in.readInt();
        this.followings = in.readInt();
        this.album = in.createStringArrayList();
        this.doctorEduDetails = in.createTypedArrayList(DoctorEduDetail.CREATOR);
        this.doctorSkills = in.createTypedArrayList(DoctorSkill.CREATOR);
        this.doctorWorkDetails = in.createTypedArrayList(DoctorWorkDetail.CREATOR);
        this.doctorAchievements = in.createTypedArrayList(DoctorAchievement.CREATOR);
        this.doctorPublications = in.createTypedArrayList(DoctorPublication.CREATOR);
        this.doctorFeedbacks = in.createTypedArrayList(DoctorFeedback.CREATOR);
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
