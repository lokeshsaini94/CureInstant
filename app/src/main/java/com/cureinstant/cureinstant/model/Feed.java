package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 15-03-2017.
 */

public class Feed implements Parcelable {

    private String type, actionName, actionType, id;
    private String title, content, time;
    private int  likes, followings, comments, shares;
    private boolean liked, followed;
    private String doctorName, doctorUsername, doctorSpec, doctorPicture;

    public Feed(String type, String actionName, String actionType, String id, String title, String content, String time, int likes, int followings, int comments, int shares, boolean liked, boolean followed, String doctorName, String doctorUsername, String doctorSpec, String doctorPicture) {
        this.type = type;
        this.actionName = actionName;
        this.actionType = actionType;
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.likes = likes;
        this.followings = followings;
        this.comments = comments;
        this.shares = shares;
        this.liked = liked;
        this.followed = followed;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
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
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeInt(this.likes);
        dest.writeInt(this.followings);
        dest.writeInt(this.comments);
        dest.writeInt(this.shares);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.followed ? (byte) 1 : (byte) 0);
        dest.writeString(this.doctorName);
        dest.writeString(this.doctorUsername);
        dest.writeString(this.doctorSpec);
        dest.writeString(this.doctorPicture);
    }

    protected Feed(Parcel in) {
        this.type = in.readString();
        this.actionName = in.readString();
        this.actionType = in.readString();
        this.id = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.likes = in.readInt();
        this.followings = in.readInt();
        this.comments = in.readInt();
        this.shares = in.readInt();
        this.liked = in.readByte() != 0;
        this.followed = in.readByte() != 0;
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