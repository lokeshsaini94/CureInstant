package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 15-03-2017.
 */

public class Feed implements Parcelable {

    private String type, actionName, actionType;
    private String title, content, time, likes, followings, comments, shares;
    private String doctorName, doctorUsername, doctorSpec, doctorPicture;

    public Feed(String type, String actionName, String actionType, String title, String content, String time, String likes, String followings, String comments, String shares, String doctorName, String doctorUsername, String doctorSpec, String doctorPicture) {
        this.type = type;
        this.actionName = actionName;
        this.actionType = actionType;
        this.title = title;
        this.content = content;
        this.time = time;
        this.likes = likes;
        this.followings = followings;
        this.comments = comments;
        this.shares = shares;
        this.doctorName = doctorName;
        this.doctorUsername = doctorUsername;
        this.doctorSpec = doctorSpec;
        this.doctorPicture = doctorPicture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getFollowings() {
        return followings;
    }

    public void setFollowings(String followings) {
        this.followings = followings;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public String getDoctorSpec() {
        return doctorSpec;
    }

    public void setDoctorSpec(String doctorSpec) {
        this.doctorSpec = doctorSpec;
    }

    public String getDoctorPicture() {
        return doctorPicture;
    }

    public void setDoctorPicture(String doctorPicture) {
        this.doctorPicture = doctorPicture;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.actionName);
        dest.writeString(this.actionType);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeString(this.likes);
        dest.writeString(this.followings);
        dest.writeString(this.comments);
        dest.writeString(this.shares);
        dest.writeString(this.doctorName);
        dest.writeString(this.doctorUsername);
        dest.writeString(this.doctorSpec);
        dest.writeString(this.doctorPicture);
    }

    protected Feed(Parcel in) {
        this.type = in.readString();
        this.actionName = in.readString();
        this.actionType = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.likes = in.readString();
        this.followings = in.readString();
        this.comments = in.readString();
        this.shares = in.readString();
        this.doctorName = in.readString();
        this.doctorUsername = in.readString();
        this.doctorSpec = in.readString();
        this.doctorPicture = in.readString();
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel source) {
            return new Feed(source);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}
