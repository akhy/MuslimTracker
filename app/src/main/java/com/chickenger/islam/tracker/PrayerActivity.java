package com.chickenger.islam.tracker;

import android.os.Bundle;
import android.widget.TextView;

import com.chickenger.islam.tracker.bean.Prayer;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class PrayerActivity extends ToolbarActivity {

    @Bind(R.id.name) TextView name;
    @Bind(R.id.description) TextView description;

    @Extra Prayer prayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);
        ButterKnife.bind(this);
        PrayerActivityIntentBuilder.inject(getIntent(), this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name.setText(prayer.getLabelResId());
    }

}
