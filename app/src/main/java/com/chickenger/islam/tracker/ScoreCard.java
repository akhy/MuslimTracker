package com.chickenger.islam.tracker;

import com.chickenger.islam.tracker.bean.DateString;
import com.chickenger.islam.tracker.bean.Prayer;
import com.chickenger.islam.tracker.bean.Record;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScoreCard {

    private static final int GOOD = 1;
    private static final int BAD = 0;

    public static Map<Date, Integer> createDateScore(Map<DateString, Map<Prayer, Record>> records) {
        Map<Date, Integer> result = new HashMap<>();
        for (Map.Entry<DateString, Map<Prayer, Record>> entry : records.entrySet()) {
            Date date = entry.getKey().asDateTime().toDate();
            boolean good = true;
            for (Record record : entry.getValue().values()) {
                if (!record.getStatus().isDone()) {
                    good = false;
                    break;
                }
            }
            result.put(date, good ? GOOD : BAD);
        }

        return result;
    }

    public static Map<Date, Integer> createScoreResId(Map<Date, Integer> scores) {
        Map<Date, Integer> result = new HashMap<>();
        for (Map.Entry<Date, Integer> score : scores.entrySet()) {
            if (score.getValue() == GOOD) {
                result.put(score.getKey(), R.drawable.score_good_bg);
            } else {
                result.put(score.getKey(), R.drawable.score_bad_bg);
            }
        }

        return result;
    }

}
