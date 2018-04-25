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
    public void testInvalidUpdateIntake1() {
        assertFalse(databaseHandler.updateIntakes(new Intake(-1,0,"","","",0,0)));
    }

    @Test
    public void testValidUpdateIntake1() {
        Intake intake = new Intake(0,"","name","",0,0);
        int id = (int)databaseHandler.addIntake(intake);
        intake.setId(id);
        intake.setName("new name");
        assertTrue(databaseHandler.updateIntakes(intake));
        assertEquals("new name", databaseHandler.getIntake(id).getName());
    }

    @Test
    public void testValidGetIntake1() {
        Intake intake = new Intake(0,"","name","",0,0);
        int id = (int)databaseHandler.addIntake(intake);
        assertEquals("name", databaseHandler.getIntake(id).getName());
    }

    @Test
    public void testInValidGetIntake1() {
        assertNull(databaseHandler.getIntake(-1));
    }



    @Test
    public void addIntake(){

        List<Intake> intakes_before = databaseHandler.getIntakes();
        int before = intakes_before.size();

        Intake intake_new = new Intake( 23,"12.2.18","Intake_test","Test",1,2);


        long AddIntakeReturn = databaseHandler.addIntake(intake_new);

        String string_print = Long.toString(AddIntakeReturn);
        String string_before = Integer.toString(before);

        String string_out_before = "Before: " + string_before;
        String string_out = "Return from Add Intake: " + string_print;
        System.out.println(string_out_before);
        System.out.println(string_out);


        List<Intake> intakes_after = databaseHandler.getIntakes();

        int after = intakes_after.size();

        String string_after = Integer.toString(before);
        String string_out_after = "After: " + string_after;

        System.out.println(string_out_after);

        assertEquals(before,after-1);

    }

    @Test
    public void getIntakes(){

        List<Intake> intakes = databaseHandler.getIntakes();

    }
}