package com.taleckij_anton.apro_task_taleckij.cars_db.DataModels;

public class Brand {
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Brand(String name) {
        this.mName = name;
    }
}
