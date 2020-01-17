package com.example.linearcatchme.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IOrtDao {
    @Query("SELECT * FROM ort")
    List<Ort> getAll();

    @Query("SELECT * FROM ort WHERE laengengrad LIKE :laengengrad AND " + "breitengrad LIKE :breitengrad LIMIT 1")
    Ort findByCoordinates(Double laengengrad, Double breitengrad);

    @Insert
    void insertAll(Ort... orts);

    @Delete
    void delete(Ort ort);

}