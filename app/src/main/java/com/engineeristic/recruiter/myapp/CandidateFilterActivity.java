package com.engineeristic.recruiter.myapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.filter.BatchFilterFragment;
import com.engineeristic.recruiter.filter.ExperienceFilterFragment;
import com.engineeristic.recruiter.filter.GenderFilterFragment;
import com.engineeristic.recruiter.filter.MultiSelectionFilterFragment;
import com.engineeristic.recruiter.filter.NoticePeriodFilterFragment;
import com.engineeristic.recruiter.filter.SalaryFilterFragment;
import com.engineeristic.recruiter.model.FilterModel;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import java.util.HashMap;

public class CandidateFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout industryLayout, functionLayout, experienceLayout,
            instituteLayout, locationLayout, batchLayout, salaryLayout, genderLayout, noticePeriodTextLayout;
    private ImageView budge01, budge02, budge03, budge04, budge05, budge06, budge07, budge08, budge09;
    private ImageView industryImgView, functionalImageView, experienceImageView,
            instituteImageView, locationImageView, batchImageView,
            salaryImageView, genderImageView, noticePeriodTextImageView;
    private TextView industryText, functionText, experienceText,
            instituteText, locationText, batchText, salaryText, genderText, noticePeriodText;
    private TextView pBackTextView, applyTextView, resetTextView;
    private boolean isResetPressed = false;
    private TextView titleText;
    private Typeface typeRobotoMedium, typeRobotoRegular, typeFontello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.candidate_filter_screen);
        typeFontello = Typeface.createFromAsset(getAssets(), "fontello.ttf");
        typeRobotoRegular = Typeface.createFromAsset(getAssets(),"robotoregular.ttf");
        typeRobotoMedium = Typeface.createFromAsset(getAssets(),"robotomedium.ttf");
        RecruiterApplication application = (RecruiterApplication) getApplication();
        AccountUtils.trackerScreen("Filter");

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        titleText = (TextView)findViewById(R.id.ttl_text);
        titleText.setTypeface(typeRobotoMedium);
        init();
        industryLayout.performClick();
        isResetPressed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        applyTextView = (TextView) findViewById(R.id.apply_cmd);
        pBackTextView = (TextView) findViewById(R.id.back_cmd);
        resetTextView = (TextView) findViewById(R.id.reset_cmd);
        resetTextView.setTypeface(typeRobotoRegular);
        applyTextView.setTypeface(typeRobotoRegular);
        industryLayout = (RelativeLayout) findViewById(R.id.tab_item01);
        functionLayout = (RelativeLayout) findViewById(R.id.tab_item02);
        experienceLayout = (RelativeLayout) findViewById(R.id.tab_item03);
        instituteLayout = (RelativeLayout) findViewById(R.id.tab_item04);
        locationLayout = (RelativeLayout) findViewById(R.id.tab_item05);
        batchLayout = (RelativeLayout) findViewById(R.id.tab_item06);
        salaryLayout = (RelativeLayout) findViewById(R.id.tab_item07);
        genderLayout = (RelativeLayout) findViewById(R.id.tab_item08);
        noticePeriodTextLayout = (RelativeLayout) findViewById(R.id.tab_item09);

        budge01 = (ImageView) findViewById(R.id.budge01);
        budge02 = (ImageView) findViewById(R.id.budge02);
        budge03 = (ImageView) findViewById(R.id.budge03);
        budge04 = (ImageView) findViewById(R.id.budge04);
        budge05 = (ImageView) findViewById(R.id.budge05);
        budge06 = (ImageView) findViewById(R.id.budge06);
        budge07 = (ImageView) findViewById(R.id.budge07);
        budge08 = (ImageView) findViewById(R.id.budge08);
        budge09 = (ImageView) findViewById(R.id.budge09);

        industryImgView = (ImageView) findViewById(R.id.imageview01);
        functionalImageView = (ImageView) findViewById(R.id.imageview02);
        experienceImageView = (ImageView) findViewById(R.id.imageview03);
        instituteImageView = (ImageView) findViewById(R.id.imageview04);
        locationImageView = (ImageView) findViewById(R.id.imageview05);
        batchImageView = (ImageView) findViewById(R.id.imageview06);
        salaryImageView = (ImageView) findViewById(R.id.imageview07);
        genderImageView = (ImageView) findViewById(R.id.imageview08);
        noticePeriodTextImageView = (ImageView) findViewById(R.id.imageview09);

        industryText = (TextView) findViewById(R.id.textView01);
        functionText = (TextView) findViewById(R.id.textView02);
        experienceText = (TextView) findViewById(R.id.textView03);
        instituteText = (TextView) findViewById(R.id.textView04);
        locationText = (TextView) findViewById(R.id.textView05);
        batchText = (TextView) findViewById(R.id.textView06);
        salaryText = (TextView) findViewById(R.id.textView07);
        genderText = (TextView) findViewById(R.id.textView08);
        noticePeriodText = (TextView) findViewById(R.id.textView09);

        pBackTextView.setTypeface(typeFontello);
        pBackTextView.setText(ConstantFontelloID.icon_back_icon);
        applyTextView.setOnClickListener(this);
        pBackTextView.setOnClickListener(this);
        resetTextView.setOnClickListener(this);
        industryLayout.setOnClickListener(this);
        functionLayout.setOnClickListener(this);
        experienceLayout.setOnClickListener(this);
        instituteLayout.setOnClickListener(this);
        locationLayout.setOnClickListener(this);
        batchLayout.setOnClickListener(this);
        salaryLayout.setOnClickListener(this);
        genderLayout.setOnClickListener(this);
        noticePeriodTextLayout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearFilter(false);
        if (isResetPressed) {
            setResult(1, getIntent());
        }
        finish();

    }

    private void clearFilter(boolean isPrefClear) {
        MultiSelectionFilterFragment.industryMap.clear();
        MultiSelectionFilterFragment.funcrtionalMap.clear();
        MultiSelectionFilterFragment.instituteMap.clear();
        MultiSelectionFilterFragment.locationMap.clear();
        NoticePeriodFilterFragment.clearHistory();
        ExperienceFilterFragment.clearHistory();
        SalaryFilterFragment.clearHistory();
        BatchFilterFragment.clearHistory();
        GenderFilterFragment.genderType = null;
        if (isPrefClear)
            MySharedPreference.clearFilterType();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_cmd:
                clearFilter(false);
                if (isResetPressed) {
                    setResult(1, getIntent());
                }
                finish();
                break;
            case R.id.reset_cmd:
                isResetPressed = true;
                clearFilter(true);
                industryLayout.performClick();
                Utility.showToastMessage(getApplicationContext(), getString(R.string.filter_removed_msg), Toast.LENGTH_SHORT);
                break;
            case R.id.apply_cmd:

			/*Log.d("+++++++++++++", "Functional Area = "+getSelectedItems(MultiSelectionFilterFragment.funcrtionalMap));
            Log.d("+++++++++++++", "Exp MAX = "+ExperienceFilterFragment.getMaxExp());
			Log.d("+++++++++++++", "Exp MIN = "+ExperienceFilterFragment.getMinExp());
			Log.d("+++++++++++++", "Institute = "+getSelectedItems(MultiSelectionFilterFragment.instituteMap));
			Log.d("+++++++++++++", "Location = "+getSelectedItems(MultiSelectionFilterFragment.locationMap));
			Log.d("+++++++++++++", "Batch MAX = "+BatchFilterFragment.getMaxBatch());
			Log.d("+++++++++++++", "Batch MIN = "+BatchFilterFragment.getMinBatch());
			Log.d("+++++++++++++", "Salary MAX = "+SalaryFilterFragment.getMaxSalary());
			Log.d("+++++++++++++", "Salary MIN = "+SalaryFilterFragment.getMinSalary());
			Log.d("+++++++++++++", "Gender Type = "+GenderFilterFragment.genderType);
			Log.d("+++++++++++++", "Notice period = "+NoticePeriodFilterFragment.getSelectedId());*/


                MySharedPreference.saveFilterType(Constant.FILTER_INDUSTRY_PARAMS, getSelectedItems(MultiSelectionFilterFragment.industryMap));
                MySharedPreference.saveFilterType(Constant.FILTER_FUNCTIONAL_PARAMS, getSelectedItems(MultiSelectionFilterFragment.funcrtionalMap));
                MySharedPreference.saveFilterType(Constant.FILTER_MIN_EXPERIENCE_PARAMS, ExperienceFilterFragment.getMinExp());
                MySharedPreference.saveFilterType(Constant.FILTER_MAX_EXPERIENCE_PARAMS, ExperienceFilterFragment.getMaxExp());
                MySharedPreference.saveFilterType(Constant.FILTER_INSTITUTE_PARAMS, getSelectedItems(MultiSelectionFilterFragment.instituteMap));
                MySharedPreference.saveFilterType(Constant.FILTER_LOCATION_PARAMS, getSelectedItems(MultiSelectionFilterFragment.locationMap));
                MySharedPreference.saveFilterType(Constant.FILTER_MIN_BATCH_PARAMS, BatchFilterFragment.getMinBatch());
                MySharedPreference.saveFilterType(Constant.FILTER_MAX_BATCH_PARAMS, BatchFilterFragment.getMaxBatch());
                MySharedPreference.saveFilterType(Constant.FILTER_MIN_SALARY_PARAMS, SalaryFilterFragment.getMinSalary());
                MySharedPreference.saveFilterType(Constant.FILTER_MAX_SALARY_PARAMS, SalaryFilterFragment.getMaxSalary());
                MySharedPreference.saveFilterType(Constant.FILTER_GENDER_PARAMS, GenderFilterFragment.genderType);
                MySharedPreference.saveFilterType(Constant.FILTER_NOTICE_PERIOD_PARAMS, NoticePeriodFilterFragment.getSelectedId());

                setResult(1, getIntent());
                finish();

                break;
            case R.id.tab_item01:
                budge01.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_green);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(industryText);
                changeFilterView(MultiSelectionFilterFragment.INDUSTRY_FILTER);
                break;
            case R.id.tab_item02:
                budge02.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_green);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(functionText);
                changeFilterView(MultiSelectionFilterFragment.FUNCTIONAL_AREA_FILTER);
                break;
            case R.id.tab_item03:
                budge03.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_green);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(experienceText);
                openFragment(new ExperienceFilterFragment());
                break;
            case R.id.tab_item04:
                budge04.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_green);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(instituteText);
                changeFilterView(MultiSelectionFilterFragment.INSTITUTE_FILTER);
                break;
            case R.id.tab_item05:
                budge05.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_green);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(locationText);
                changeFilterView(MultiSelectionFilterFragment.LOCATION_FILTER);
                break;
            case R.id.tab_item06:
                budge06.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_green);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(batchText);
                openFragment(new BatchFilterFragment());
                break;
            case R.id.tab_item07:
                budge07.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_green);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(salaryText);
                openFragment(new SalaryFilterFragment());
                break;
            case R.id.tab_item08:
                budge08.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_green);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_black);
                v.setTag(genderText);
                openFragment(new GenderFilterFragment());
                break;
            case R.id.tab_item09:
                budge09.setVisibility(View.GONE);
                industryImgView.setBackgroundResource(R.drawable.industry_black);
                functionalImageView.setBackgroundResource(R.drawable.functional_area_black);
                experienceImageView.setBackgroundResource(R.drawable.experience_black);
                instituteImageView.setBackgroundResource(R.drawable.institute_black);
                locationImageView.setBackgroundResource(R.drawable.location_black);
                batchImageView.setBackgroundResource(R.drawable.batch_black);
                salaryImageView.setBackgroundResource(R.drawable.salary_black);
                genderImageView.setBackgroundResource(R.drawable.gender_black);
                noticePeriodTextImageView.setBackgroundResource(R.drawable.notice_period_green);
                v.setTag(noticePeriodText);
                openFragment(new NoticePeriodFilterFragment());
                break;
        }
        tabBackgroundChange(v);
    }

    private String getSelectedItems(HashMap<String, FilterModel> hashMap) {
        String items = "";
        if (hashMap == null)
            return null;
        for (String key : hashMap.keySet()) {
            final FilterModel value = hashMap.get(key);
            items = items + value.getId() + ",";
        }
        if (!items.equals(""))
            items = items.substring(0, items.length() - 1);
        else {
            return null;
        }
        return items;
    }

    private void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void changeFilterView(final byte filterType) {
        MultiSelectionFilterFragment industryFragment = new MultiSelectionFilterFragment();
        Bundle args = new Bundle();
        args.putByte(MultiSelectionFilterFragment.KEY, filterType);
        industryFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, industryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openFragment(Fragment mFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void tabBackgroundChange(View view) {
        if (view instanceof RelativeLayout
                && view.getId() != R.id.back_cmd
                && view.getId() != R.id.reset_cmd) {
            industryLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            functionLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            experienceLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            instituteLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            locationLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            batchLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            salaryLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            genderLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            noticePeriodTextLayout.setBackgroundColor(getResources().getColor(R.color.filter_tab_unsel_color));
            view.setBackgroundColor(getResources().getColor(R.color.filter_tab_sel_color));
            Object object = view.getTag();
            if (object instanceof TextView) {
                industryText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                functionText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                experienceText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                instituteText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                locationText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                batchText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                salaryText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                genderText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                noticePeriodText.setTextColor(getResources().getColor(R.color.filter_text_gray_color));
                ((TextView) object).setTextColor(getResources().getColor(R.color.green_clr));
            }
        }
    }
}
