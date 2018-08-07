package com.taleckij_anton.apro_task_taleckij;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Car;

public class CarInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);

        final Toolbar carInfoToolbar = findViewById(R.id.car_info_toolbar);
        carInfoToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent, null));
        setSupportActionBar(carInfoToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Car currentCar = Car.fromIntent(getIntent());

        fillCarDataView(currentCar);

//        setTitleColor(getResources().getColor(R.color.colorAccent, null));
//        setTitle(String.valueOf(currentCar.getPrice()));
    }

    private void fillCarDataView(Car car) {
        final ImageView imageView = findViewById(R.id.car_info_photo);
        imageView.setImageBitmap(car.getPhoto().getBitmapPhoto());

        final TextView priceView = findViewById(R.id.car_price);
        priceView.setText(String.valueOf(car.getPrice()));
        final TextView brandView = findViewById(R.id.car_brand);
        brandView.setText(car.getBrand().getName());
        final TextView modelView = findViewById(R.id.car_model);
        modelView.setText(car.getModel().getName());

        final TextView mileageView = findViewById(R.id.car_mileage);
        mileageView.setText(String.valueOf(car.getMileage()));
        final TextView manufactureYearView = findViewById(R.id.car_manufacture_year);
        manufactureYearView.setText(String.valueOf(car.getManufactureYear()));
        final TextView engineVolumeView = findViewById(R.id.car_engine_volume);
        engineVolumeView.setText(String.valueOf(car.getEngineVolume()));
        final TextView fuelTankVolumeView = findViewById(R.id.car_fuel_tank_volume);
        fuelTankVolumeView.setText(String.valueOf(car.getFuelTankVolume()));
        final TextView maxSpeedView = findViewById(R.id.car_max_speed);
        maxSpeedView.setText(String.valueOf(car.getMaxSpeed()));
        final TextView weightView = findViewById(R.id.car_weight);
        weightView.setText(String.valueOf(car.getWeight()));
    }
}
