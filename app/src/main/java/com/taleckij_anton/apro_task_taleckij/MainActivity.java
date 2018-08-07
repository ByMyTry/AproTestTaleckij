package com.taleckij_anton.apro_task_taleckij;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Car;
import com.taleckij_anton.apro_task_taleckij.cars_db.CarsDbHelper;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Photo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int ADD_CAR_ACTION_CODE = 0;

    private CarsDbHelper mCarsDbHelper;

    private boolean isFirstLaunch(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getBoolean(getResources().getString(R.string.first_launch_key), false);
    }

    private void setFirstLaunchFalse(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit()
                .putBoolean(getResources().getString(R.string.first_launch_key), false)
                .apply();
    }

    private void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        final Toolbar carsToolbar = findViewById(R.id.cars_toolbar);
        setSupportActionBar(carsToolbar);

        mCarsDbHelper = new CarsDbHelper(this);
        if(isFirstLaunch()){
            showToastMessage("DataBase initialization");
            testInitDb(mCarsDbHelper);
            setFirstLaunchFalse();
        }
        final ArrayList<Car> cars = mCarsDbHelper.readCars();

        final RecyclerView autosRv = findViewById(R.id.cars_rv);
        autosRv.setLayoutManager(new LinearLayoutManager(this));
        autosRv.setAdapter(new CarsRvAdapter(cars));

        final FloatingActionButton addAutoFab = findViewById(R.id.fab_add_car);
        addAutoFab.setOnClickListener(getFabOnClickListener());

        Stetho.initializeWithDefaults(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cars_tools_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ADD_CAR_ACTION_CODE && resultCode == RESULT_OK)
        {
            mCarsDbHelper.insertCar(Car.fromIntent(intent));
            recreate();
        }
    }

//    private void restartActivity(){
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//    }

    private void testInitDb(CarsDbHelper carsDbHelper){
        carsDbHelper.clearData();
        carsDbHelper.insertBrand("China№One");
        carsDbHelper.insertBrand("RussianMans");
        carsDbHelper.insertModel("BMW");
        carsDbHelper.insertModel("Audi");

        Bitmap iconBmw = ((BitmapDrawable) getDrawable(R.drawable.bmw)).getBitmap();
        Photo photoBmw = new Photo(iconBmw, null);
        photoBmw.setBitmapPhoto(photoBmw.getBitmapPhoto());

        Bitmap iconAudi = ((BitmapDrawable) getDrawable(R.drawable.audi)).getBitmap();
        Photo photoAudi = new Photo(iconAudi, null);
        photoAudi.setBitmapPhoto(photoAudi.getBitmapPhoto());

        carsDbHelper.insertCar(new Car((float)210.21, 1990,300, 4,
                12, 200, 2000, photoAudi),
                1, 2);
        carsDbHelper.insertCar(new Car((float)121.21, 1990,300, 4,
                12, 200, 2000, photoBmw),
                2, 1);
        carsDbHelper.insertCar(new Car((float)211.21, 1990,300, 4,
                12, 200, 2000, photoBmw),
                1, 1);
        carsDbHelper.insertCar(new Car((float)210.21, 1990,300, 4,
                        12, 200, 2000, photoAudi),
                1, 2);
        carsDbHelper.insertCar(new Car((float)121.23, 1990,300, 4,
                        12, 200, 2000, photoBmw),
                2, 1);
        carsDbHelper.insertCar(new Car((float)211.24, 1990,300, 4,
                        12, 200, 2000, photoBmw),
                1, 1);
        carsDbHelper.insertCar(new Car((float)210.25, 1990,300, 4,
                        12, 200, 2000, photoAudi),
                1, 2);
        carsDbHelper.insertCar(new Car((float)121.26, 1990,300, 4,
                        12, 200, 2000, photoBmw),
                2, 1);
        carsDbHelper.insertCar(new Car((float)211.27, 1990,300, 4,
                        12, 200, 2000, photoBmw),
                1, 1);
        carsDbHelper.insertCar(new Car((float)210.28, 1990,300, 4,
                        12, 200, 2000, photoAudi),
                1, 2);
        carsDbHelper.insertCar(new Car((float)121.29, 1990,300, 4,
                        12, 200, 2000, photoBmw),
                2, 1);
        carsDbHelper.insertCar(new Car((float)211.31, 1990,300, 4,
                        12, 200, 2000, photoBmw),
                1, 1);
    }

    private View.OnClickListener getFabOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar =  Snackbar.make(v, "Добавить машину?", Snackbar.LENGTH_LONG)
                        .setAction("Да", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(pickPhoto , ADD_CAR_ACTION_CODE);
                                Intent intent = new Intent(
                                        MainActivity.this, CarEditActivity.class);
                                startActivityForResult(intent , ADD_CAR_ACTION_CODE);
                            }
                        });
                snackbar.show();
            }
        };
    }
}
