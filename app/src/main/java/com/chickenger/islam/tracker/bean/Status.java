package com.chickenger.islam.tracker.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.chickenger.islam.tracker.R;

public class Status implements Parcelable {

    public static final Status UNSET = new Status(0, "unset", R.string.status_unknown, 0, 0, false);
    public static final Status MISSED = new Status(1, "missed", R.string.status_missed, R.drawable.ic_status_missed_red_800_18dp, -10, false);
    public static final Status LATE = new Status(2, "late", R.string.status_late, R.drawable.ic_status_delay_grey_800_18dp, 5, true);
    public static final Status ONTIME = new Status(3, "ontime", R.string.status_ontime, R.drawable.ic_status_ontime_green_800_18dp, 10, true);
    public static final Status DEFAULT = UNSET;

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    private int id;
    private String key;
    private int labelResId;
    private int iconResId;
    private int score;
    private boolean isDone;

    private Status(int id, String key, int labelResId, int iconResId, int score, boolean isDone) {
        this.id = id;
        this.key = key;
        this.labelResId = labelResId;
        this.iconResId = iconResId;
        this.score = score;
        this.isDone = isDone;
    }

    protected Status(Parcel in) {
        id = in.readInt();
        key = in.readString();
        labelResId = in.readInt();
        score = in.readInt();
    }

    public static Status ofId(int id) {
        switch (id) {
            case 0:
                return UNSET;
            case 1:
                return MISSED;
            case 2:
                return LATE;
            case 3:
                return ONTIME;
            default:
                throw new IllegalArgumentException("There is no Status with id " + id);
        }
    }

    public static Status ofKey(String key) {
        switch (key) {
            case "unknown":
                return UNSET;
            case "missed":
                return MISSED;
            case "late":
                return LATE;
            case "ontime":
                return ONTIME;
            default:
                throw new IllegalArgumentException("There is no Status with key " + key);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(key);
        dest.writeInt(labelResId);
        dest.writeInt(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    @StringRes
    public int getLabelResId() {
        return labelResId;
    }

    @DrawableRes
    public int getIconResId() {
        return iconResId;
    }

    public int getScore() {
        return score;
    }

    public boolean isDone() {
        return isDone;
    }

}
