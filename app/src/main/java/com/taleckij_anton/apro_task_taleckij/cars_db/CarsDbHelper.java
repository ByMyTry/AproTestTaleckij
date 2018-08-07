package com.taleckij_anton.apro_task_taleckij.cars_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Brand;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Car;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Model;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Photo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class CarsDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "cars_shop.db";

    public CarsDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.i("2","---------------------");
        db.execSQL(CarsDb.CREATE_BRANDS_TABLE_SCRIPT);
        db.execSQL(CarsDb.CREATE_MODELS_TABLE_SCRIPT);
        db.execSQL(CarsDb.CREATE_CARS_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CarsDb.DROP_BRANDS_TABLE_SCRIPT);
        db.execSQL(CarsDb.DROP_MODELS_TABLE_SCRIPT);
        db.execSQL(CarsDb.DROP_CARS_TABLE_SCRIPT);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertCar(Car car, long brandId, long modelId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CarsDb.CarsTableColumns.PRICE, car.getPrice());
            contentValues.put(CarsDb.CarsTableColumns.MANUFACTURE_YEAR, car.getManufactureYear());
            contentValues.put(CarsDb.CarsTableColumns.MILEAGE, car.getMileage());
            contentValues.put(CarsDb.CarsTableColumns.ENGINE_VOLUME, car.getEngineVolume());
            contentValues.put(CarsDb.CarsTableColumns.FUEL_TANK_VOLUME, car.getFuelTankVolume());
            contentValues.put(CarsDb.CarsTableColumns.MAX_SPEED, car.getMaxSpeed());
            contentValues.put(CarsDb.CarsTableColumns.WEIGHT, car.getWeight());
            contentValues.put(CarsDb.CarsTableColumns.PHOTO, car.getPhoto().getBytesPhoto());
            contentValues.put(CarsDb.CarsTableColumns.BRAND_ID, brandId);
            contentValues.put(CarsDb.CarsTableColumns.MODEL_ID, modelId);
            db.insert(CarsDb.CARS_TABLE, null, contentValues);
        } catch (SQLiteException e) {}
    }

    public void insertCar(Car car) {
        String brandName = car.getBrand().getName();
        String modelName = car.getModel().getName();

        long brandId = readBrandId(brandName);
        long modelId = readModelId(modelName);

        if(brandId == -1)
            brandId = insertBrand(brandName);
        if(modelId == -1)
            modelId = insertModel(modelName);

        insertCar(car, brandId, modelId);
    }

