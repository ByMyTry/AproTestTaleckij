package com.taleckij_anton.apro_task_taleckij.cars_db;

import android.provider.BaseColumns;

public interface CarsDb {
    String CARS_TABLE = "CARS_TABLE";
    String BRANDS_TABLE = "BRANDS_TABLE";
    String MODELS_TABLE = "MODELS_TABLE";

    interface CarsTableColumns extends BaseColumns{
        String _ID = "car_id";
        String PRICE = "price";

        String MANUFACTURE_YEAR = "manufacture_year";
        String MILEAGE = "mileage";
        String ENGINE_VOLUME = "engine_volume";
        String FUEL_TANK_VOLUME = "fuel_tank_volume";
        String MAX_SPEED = "max_speed";
        String WEIGHT = "weight";

        String PHOTO = "photo";

        String BRAND_ID = "brand_id";
        String MODEL_ID = "model_id";
    }

    interface  BrandsTableColumns extends BaseColumns{
        String _ID = "brand_id";
        String NAME = "brand_name";
    }

    interface  ModelsTableColumns extends BaseColumns{
        String _ID = "model_id";
        String NAME = "model_name";
    }

    String CREATE_CARS_TABLE_SCRIPT =
            "CREATE TABLE IF NOT EXISTS " + CARS_TABLE + "(" +
                    CarsTableColumns._ID + " INTEGER PRIMARY KEY, " +
                    CarsTableColumns.PRICE + " REAL," +

                    CarsTableColumns.MANUFACTURE_YEAR + " INTEGER," +
                    CarsTableColumns.MILEAGE + " INTEGER," +
                    CarsTableColumns.ENGINE_VOLUME + " INTEGER," +
                    CarsTableColumns.FUEL_TANK_VOLUME + " INTEGER," +
                    CarsTableColumns.MAX_SPEED + " INTEGER," +
                    CarsTableColumns.WEIGHT + " INTEGER," +

                    CarsTableColumns.PHOTO + " BLOB," +

                    CarsTableColumns.BRAND_ID + " INTEGER," +
                    CarsTableColumns.MODEL_ID + " INTEGER," +

                    "FOREIGN KEY(" + CarsTableColumns.BRAND_ID +
                    ") REFERENCES "+ BRANDS_TABLE +"("+ BrandsTableColumns._ID +")" +
                    " ON DELETE CASCADE "+
                    "FOREIGN KEY(" + CarsTableColumns.MODEL_ID +
                    ") REFERENCES "+ MODELS_TABLE +"("+ ModelsTableColumns._ID +")" +
                    " ON DELETE CASCADE "+
            ")";

    String SELECT_ALL_CARS =
            "SELECT DISTINCT " +
                    CARS_TABLE + "." + CarsTableColumns._ID +  "," +
                    CARS_TABLE + "." + CarsTableColumns.PRICE +  "," +
                    CARS_TABLE + "." + CarsTableColumns.MANUFACTURE_YEAR +  "," +
                    CARS_TABLE + "." + CarsTableColumns.MILEAGE +  "," +
                    CARS_TABLE + "." + CarsTableColumns.ENGINE_VOLUME +  "," +
                    CARS_TABLE + "." + CarsTableColumns.FUEL_TANK_VOLUME +  "," +
                    CARS_TABLE + "." + CarsTableColumns.MAX_SPEED +  "," +
                    CARS_TABLE + "." + CarsTableColumns.WEIGHT +  "," +
                    CARS_TABLE + "." + CarsTableColumns.PHOTO +  "," +

                    BRANDS_TABLE + "." + BrandsTableColumns.NAME +  "," +
                    MODELS_TABLE + "." + ModelsTableColumns.NAME +
            " FROM (" + CARS_TABLE +
                    " LEFT JOIN " + BRANDS_TABLE +
                    " ON " + CARS_TABLE + "." + CarsTableColumns.BRAND_ID  + " = " +
                    BRANDS_TABLE + "." + BrandsTableColumns._ID +
                    ") LEFT JOIN " + MODELS_TABLE +
                    " ON " + CARS_TABLE + "." + CarsTableColumns.MODEL_ID  + " = " +
                    MODELS_TABLE + "." + ModelsTableColumns._ID;


    String SELECT_ALL_BRANDS_NAMES =
            "SELECT DISTINCT " +
                    BRANDS_TABLE + "." + BrandsTableColumns.NAME +
                    " FROM " + BRANDS_TABLE;

    String SELECT_ALL_MODELS_NAMES =
            "SELECT DISTINCT " +
                    MODELS_TABLE + "." + ModelsTableColumns.NAME +
                    " FROM " + MODELS_TABLE;

    String DROP_CARS_TABLE_SCRIPT =
            "DROP TABLE IF EXISTS " + CARS_TABLE;

    String CREATE_BRANDS_TABLE_SCRIPT =
            "CREATE TABLE IF NOT EXISTS " + BRANDS_TABLE + "(" +
                    BrandsTableColumns._ID +  " INTEGER PRIMARY KEY, " +
                    BrandsTableColumns.NAME + " TEXT" +
            ")";

    String DROP_BRANDS_TABLE_SCRIPT =
            "DROP TABLE IF EXISTS " + BRANDS_TABLE;

    String CREATE_MODELS_TABLE_SCRIPT =
            "CREATE TABLE IF NOT EXISTS " + MODELS_TABLE + "(" +
                    ModelsTableColumns._ID +  " INTEGER PRIMARY KEY, " +
                    ModelsTableColumns.NAME + " TEXT" +
            ")";

    String DROP_MODELS_TABLE_SCRIPT =
            "DROP TABLE IF EXISTS " + MODELS_TABLE;
}
