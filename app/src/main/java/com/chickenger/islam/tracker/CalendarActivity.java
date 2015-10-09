package com.chickenger.islam.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class CalendarActivity extends ToolbarActivity {

    @Extra
    @Nullable
    DateTime defDateTime;

    private UserRecords userRecords;
    private CaldroidFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_with_toolbar);
        ButterKnife.bind(this);
        CalendarActivityIntentBuilder.inject(getIntent(), this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userRecords = new UserRecords(this);
        DateTime today = DateTime.now();
        if (defDateTime == null) {
            defDateTime = today;
        }

        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, defDateTime.getMonthOfYear());
        args.putInt(CaldroidFragment.YEAR, defDateTime.getYear());

        fragment = new CaldroidFragment();
        fragment.setArguments(args);
        fragment.setMaxDate(today.toDate());
        fragment.setSelectedDate(defDateTime.toDate());
        fragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                onDateSelected(date);
            }
        });

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content, fragment)
            .commit();

        loadPrayerRecords();
    }

    private void loadPrayerRecords() {
        Observable
            .defer(new Func0<Observable<Map<Date, Integer>>>() {
                @Override
                public Observable<Map<Date, Integer>> call() {
                    Map<Date, Integer> resIds = ScoreCard.createScoreResId(
                        ScoreCard.createDateScore(userRecords.getRecords()));
                    return Observable.just(resIds);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Map<Date, Integer>>() {
                @Override
                public void call(Map<Date, Integer> dateIntegerMap) {
                    fragment.setBackgroundResourceForDates(new HashMap<>(dateIntegerMap));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDateSelected(Date date) {
        Intent intent = new Intent(this, DayActivity.class);
        intent.putExtra(DayActivity.EXTRA_DATE_TIME, new DateTime(date));
        startActivity(intent);
    }

}
