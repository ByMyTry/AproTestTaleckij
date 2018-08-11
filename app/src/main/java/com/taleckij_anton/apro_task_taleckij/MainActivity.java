package com.taleckij_anton.apro_task_taleckij;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Car;
import com.taleckij_anton.apro_task_taleckij.cars_db.CarsDbHelper;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Photo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private final static int ADD_CAR_ACTION_CODE = 0;
//    private final static int SETTING_ACTION_CODE = 1;

    private CarsDbHelper mCarsDbHelper;
    private RecyclerView mAutosRv;

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
            testInitDb(mCarsDbHelper);
            setFirstLaunchFalse();
            showToastMessage("DataBase initialization success");
        }
        final ArrayList<Car> cars = mCarsDbHelper.readCars();

        mAutosRv = findViewById(R.id.cars_rv);
        mAutosRv.setLayoutManager(new LinearLayoutManager(this));
        mAutosRv.setAdapter(new CarsRvAdapter(transformCars(cars)));

        final FloatingActionButton addAutoFab = findViewById(R.id.fab_add_car);
        addAutoFab.setOnClickListener(getFabOnClickListener());

        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ArrayList<Car> cars = mCarsDbHelper.readCars();
        mAutosRv.setAdapter(new CarsRvAdapter(transformCars(cars)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cars_tools_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings_car_list_menu_item:
                Intent carListSettingsIntent =
                        new Intent(this, CarsListSettingActivity.class);
//                startActivityForResult(carListSettingsIntent, SETTING_ACTION_CODE);
                startActivity(carListSettingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if(requestCode == ADD_CAR_ACTION_CODE){
                mCarsDbHelper.insertCar(Car.fromIntent(intent));
                recreate();
            }
//            else if (requestCode == SETTING_ACTION_CODE) {
//                final ArrayList<Car> cars = mCarsDbHelper.readCars();
//                final CarsRvAdapter carsRvAdapter = new CarsRvAdapter(transformCars(cars, intent));
//                mAutosRv.setAdapter(carsRvAdapter);
//            }
        }
    }

    private ArrayList<Car> transformCars(ArrayList<Car> cars){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        final LinkedList<Car> transformedCars = new LinkedList<>();
//        final String filterBrand =
//                intent.getStringExtra(CarsListSettingActivity.FILTER_BY_BRAND_KEY);
        final String filterBrand =
                sp.getString(getResources().getString(R.string.filter_brand_key), "");
        if(!filterBrand.isEmpty()){
            for (Car car: cars) {
                if(car.getBrand() != null &&
                        car.getBrand().getName().equals(filterBrand))
                    transformedCars.add(car);
            }
        } else {
            transformedCars.addAll(cars);
        }

        final LinkedList<Car> transformed1Cars = new LinkedList<>();
//        final String filterModel
//                = intent.getStringExtra(CarsListSettingActivity.FILTER_BY_MODEL_KEY);
        final String filterModel =
                sp.getString(getResources().getString(R.string.filter_model_key), "");
        if(!filterModel.isEmpty()){
            for (Car car: transformedCars) {
                if(car.getModel() != null &&
                        car.getModel().getName().equals(filterModel))
                    transformed1Cars.add(car);
            }
        } else {
            transformed1Cars.addAll(transformedCars);
        }

        final ArrayList<Car> filteredCars = new ArrayList<>(transformed1Cars);
//        final String sortType
//                = intent.getStringExtra(CarsListSettingActivity.SORT_PRICE_KEY);
//        if(sortType != null){
        final String sortType =
                sp.getString(getResources().getString(R.string.sort_price_key), "");
        if(!sortType.isEmpty()){
            if (sortType.equals(CarsListSettingActivity.SORT_PRICE_ASC)) {
                Collections.sort(filteredCars, new Comparator<Car>() {
                    @Override
                    public int compare(Car o1, Car o2) {
                        float res = o1.getPrice() - o2.getPrice();
                        if(res > 0)
                            return 1;
                        if(res < 0)
                            return -1;
                        return 0;
                    }
                });
            } else if (sortType.equals(CarsListSettingActivity.SORT_PRICE_DESC)){
                Collections.sort(filteredCars, new Comparator<Car>() {
                    @Override
                    public int compare(Car o1, Car o2) {
                        float res = o1.getPrice() - o2.getPrice();
                        if(res > 0)
                            return -1;
                        if(res < 0)
                            return 1;
                        return 0;
                    }
                });
            }
        }

        return filteredCars;
    }

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
