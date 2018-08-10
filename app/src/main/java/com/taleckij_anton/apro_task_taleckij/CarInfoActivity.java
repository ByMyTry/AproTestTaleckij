package com.taleckij_anton.apro_task_taleckij;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taleckij_anton.apro_task_taleckij.cars_db.CarsDbHelper;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Car;

public class CarInfoActivity extends AppCompatActivity {

    private static final int EDIT_CAR_ACTION_CODE = 1;

    private Car mCurrentCar;

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

        mCurrentCar = Car.fromIntent(getIntent());

        fillCarDataView(mCurrentCar);

        final FloatingActionButton changeCarInfoFab = findViewById(R.id.fab_change_car_info);
        changeCarInfoFab.setOnClickListener(getFabOnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CAR_ACTION_CODE && resultCode == RESULT_OK)
        {
            CarsDbHelper carsDbHelper = new CarsDbHelper(this);
            Long currentId = mCurrentCar.getId();
            if(currentId != null) {
                Car changedCar = Car.fromIntent(data);
                carsDbHelper.updateCar(currentId, changedCar);

                changedCar.setId(currentId);
                mCurrentCar = changedCar;
                fillCarDataView(mCurrentCar);
            }
        }
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

    private View.OnClickListener getFabOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar =  Snackbar.make(v, "Редактировать машину?", Snackbar.LENGTH_LONG)
                        .setAction("Да", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent carEditIntent =
                                        new Intent(v.getContext(), CarEditActivity.class);
                                carEditIntent = mCurrentCar.toIntent(carEditIntent);
                                carEditIntent.putExtra(CarEditActivity.CAR_EDIT_FLAG, true);
                                startActivityForResult(carEditIntent, EDIT_CAR_ACTION_CODE);
                            }
                        });
                snackbar.show();
            }
        };
    }
}
