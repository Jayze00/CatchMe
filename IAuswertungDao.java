package com.example.linearcatchme.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;

import java.util.List;

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
public interface IAuswertungDao {

    @Transaction
    @Query("SELECT * FROM Auswertung JOIN Ort on Auswertung.fkOrtId=Ort.ortId")
    public List<OrtMitAuswertung> getAuswertungsAndOrts();

    @Query("SELECT * FROM auswertung JOIN Ort on Auswertung.fkOrtId=Ort.ortId WHERE auswertungId LIKE :auswertungId LIMIT 1")
    Auswertung findById(int auswertungId);

    @Query("SELECT * FROM Auswertung JOIN Ort on Auswertung.fkOrtId=Ort.ortId")
    List<Auswertung> getAll();

    @Query("SELECT * FROM Auswertung JOIN Ort on Auswertung.fkOrtId=Ort.ortId WHERE fkOrtId LIKE :fkOrtId")
    Auswertung findByFk(int fkOrtId);

    @Query("UPDATE Auswertung SET anzahl_besuche = (anzahl_besuche + 1) WHERE auswertungId LIKE :auswertungId")
    void update(int auswertungId);

    @Insert
    void insertAll(Auswertung... auswertungs);

    @Delete
    void delete(Auswertung auswertung);



}