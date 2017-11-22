package com.engineeristic.recruiter.screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by d on 4/25/2017.
 */

public class BaseActionBarActivity extends AppCompatActivity {
    protected static final int FRAGMENT_ID_HOME = 0x65;
    protected static final int FRAGMENT_ID_USER_PROFILE = 0x66;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
