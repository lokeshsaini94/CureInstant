package com.cureinstant.cureinstant.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lokeshsaini94 on 23-03-2017.
 */

public class Answer implements Parcelable {
    public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel source) {
            return new Answer(source);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
    private String comment, replyID, replyCount, likes, time;
    private User user;

    public Answer(String comment, String replyID, String replyCount, String likes, String time, User user) {
        this.comment = comment;
        this.replyID = replyID;
        this.replyCount = replyCount;
        this.likes = likes;
        this.time = time;
        this.user = user;
    }

    protected Answer(Parcel in) {
        this.comment = in.readString();
        this.replyID = in.readString();
        this.replyCount = in.readString();
        this.likes = in.readString();
        this.time = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReplyID() {
        return replyID;
    }

    public void setReplyID(String replyID) {
        this.replyID = replyID;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.comment);
        dest.writeString(this.replyID);
        dest.writeString(this.replyCount);
        dest.writeString(this.likes);
        dest.writeString(this.time);
        dest.writeParcelable(this.user, flags);
    }
}
