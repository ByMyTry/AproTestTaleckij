package com.taleckij_anton.apro_task_taleckij;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.taleckij_anton.apro_task_taleckij.cars_db.CarsDbHelper;

import java.util.ArrayList;

public class CarsListSettingActivity extends AppCompatActivity {

//    public static final String FILTER_BY_BRAND_KEY = "FILTER_BY_BRAND_KEY";
//    public static final String FILTER_BY_MODEL_KEY = "FILTER_BY_MODEL_KEY";
//
//    public static final String SORT_PRICE_KEY = "SORT_KEY";
    public static final String SORT_WITHOUT = "";
    public static final String SORT_PRICE_ASC = "SORT_ASC";
    public static final String SORT_PRICE_DESC = "SORT_DESC";

    private Spinner mBrandsSpinner;
    private Spinner mModelsSpinner;

    private RadioButton mWithoutSortRadio;
    private RadioButton mPriceAscSortRadio;
    private RadioButton mPriceDescSortRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list_setting);

        final CarsDbHelper carsDbHelper = new CarsDbHelper(this);

        mBrandsSpinner = findViewById(R.id.brands_filter_spinner);
        ArrayList<String> brandsNames = carsDbHelper.readBrandsNames();
        loadSpinner(mBrandsSpinner, brandsNames);
        mModelsSpinner = findViewById(R.id.models_filter_spinner);
        ArrayList<String> modelsNames = carsDbHelper.readModelsNames();
        loadSpinner(mModelsSpinner, modelsNames);

        mWithoutSortRadio = findViewById(R.id.radio_without_sort);
        mWithoutSortRadio.setChecked(true);
        mPriceAscSortRadio = findViewById(R.id.radio_price_asc_sort);
        mPriceDescSortRadio = findViewById(R.id.radio_price_desc_sort);

        loadSettingsFromPreferences(brandsNames, modelsNames);

        final Button carsListSettingsButton = findViewById(R.id.car_list_settings_button);
        carsListSettingsButton.setOnClickListener(getSettingsBtnListener());
    }


    private void loadSpinner(Spinner spinner, ArrayList<String> labels){
        labels.add(0, "");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

//    private Intent getSettingIntent(){
//        Intent intent = new Intent();
//        intent.putExtra(FILTER_BY_BRAND_KEY, mBrandsSpinner.getSelectedItem().toString());
//        intent.putExtra(FILTER_BY_MODEL_KEY, mModelsSpinner.getSelectedItem().toString());
//        if(mWithoutSortRadio.isChecked()){
//        } else if(mPriceAscSortRadio.isChecked()){
//            intent.putExtra(SORT_PRICE_KEY, SORT_PRICE_ASC);
//        } else {
//            intent.putExtra(SORT_PRICE_KEY, SORT_PRICE_DESC);
//        }
//        return intent;
//    }

    private void loadSettingsFromPreferences(ArrayList<String> brandsNames,
                                             ArrayList<String> modelsNames){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        final String filterBrand =
                sp.getString(getResources().getString(R.string.filter_brand_key), "");
        mBrandsSpinner.setSelection(brandsNames.indexOf(filterBrand));
        final String filterModel =
                sp.getString(getResources().getString(R.string.filter_model_key), "");
        mModelsSpinner.setSelection(modelsNames.indexOf(filterModel));

        final String sortType =
            sp.getString(getResources().getString(R.string.sort_price_key), "");
        if (sortType.isEmpty()) {
            mWithoutSortRadio.setChecked(true);
        } else if (sortType.equals(CarsListSettingActivity.SORT_PRICE_ASC)) {
            mPriceAscSortRadio.setChecked(true);
        } else if (sortType.equals(CarsListSettingActivity.SORT_PRICE_DESC)){
            mPriceDescSortRadio.setChecked(true);
        }
    }

    private void saveSettingsToPreferences(){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit()
                .putString(getResources().getString(R.string.filter_brand_key),
                        mBrandsSpinner.getSelectedItem().toString())
                .apply();
        sp.edit()
                .putString(getResources().getString(R.string.filter_model_key),
                        mModelsSpinner.getSelectedItem().toString())
                .apply();
        if(mWithoutSortRadio.isChecked()){
            sp.edit()
                    .putString(getResources().getString(R.string.sort_price_key), SORT_WITHOUT)
                    .apply();
        } else if(mPriceAscSortRadio.isChecked()){
            sp.edit()
                .putString(getResources().getString(R.string.sort_price_key), SORT_PRICE_ASC)
                .apply();
        } else if(mPriceDescSortRadio.isChecked()) {
            sp.edit()
                .putString(getResources().getString(R.string.sort_price_key), SORT_PRICE_DESC)
                .apply();
        }
    }

    private View.OnClickListener getSettingsBtnListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent carsListSettingsIntent = getSettingIntent();
//                setResult(RESULT_OK, carsListSettingsIntent);
                saveSettingsToPreferences();
                finish();
            }
        };
    }
}
