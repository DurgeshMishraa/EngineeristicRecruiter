package com.engineeristic.recruiter.myapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import com.engineeristic.recruiter.R;
public class SlideHeaderScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.slide_screen_layout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("User Profile");
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.WhiteColor));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.WhiteColor));

    }
}
