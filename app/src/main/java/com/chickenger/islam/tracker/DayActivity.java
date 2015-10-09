package com.chickenger.islam.tracker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chickenger.islam.tracker.bean.DateString;
import com.chickenger.islam.tracker.bean.Place;
import com.chickenger.islam.tracker.bean.Prayer;
import com.chickenger.islam.tracker.bean.Status;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DayActivity extends ToolbarActivity implements PrayerDialog.Callback {

    public static final String EXTRA_DATE_TIME = "date_time";

    @Bind(R.id.day_name) TextView dayName;
    @Bind(R.id.date) TextView date;
    @Bind(R.id.btn_prev_day) ImageButton btnPrevDay;
    @Bind(R.id.btn_next_day) ImageButton btnNextDay;
    @Bind(R.id.prayer_fajr) PrayerView prayerFajr;
    @Bind(R.id.prayer_zuhr) PrayerView prayerZuhr;
    @Bind(R.id.prayer_asr) PrayerView prayerAsr;
    @Bind(R.id.prayer_maghrib) PrayerView prayerMaghrib;
    @Bind(R.id.prayer_isha) PrayerView prayerIsha;
    @Bind(R.id.scroll_view) ObservableScrollView scrollView;

    private DateTime currentDateTime;
    private UserRecords userRecords;
    private float maxScrollOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);

        maxScrollOffset = getResources().getDimension(R.dimen.header_height);
        maxScrollOffset -= getToolbar().getHeight();
        maxScrollOffset -= getStatusBarHeight();
        scrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                updateToolbarAlpha(Math.max(0f, Math.min(scrollY / maxScrollOffset, 1f)));
            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });
        updateToolbarAlpha(0f);

        userRecords = new UserRecords(this);

        onNewIntent(getIntent());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void onToolbarSet(Toolbar toolbar) {
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    private void updateToolbarAlpha(float alpha) {
        getToolbar().getBackground().setAlpha((int) (alpha * 255));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        DateTime dateTime = intent.hasExtra(EXTRA_DATE_TIME)
            ? (DateTime) intent.getSerializableExtra(EXTRA_DATE_TIME)
            : DateTime.now();

        setCurrentDateTime(dateTime);
        loadRecords(dateTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:
                startActivity(new CalendarActivityIntentBuilder()
                    .defDateTime(currentDateTime)
                    .build(this));
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadRecords(DateTime dateTime) {
        DateString dateString = DateString.of(dateTime);

        // TODO refactor this
        prayerFajr.setStatus(userRecords.getStatus(dateString, Prayer.FAJR));
        prayerZuhr.setStatus(userRecords.getStatus(dateString, Prayer.ZUHR));
        prayerAsr.setStatus(userRecords.getStatus(dateString, Prayer.ASR));
        prayerMaghrib.setStatus(userRecords.getStatus(dateString, Prayer.MAGHRIB));
        prayerIsha.setStatus(userRecords.getStatus(dateString, Prayer.ISHA));

        prayerFajr.setPlace(userRecords.getPlace(dateString, Prayer.FAJR));
        prayerZuhr.setPlace(userRecords.getPlace(dateString, Prayer.ZUHR));
        prayerAsr.setPlace(userRecords.getPlace(dateString, Prayer.ASR));
        prayerMaghrib.setPlace(userRecords.getPlace(dateString, Prayer.MAGHRIB));
        prayerIsha.setPlace(userRecords.getPlace(dateString, Prayer.ISHA));
    }

    @OnClick({
        R.id.prayer_fajr,
        R.id.prayer_zuhr,
        R.id.prayer_asr,
        R.id.prayer_maghrib,
        R.id.prayer_isha
    })
    public void showStatusDialog(final PrayerView prayerView) {
        PrayerDialog.newInstance(DateString.of(currentDateTime), prayerView)
            .show(getSupportFragmentManager(), "prayer-dialog");
    }

    @Override
    public void updatePrayerRecord(DateString date, Prayer prayer, Status status, Place place) {
        if (!DateString.of(currentDateTime).equals(date)) {
            return;
        }

        getPrayerView(prayer).setStatus(status);
        getPrayerView(prayer).setPlace(place);
        userRecords.setStatus(date, prayer, status);
        userRecords.setPlace(date, prayer, place);
    }

    private PrayerView getPrayerView(Prayer prayer) {
        if (Prayer.FAJR.equals(prayer)) {
            return prayerFajr;
        } else if (Prayer.ZUHR.equals(prayer)) {
            return prayerZuhr;
        } else if (Prayer.ASR.equals(prayer)) {
            return prayerAsr;
        } else if (Prayer.MAGHRIB.equals(prayer)) {
            return prayerMaghrib;
        } else if (Prayer.ISHA.equals(prayer)) {
            return prayerIsha;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void showPrayerDetail(Prayer prayer) {
        startActivity(new PrayerActivityIntentBuilder(prayer).build(this));
    }

    @OnClick(R.id.btn_prev_day)
    public void prevDay() {
        setCurrentDateTime(currentDateTime.minusDays(1));
    }

    @OnClick(R.id.btn_next_day)
    public void nextDay() {
        setCurrentDateTime(currentDateTime.plusDays(1));
    }

    public void setCurrentDateTime(DateTime currentDateTime) {
        this.currentDateTime = currentDateTime;

        loadRecords(currentDateTime);
        dayName.setText(currentDateTime.toString("EEEE"));
        date.setText(currentDateTime.toString(DateTimeFormat.mediumDate()));

        boolean isToday = DateString.of(currentDateTime).equals(DateString.today());
        btnNextDay.setAlpha(isToday ? 0.3f : 1.0f);
        btnNextDay.setEnabled(!isToday);
    }


}
