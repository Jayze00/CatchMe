package com.example.linearcatchme.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Auswertung {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "auswertungId")
    private int auswertungId;

    @ColumnInfo(name = "anzahl_besuche")
    private int anzBesuche;

    public int getFkOrtId() {
        return fkOrtId;
    }

    public void setFkOrtId(int fkOrtId) {
        this.fkOrtId = fkOrtId;
    }

    @ColumnInfo(name = "fkOrtId")
    private int fkOrtId;

    public int getAuswertungId() {
        return auswertungId;
    }

    public void setAuswertungId(int auswertungId) {
        this.auswertungId = auswertungId;
    }

    public int getAnzBesuche() {
        return anzBesuche;
    }

    public void setAnzBesuche(int anzBesuche) {
        this.anzBesuche = anzBesuche;
    }
}