//    public ContentValues getContentValues(int brandId, int modelId){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(CarsDb.CarsTableColumns.PRICE, mPrice);
//        contentValues.put(CarsDb.CarsTableColumns.MANUFACTURE_YEAR, mManufactureYear);
//        contentValues.put(CarsDb.CarsTableColumns.MILEAGE, mMileage);
//        contentValues.put(CarsDb.CarsTableColumns.ENGINE_VOLUME, mEngineVolume);
//        contentValues.put(CarsDb.CarsTableColumns.FUEL_TANK_VOLUME, mFuelTankVolume);
//        contentValues.put(CarsDb.CarsTableColumns.MAX_SPEED, mMaxSpeed);
//        contentValues.put(CarsDb.CarsTableColumns.WEIGHT, mWeight);
//        contentValues.put(CarsDb.CarsTableColumns.PHOTO, getBitmapAsByteArray(mPhoto));
//        contentValues.put(CarsDb.CarsTableColumns.BRAND_ID, brandId);
//        contentValues.put(CarsDb.CarsTableColumns.MODEL_ID, modelId);
//        return contentValues;
//    }

    public long insertBrand(String brandName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarsDb.BrandsTableColumns.NAME, brandName);
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.insert(CarsDb.BRANDS_TABLE, null, contentValues);
            db.close();
            return id;
        } catch (SQLiteException e) {}
        return -1;
    }

    public long insertModel(String modelName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarsDb.ModelsTableColumns.NAME, modelName);
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            long id = db.insert(CarsDb.MODELS_TABLE, null, contentValues);
            db.close();
            return id;
        } catch (SQLiteException e) {}
        return -1;
    }

    public ArrayList<Car> readCars(){
        final LinkedList<Car> cars = new LinkedList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.query(
//                    //DISTINCT?
//                    true,
//                    CarsDb.CARS_TABLE,
//                    new String[]{CarsDb.CarsTableColumns., CarsDb.CarsTableColumns.PHOTO},
//                    null,
//                    null,
//                    //GROUP BY
//                    null,
//                    //having
//                    null,
//                    //ORDER BY
//                    null,
//                    null
//            );
            Cursor cursor = db.rawQuery(CarsDb.SELECT_ALL_CARS, null);

            while (cursor.moveToNext()) {
                float price = cursor.getFloat(cursor.getColumnIndex(CarsDb.CarsTableColumns.PRICE));
                int manufacture_year = cursor.getInt(
                        cursor.getColumnIndex(CarsDb.CarsTableColumns.MANUFACTURE_YEAR));
                int mileage =
                        cursor.getInt(cursor.getColumnIndex(CarsDb.CarsTableColumns.MILEAGE));
                int engine_volume = cursor.getInt(
                        cursor.getColumnIndex(CarsDb.CarsTableColumns.ENGINE_VOLUME));
                int fuel_tank_volume = cursor.getInt(
                        cursor.getColumnIndex(CarsDb.CarsTableColumns.FUEL_TANK_VOLUME));
                int max_speed =
                        cursor.getInt(cursor.getColumnIndex(CarsDb.CarsTableColumns.MAX_SPEED));
                int weight =
                        cursor.getInt(cursor.getColumnIndex(CarsDb.CarsTableColumns.WEIGHT));
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(CarsDb.CarsTableColumns.PHOTO));

                String brandName = cursor.getString(
                        cursor.getColumnIndex(CarsDb.BrandsTableColumns.NAME));
                String modelName = cursor.getString(
                        cursor.getColumnIndex(CarsDb.ModelsTableColumns.NAME));

//                Log.i("+++++++++++++++++++", brandName);
//                Log.i("+++++++++++++++++++", modelName);

                Car car = new Car(
                        price, manufacture_year, mileage, engine_volume,
                        fuel_tank_volume, max_speed, weight,
                        new Photo(null, bytes),// getByteArrayAsBitmap(bytes),
                        new Brand(brandName), new Model(modelName)
                );
                cars.add(car);
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {}
        return new ArrayList<>(cars);
    }

    public long readBrandId(String brandName){
        long brandId = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(
                    //DISTINCT?
                    true,
                    CarsDb.BRANDS_TABLE,
                    new String[]{CarsDb.BrandsTableColumns._ID},
                    CarsDb.BrandsTableColumns.NAME + " = ? ",
                    new String[]{brandName},
                    //GROUP BY
                    null,
                    //having
                    null,
                    //ORDER BY
                    null,
                    null
            );
            if (cursor.moveToNext()) {
                brandId = cursor.getLong(0);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {}
        return brandId;
    }

    public long readModelId(String modelName){
        long modelId = -1;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(
                    //DISTINCT?
                    true,
                    CarsDb.MODELS_TABLE,
                    new String[]{CarsDb.ModelsTableColumns._ID},
                    CarsDb.ModelsTableColumns.NAME + " = ? ",
                    new String[]{modelName},
                    //GROUP BY
                    null,
                    //having
                    null,
                    //ORDER BY
                    null,
                    null
            );
            if (cursor.moveToNext()) {
                modelId = cursor.getLong(0);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {}
        return modelId;
    }

    public void clearData() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CarsDb.CARS_TABLE, null, null);
            db.delete(CarsDb.BRANDS_TABLE, null, null);
            db.delete(CarsDb.MODELS_TABLE, null, null);
            db.close();
        } catch (SQLiteException e) {}
    }
}
