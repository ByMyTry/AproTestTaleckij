package com.taleckij_anton.apro_task_taleckij;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.taleckij_anton.apro_task_taleckij.R;
import com.taleckij_anton.apro_task_taleckij.cars_db.CarsDbHelper;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Brand;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Car;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Model;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Photo;

import java.io.IOException;
import java.util.ArrayList;

public class CarEditActivity extends AppCompatActivity {

    private int CHOOSE_PHOTO_ACTION_CODE = 0;

    private ImageView mPhotoView;
    private Bitmap mPhotoBitmap;
//    private Spinner mBrandsSpinner;
//    private Spinner mModelsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);

        final Toolbar carInfoToolbar = findViewById(R.id.car_edit_toolbar);
        carInfoToolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent, null));
        setSupportActionBar(carInfoToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPhotoView = findViewById(R.id.car_edit_photo);
        mPhotoView.setOnClickListener(getPhotoOnClickListener());

        final FloatingActionButton fabSaveEdit = findViewById(R.id.fab_save_car_edit);
        fabSaveEdit.setOnClickListener(getFabOnClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == CHOOSE_PHOTO_ACTION_CODE && resultCode == RESULT_OK)
        {
            Uri imageUri = intent.getData();
            try {
                mPhotoBitmap = BitmapHelper.getThumbnail(this, imageUri);
                mPhotoView.setImageBitmap(mPhotoBitmap);
//                Photo photo = new Photo(bitmap, null);
//                photo.setBitmapPhoto(photo.getBitmapPhoto());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSpinner(Spinner spinner, ArrayList<String> lables){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private View.OnClickListener getPhotoOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , CHOOSE_PHOTO_ACTION_CODE);
            }
        };
    }

    private View.OnClickListener getFabOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar =  Snackbar.make(v, "Сохранить машину?", Snackbar.LENGTH_LONG)
                        .setAction("Да", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(pickPhoto , ADD_CAR_ACTION_CODE);
                                Car car = makeCar();
                                setResult(RESULT_OK, car.toIntent(new Intent()));
                                finish();
                            }
                        });
                snackbar.show();
            }
        };
    }

    private Car makeCar(){
        final EditText priceView = findViewById(R.id.car_price);
        float price = Float.valueOf(priceView.getText().toString());

        final EditText brandView = findViewById(R.id.car_brand);
        String brandName = brandView.getText().toString();
        final EditText modelView = findViewById(R.id.car_model);
        String modelName = modelView.getText().toString();

        final EditText mileageView = findViewById(R.id.car_mileage);
        int mileage = Integer.valueOf(mileageView.getText().toString());
        final EditText manufactureYearView = findViewById(R.id.car_manufacture_year);
        int manufactureYear = Integer.valueOf(manufactureYearView.getText().toString());
        final EditText engineVolumeView = findViewById(R.id.car_engine_volume);
        int engineVolume = Integer.valueOf(engineVolumeView.getText().toString());
        final EditText fuelTankVolumeView = findViewById(R.id.car_fuel_tank_volume);
        int fuelTankVolume = Integer.valueOf(fuelTankVolumeView.getText().toString());
        final EditText maxSpeedView = findViewById(R.id.car_max_speed);
        int maxSpeed = Integer.valueOf(maxSpeedView.getText().toString());
        final EditText weightView = findViewById(R.id.car_weight);
        int weight = Integer.valueOf(weightView.getText().toString());

        return new Car(price, manufactureYear, mileage, engineVolume,
                fuelTankVolume, maxSpeed, weight, new Photo(mPhotoBitmap, null),
                new Brand(brandName), new Model(modelName));
    }
}
