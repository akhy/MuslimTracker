package com.chickenger.islam.tracker.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.chickenger.islam.tracker.R;

public class Prayer implements Parcelable {

    public static final String KEY_FAJR = "fajr";
    public static final String KEY_ZUHR = "zuhr";
    public static final String KEY_ASR = "asr";
    public static final String KEY_MAGHRIB = "maghrib";
    public static final String KEY_ISHA = "isha";

    public static final Prayer FAJR = new Prayer(0, KEY_FAJR, R.string.prayer_fajr, R.drawable.prayer_bg_fajr);
    public static final Prayer ZUHR = new Prayer(1, KEY_ZUHR, R.string.prayer_zuhr, R.drawable.prayer_bg_zuhr);
    public static final Prayer ASR = new Prayer(2, KEY_ASR, R.string.prayer_asr, R.drawable.prayer_bg_asr);
    public static final Prayer MAGHRIB = new Prayer(3, KEY_MAGHRIB, R.string.prayer_maghrib, R.drawable.prayer_bg_maghrib);
    public static final Prayer ISHA = new Prayer(4, KEY_ISHA, R.string.prayer_isha, R.drawable.prayer_bg_isha);
    public static final Creator<Prayer> CREATOR = new Creator<Prayer>() {
        @Override
        public Prayer createFromParcel(Parcel in) {
            return new Prayer(in);
        }

        @Override
        public Prayer[] newArray(int size) {
            return new Prayer[size];
        }
    };
    private int id;
    private String key;
    private int labelResId;
    private int bgResId;

    public Prayer(int id, String key, int labelResId, int bgResId) {
        this.id = id;
        this.key = key;
        this.labelResId = labelResId;
        this.bgResId = bgResId;
    }

    protected Prayer(Parcel in) {
        id = in.readInt();
        key = in.readString();
        labelResId = in.readInt();
        bgResId = in.readInt();
    }

    public static Prayer ofId(int id) {
        switch (id) {
            case 0:
                return FAJR;
            case 1:
                return ZUHR;
            case 2:
                return ASR;
            case 3:
                return MAGHRIB;
            case 4:
                return ISHA;
            default:
                throw new IllegalArgumentException("There is no Prayer with id " + id);
        }
    }

    public static Prayer ofKey(String key) {
        switch (key) {
            case KEY_FAJR:
                return FAJR;
            case KEY_ZUHR:
                return ZUHR;
            case KEY_ASR:
                return ASR;
            case KEY_MAGHRIB:
                return MAGHRIB;
            case KEY_ISHA:
                return ISHA;
            default:
                throw new IllegalArgumentException("There is no Prayer with key " + key);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(key);
        dest.writeInt(labelResId);
        dest.writeInt(bgResId);
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
    public int getBgResId() {
        return bgResId;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prayer prayer = (Prayer) o;

        if (id != prayer.id) return false;
        if (labelResId != prayer.labelResId) return false;
        if (bgResId != prayer.bgResId) return false;

        return key.equals(prayer.key);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + key.hashCode();
        result = 31 * result + labelResId;
        result = 31 * result + bgResId;
        return result;
    }
}
