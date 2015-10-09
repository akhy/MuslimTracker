package com.chickenger.islam.tracker.bean;

public class Record {

    private DateString dateString;
    private Prayer prayer;
    private Status status = Status.DEFAULT;
    private Place place = Place.OTHER;

    public Record(DateString dateString, Prayer prayer) {
        if (dateString == null || prayer == null) {
            throw new IllegalArgumentException("dateString and prayer must not be null");
        }

        this.dateString = dateString;
        this.prayer = prayer;
    }

    public DateString getDateString() {
        return dateString;
    }

    public void setDateString(DateString dateString) {
        this.dateString = dateString;
    }

    public Prayer getPrayer() {
        return prayer;
    }

    public void setPrayer(Prayer prayer) {
        this.prayer = prayer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public static boolean sameKeys(Record r1, Record r2) {
        return r1.dateString.equals(r2.dateString) && r1.prayer.equals(r2.prayer);
    }

}
