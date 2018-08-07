package com.taleckij_anton.apro_task_taleckij.cars_db.DataModels;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;

import com.taleckij_anton.apro_task_taleckij.cars_db.CarsDb;

import java.io.ByteArrayOutputStream;

import javax.annotation.Nullable;

public class Car {
    private float mPrice;
    private int mManufactureYear;
    private int mMileage;
    private int mEngineVolume;
    private int mFuelTankVolume;
    private int mMaxSpeed;
    private int mWeight;
    private Photo mPhoto;

    @Nullable
    private Brand mBrand;
    @Nullable
    private Model mModel;

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    public int getManufactureYear() {
        return mManufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.mManufactureYear = manufactureYear;
    }

    public int getMileage() {
        return mMileage;
    }

    public void setMileage(int mileage) {
        this.mMileage = mileage;
    }

    public int getEngineVolume() {
        return mEngineVolume;
    }

    public void setEngineVolume(int engineVolume) {
        this.mEngineVolume = engineVolume;
    }

    public int getFuelTankVolume() {
        return mFuelTankVolume;
    }

    public void setFuelTankVolume(int fuelTankVolume) {
        this.mFuelTankVolume = fuelTankVolume;
    }

    public int getMaxSpeed() {
        return mMaxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.mMaxSpeed = maxSpeed;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        this.mWeight = weight;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        this.mPhoto = photo;
    }

    public Brand getBrand() {
        return mBrand;
    }

    public void setBrand(Brand brand) {
        this.mBrand = brand;
    }

    public Model getModel() {
        return mModel;
    }

    public void setModel(Model model) {
        this.mModel = model;
    }

    public Car(float price, int manufactureYear, int mileage,
               int engineVolume, int fuelTankVolume, int maxSpeed,
               int weight, Photo photo,
               @Nullable Brand brand, @Nullable Model model) {
        this.mPrice = price;
        this.mManufactureYear = manufactureYear;
        this.mMileage = mileage;
        this.mEngineVolume = engineVolume;
        this.mFuelTankVolume = fuelTankVolume;
        this.mMaxSpeed = maxSpeed;
        this.mWeight = weight;
        this.mPhoto = photo;
        this.mBrand = brand;
        this.mModel = model;
    }

    public Car(float price, int manufactureYear, int mileage,
               int engineVolume, int fuelTankVolume, int maxSpeed,
               int weight, Photo photo) {
        this(price, manufactureYear, mileage, engineVolume, fuelTankVolume,
                maxSpeed, weight, photo, null, null);
    }

    public Intent toIntent(Intent intent){
        intent.putExtra(CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.PRICE, getPrice());
        intent.putExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.MANUFACTURE_YEAR,
                getManufactureYear());
        intent.putExtra(CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.MILEAGE, getMileage());
        intent.putExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.ENGINE_VOLUME,
                getEngineVolume());
        intent.putExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.FUEL_TANK_VOLUME,
                getFuelTankVolume());
        intent.putExtra(CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.MAX_SPEED, getMaxSpeed());
        intent.putExtra(CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.WEIGHT, getWeight());
        intent.putExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.PHOTO,
                getPhoto().getBytesPhoto());
        intent.putExtra(
                CarsDb.BRANDS_TABLE + CarsDb.BrandsTableColumns.NAME,
                getBrand().getName());
        intent.putExtra(
                CarsDb.MODELS_TABLE + CarsDb.ModelsTableColumns.NAME,
                getModel().getName());
        return intent;
    }

    public static Car fromIntent(Intent intent){
        float price = intent.getFloatExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.PRICE, 0);
        int manufactureYear = intent.getIntExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.MANUFACTURE_YEAR, 0);
        int mileage = intent.getIntExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.MILEAGE, 0);
        int engineVolume = intent.getIntExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.ENGINE_VOLUME, 0);
        int fuelTankVolume = intent.getIntExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.FUEL_TANK_VOLUME, 0);
        int maxSpeed = intent.getIntExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.MAX_SPEED, 0);
        int weight = intent.getIntExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.WEIGHT, 0);

        byte[] bitmapPhoto = intent.getByteArrayExtra(
                CarsDb.CARS_TABLE + CarsDb.CarsTableColumns.PHOTO);
        Photo photo = new Photo(Photo.getByteArrayAsBitmap(bitmapPhoto), bitmapPhoto);

        String brandName = intent.getStringExtra(
                CarsDb.BRANDS_TABLE + CarsDb.BrandsTableColumns.NAME);
        Brand brand = new Brand(brandName);

        String modelName = intent.getStringExtra(
                CarsDb.MODELS_TABLE + CarsDb.ModelsTableColumns.NAME);
        Model model = new Model(modelName);
        return new Car(price, manufactureYear, mileage, engineVolume,
                fuelTankVolume, maxSpeed, weight, photo,
                brand, model);
    }
}
