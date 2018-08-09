package com.udacity.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class Step implements Parcelable {

    public int id;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {

        @Override
        public Step createFromParcel(Parcel in) {
            Step step = new Step();
            step.readFromParcel(in);
            return step;
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
