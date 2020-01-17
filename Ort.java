package com.example.linearcatchme.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ort {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ortId")
    private int ortId;

    @ColumnInfo(name = "breitengrad")
    private double breitengrad;

    @ColumnInfo(name = "laengengrad")
    private double laengengrad;

    public int getOrtId() {
        return ortId;
    }

    public void setOrtId(int ortId){
        this.ortId = ortId;
    }

    public double getBreitengrad() {
        return breitengrad;
    }

    public void setBreitengrad(double breitengrad) {
        this.breitengrad = breitengrad;
    }

    public double getLaengengrad() {
        return laengengrad;
    }

    public void setLaengengrad(double laengengrad) {
        this.laengengrad = laengengrad;
    }
}
