package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 23-03-2017.
 */

public class Answer implements Parcelable {

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
    private String content, time;
    private int id, replyCount, likes;
    private boolean liked;
    private String name, username, speciality, picture;

    public Answer(String content, String time, int id, int replyCount, int likes, boolean liked, String name, String username, String speciality, String picture) {
        this.content = content;
        this.time = time;
        this.id = id;
        this.replyCount = replyCount;
        this.likes = likes;
        this.liked = liked;
        this.name = name;
        this.username = username;
        this.speciality = speciality;
        this.picture = picture;
    }

    protected Answer(Parcel in) {
        this.content = in.readString();
        this.time = in.readString();
        this.id = in.readInt();
        this.replyCount = in.readInt();
        this.likes = in.readInt();
        this.liked = in.readByte() != 0;
        this.name = in.readString();
        this.username = in.readString();
        this.speciality = in.readString();
        this.picture = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeInt(this.id);
        dest.writeInt(this.replyCount);
        dest.writeInt(this.likes);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.speciality);
        dest.writeString(this.picture);
    }
}
