package me.finance.finance;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import me.finance.finance.Model.Intake;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseHandlerTest {


    private DatabaseHandler databaseHandler;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();

        databaseHandler = DatabaseHandler.getInstance(context);
        databaseHandler.open();
        databaseHandler.createTables();
        databaseHandler.deleteTableContents();
        databaseHandler.insertDummyValues();
    }

    @After
    public void closeDb() {
        databaseHandler.close();
    }



    @Test
    public void updateIntake() {
        assertFalse(databaseHandler.updateIntakes(new Intake(-1,0,"","","")));
    }

    @Test
    public void addIntake(){

        List<Intake> intakes_before = databaseHandler.getIntakes();


        // double value_, String date_, String name_, String comment_, int category_, int payment_opt_
        Intake intake_new = new Intake( 23,"12.2.18","Intake_test","Test",1,2);

        databaseHandler.addIntake(intake_new);


        List<Intake> intakes_after = databaseHandler.getIntakes();

    }

    @Test
    public void getIntake(){

        List<Intake> intakes = databaseHandler.getIntakes();

    }
}