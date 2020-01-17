package com.example.linearcatchme.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.Transaction;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class SaveService {

    private IOrtDao mOrtDao;
    private IAuswertungDao mAuswertungDao;
    private AppDatabase db;
    private OrtMitAuswertung mOrtMitAuswertung;


    public SaveService(AppDatabase db) {

        this.db = db;
        mOrtDao = db.ortDao();
        mAuswertungDao = db.auswertungDao();
    }


    public void savePlace(LatLng position) {

        Ort ort = new Ort();
        ort.setBreitengrad(position.latitude);
        ort.setLaengengrad(position.longitude);
        mOrtDao.insertAll(ort);
    }


    public List<LatLng> getTrackedPlace() {
        List<Ort> places = mOrtDao.getAll();
        List<LatLng> trackedPlaces = new ArrayList<>();
        for(Ort place : places){
            LatLng coordinates = new LatLng(place.getBreitengrad(),place.getLaengengrad());
            trackedPlaces.add(coordinates);
        }
       return trackedPlaces;
    }

    public List<Ort> getTrackedOrt(){
        return mOrtDao.getAll();
    }

    public void saveAuswertung(Ort ort){
        Auswertung auswertung = mAuswertungDao.findByFk(ort.getOrtId());
        if(auswertung != null){
            mAuswertungDao.update(auswertung.getAuswertungId());
        } else{
            auswertung.setAnzBesuche(1);
            auswertung.setFkOrtId(ort.getOrtId());
            mOrtMitAuswertung = new OrtMitAuswertung();
            mOrtMitAuswertung.auswertung = auswertung;
            mOrtMitAuswertung.ort = ort;
            mAuswertungDao.insertAll(auswertung);
        }
    }

    public int anzBesuche(Ort ort){
        return mAuswertungDao.findByFk(ort.getOrtId()).getAnzBesuche();
    }

}
