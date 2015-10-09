package com.chickenger.islam.tracker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chickenger.islam.tracker.bean.DateString;
import com.chickenger.islam.tracker.bean.Place;
import com.chickenger.islam.tracker.bean.Prayer;
import com.chickenger.islam.tracker.bean.Status;

import org.joda.time.format.DateTimeFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class PrayerDialog extends DialogFragment {


    @Bind(R.id.prayer_name) TextView prayerName;
    @Bind(R.id.timestamp) TextView timestamp;
    @Bind(R.id.info_button) ImageButton infoButton;
    @Bind(R.id.status_group) RadioGroup statusGroup;
    @Bind(R.id.place_group) RadioGroup placeGroup;
    @Bind(R.id.cancel_button) Button cancelButton;
    @Bind(R.id.ok_button) Button okButton;
    @Bind(R.id.radio_status_ontime) RadioButton radioStatusOntime;
    @Bind(R.id.radio_status_late) RadioButton radioStatusLate;
    @Bind(R.id.radio_status_missed) RadioButton radioStatusMissed;
    @Bind(R.id.radio_place_mosque) RadioButton radioPlaceMosque;
    @Bind(R.id.radio_place_home) RadioButton radioPlaceHome;
    @Bind(R.id.radio_place_office) RadioButton radioPlaceOffice;
    @Bind(R.id.radio_place_other) RadioButton radioPlaceOther;

    @Nullable private Callback callback;

    public static PrayerDialog newInstance(DateString date, PrayerView prayerView) {
        return newInstance(date, prayerView.getPrayer(), prayerView.getStatus(), prayerView.getPlace());
    }

    public static PrayerDialog newInstance(DateString date, Prayer prayer, Status status, Place place) {
        Bundle args = new Bundle();
        args.putParcelable("date", date);
        args.putParcelable("prayer", prayer);
        args.putParcelable("status", status);
        args.putParcelable("place", place);

        PrayerDialog dialog = new PrayerDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_prayer, container, true);
        ButterKnife.bind(this, view);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Status status = getArguments().getParcelable("status");
        Place place = getArguments().getParcelable("place");

        initHeader();
        initStatus(status);
        initPlace(place);
        updateDisabledViews();

        // FIXME remove later
        infoButton.setVisibility(View.GONE);

        statusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateDisabledViews();
            }
        });
        placeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateDisabledViews();
            }
        });
    }

    private void initHeader() {
        prayerName.setText(getPrayer().getLabelResId());
        timestamp.setText(getDate().asDateTime().toString(DateTimeFormat.fullDate()));
    }

    private void initStatus(Status status) {
        if (Status.ONTIME.equals(status)) {
            radioStatusOntime.setChecked(true);
        } else if (Status.LATE.equals(status)) {
            radioStatusLate.setChecked(true);
        } else if (Status.MISSED.equals(status)) {
            radioStatusMissed.setChecked(true);
        }
    }

    private void initPlace(Place place) {
        if (Place.MOSQUE.equals(place)) {
            radioPlaceMosque.setChecked(true);
        } else if (Place.HOME.equals(place)) {
            radioPlaceHome.setChecked(true);
        } else if (Place.OFFICE.equals(place)) {
            radioPlaceOffice.setChecked(true);
        } else if (Place.OTHER.equals(place)) {
            radioPlaceOther.setChecked(true);
        }
    }

    public void updateDisabledViews() {
        boolean isDone = getStatus() != null && getStatus().isDone();
        View child;
        for (int i = 0; i < placeGroup.getChildCount(); i++) {
            child = placeGroup.getChildAt(i);
            child.setAlpha(isDone ? 1f : 0.4f);
            child.setEnabled(isDone);
        }
        boolean isValid = (getStatus() != null && getPlace() != null)
            || (getStatus() != null && !getStatus().isDone());
        okButton.setEnabled(isValid);
    }

    @OnClick(R.id.info_button)
    public void showInfo() {
        if (callback != null) {
            callback.showPrayerDetail(getPrayer());
        }
    }

    @OnClick(R.id.ok_button)
    public void finish() {
        if (callback != null && getStatus() != null && getPlace() != null) {
            callback.updatePrayerRecord(getDate(), getPrayer(), getStatus(), getPlace());
        }

        dismiss();
    }

    @Override
    @OnClick(R.id.cancel_button)
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            callback = (Callback) activity;
        } else {
            Timber.w(activity.getClass() + " is not implementing " + Callback.class);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public DateString getDate() {
        return getArguments().getParcelable("date");
    }

    public Prayer getPrayer() {
        return getArguments().getParcelable("prayer");
    }

    @Nullable
    public Status getStatus() {
        switch (statusGroup.getCheckedRadioButtonId()) {
            case R.id.radio_status_ontime:
                return Status.ONTIME;
            case R.id.radio_status_late:
                return Status.LATE;
            case R.id.radio_status_missed:
                return Status.MISSED;
            default:
                return null;
        }
    }

    @Nullable
    public Place getPlace() {
        switch (placeGroup.getCheckedRadioButtonId()) {
            case R.id.radio_place_mosque:
                return Place.MOSQUE;
            case R.id.radio_place_home:
                return Place.HOME;
            case R.id.radio_place_office:
                return Place.OFFICE;
            case R.id.radio_place_other:
                return Place.OTHER;
            default:
                return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface Callback {
        void updatePrayerRecord(DateString date, Prayer prayer, Status status, Place place);

        void showPrayerDetail(Prayer prayer);
    }
}
