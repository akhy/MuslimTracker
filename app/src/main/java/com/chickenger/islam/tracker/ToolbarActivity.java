package com.chickenger.islam.tracker;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

public class ToolbarActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initToolbar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            throw new RuntimeException("Layout has no @+id/toolbar view.");
        }

        setSupportActionBar(toolbar);
        onToolbarSet(toolbar);
    }

    protected void onToolbarSet(Toolbar toolbar) {
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public ActionBar getSupportActionBar() {
        return super.getSupportActionBar();
    }
}
