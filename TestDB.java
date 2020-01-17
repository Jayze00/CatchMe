package com.example.linearcatchme;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.linearcatchme.database.AppDatabase;
import com.example.linearcatchme.database.Auswertung;
import com.example.linearcatchme.database.IAuswertungDao;
import com.example.linearcatchme.database.IOrtDao;
import com.example.linearcatchme.database.Ort;
import com.example.linearcatchme.database.OrtMitAuswertung;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestDB {
    private IOrtDao mOrtDao;
    private IAuswertungDao mAuswertungDao;
    private AppDatabase mDb;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mOrtDao = mDb.ortDao();
        mAuswertungDao = mDb.auswertungDao();
    }

    @After
    public void closeDB() throws IOException{
        mDb.close();
    }

    @Test
    public void writeOrtandAuswertung() throws Exception{
        //insert ort
        Ort ort = new Ort();
        ort.setBreitengrad(25.763531);
        ort.setLaengengrad(-80.191008);
        mOrtDao.insertAll(ort);

        //insert auswertung
        Auswertung auswertung = new Auswertung();
        auswertung.setAnzBesuche(0);
        auswertung.setFkOrtId(mOrtDao.findByCoordinates(-80.191008, 25.763531).getOrtId());
        OrtMitAuswertung oA = new OrtMitAuswertung();
        oA.auswertung = auswertung;
        oA.ort = ort;
        mAuswertungDao.insertAll(auswertung);
        Auswertung auswertung1 = new Auswertung();
        auswertung1 = mAuswertungDao.findByFk(mOrtDao.findByCoordinates(-80.191008, 25.763531).getOrtId());
        mAuswertungDao.update(auswertung1.getAuswertungId());
        auswertung1 = mAuswertungDao.findById(auswertung1.getAuswertungId());
        System.out.println(auswertung1.getFkOrtId()+" " +auswertung1.getAuswertungId() +" " +auswertung1.getAnzBesuche());
        List<OrtMitAuswertung> lr = mAuswertungDao.getAuswertungsAndOrts();


        assertEquals(1, lr.size());

    }

}