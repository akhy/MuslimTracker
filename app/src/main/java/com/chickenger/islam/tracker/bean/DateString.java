package com.chickenger.islam.tracker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;

public class DateString implements Parcelable, Serializable {

    public static final String PATTERN = "yyyy-MM-dd";
    public static final Parcelable.Creator<DateString> CREATOR = new Parcelable.Creator<DateString>() {
        public DateString createFromParcel(Parcel source) {
            return new DateString(source);
        }

        public DateString[] newArray(int size) {
            return new DateString[size];
        }
    };

    private String string;

    private DateString(String string) {
        if (string == null)
            throw new IllegalArgumentException("String must not be null");

        this.string = string;
    }

    protected DateString(Parcel in) {
        this.string = in.readString();
    }

    public static DateString today() {
        return of(DateTime.now());
    }

    public static DateString of(DateTime dateTime) {
        return new DateString(dateTime.toString(PATTERN));
    }

    public static DateString of(String string) {
        return of(DateTime.parse(string, DateTimeFormat.forPattern(PATTERN)));
    }

    public String getString() {
        return string;
    }

    public DateTime asDateTime() {
        return DateTime.parse(string, DateTimeFormat.forPattern(PATTERN));
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateString that = (DateString) o;

        return string.equals(that.string);

    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.string);
    }
}
