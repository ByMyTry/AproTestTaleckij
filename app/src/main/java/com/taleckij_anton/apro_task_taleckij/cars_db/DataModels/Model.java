package com.taleckij_anton.apro_task_taleckij.cars_db.DataModels;

public class Model {
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Model(String name) {
        this.mName = name;
    }
}
