package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 23-03-2017.
 */

public class Comment implements Parcelable {
    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    private String comment, time;
    private int replyID, replyCount, likes;
    private String name, username, picture;

    public Comment(String comment, String time, int replyID, int replyCount, int likes, String name, String username, String picture) {
        this.comment = comment;
        this.time = time;
        this.replyID = replyID;
        this.replyCount = replyCount;
        this.likes = likes;
        this.name = name;
        this.username = username;
        this.picture = picture;
    }

    protected Comment(Parcel in) {
        this.comment = in.readString();
        this.time = in.readString();
        this.replyID = in.readInt();
        this.replyCount = in.readInt();
        this.likes = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.picture = in.readString();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
        dest.writeString(this.comment);
        dest.writeString(this.time);
        dest.writeInt(this.replyID);
        dest.writeInt(this.replyCount);
        dest.writeInt(this.likes);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.picture);
    }
}
