package com.chickenger.islam.tracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.chickenger.islam.tracker.bean.DateString;
import com.chickenger.islam.tracker.bean.Place;
import com.chickenger.islam.tracker.bean.Prayer;
import com.chickenger.islam.tracker.bean.Record;
import com.chickenger.islam.tracker.bean.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRecords {

    private static final String STATUS = "status";
    private static final String PLACE = "place";

    private SharedPreferences prefs;


    public UserRecords(Context context) {
        this(context.getSharedPreferences("records", Context.MODE_PRIVATE));
    }

    public UserRecords(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public void setStatus(DateString dateString, Prayer prayer, Status status) {
        String key = String.format("%s_%s_%s", dateString, prayer.getKey(), STATUS);
        prefs.edit()
            .putString(key, status.getKey())
            .apply();
    }

    @NonNull
    public Status getStatus(DateString dateString, Prayer prayer) {
        String key = String.format("%s_%s_%s", dateString, prayer.getKey(), STATUS);
        if (prefs.contains(key)) {
            return Status.ofKey(prefs.getString(key, Status.UNSET.getKey()));
        } else {
            return Status.UNSET;
        }
    }

    public void setPlace(DateString dateString, Prayer prayer, Place place) {
        String key = String.format("%s_%s_%s", dateString, prayer.getKey(), PLACE);
        prefs.edit()
            .putString(key, place.getKey())
            .apply();
    }

    @NonNull
    public Place getPlace(DateString dateString, Prayer prayer) {
        String key = String.format("%s_%s_%s", dateString, prayer.getKey(), PLACE);
        if (prefs.contains(key)) {
            return Place.ofKey(prefs.getString(key, Place.UNSET.getKey()));
        } else {
            return Place.UNSET;
        }
    }

    public Map<DateString, Map<Prayer, Record>> getRecords() {
        Map<DateString, Map<Prayer, Record>> result = new HashMap<>();

        Map<String, ?> all = prefs.getAll();
        Pattern pattern = Pattern.compile("(.+)_(.+)_(.+)");
        Matcher matcher;

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            matcher = pattern.matcher(key);
            if (!matcher.find()) {
                continue;
            }

            DateString dateString = DateString.of(matcher.group(1));
            Prayer prayer = Prayer.ofKey(matcher.group(2));
            String field = matcher.group(3);

            Map<Prayer, Record> prayerRecordMap;
            if (result.containsKey(dateString)) {
                prayerRecordMap = result.get(dateString);
            } else {
                prayerRecordMap = new HashMap<>();
            }

            Record record;
            if (prayerRecordMap.containsKey(prayer)) {
                record = prayerRecordMap.get(prayer);
            } else {
                record = new Record(dateString, prayer);
            }

            if (STATUS.equals(field)) {
                record.setStatus(Status.ofKey(value));
            } else if (PLACE.equals(field)) {
                record.setPlace(Place.ofKey(value));
            }

            prayerRecordMap.put(prayer, record);
            result.put(dateString, prayerRecordMap);
        }

        return result;
    }

}
