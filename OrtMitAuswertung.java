package com.example.linearcatchme.database;

import androidx.room.Embedded;
import androidx.room.Relation;
import androidx.room.RoomWarnings;

public class OrtMitAuswertung {
    @Embedded
    public Ort ort;
    @Relation(
            parentColumn = "ortId",
            entityColumn = "fkOrtId"
    )
    public Auswertung auswertung;
}
