package me.finance.finance;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;

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
        //databaseHandler.insertDummyValues();
    }

    @After
    public void closeDb() {
        databaseHandler.close();
    }



    @Test
    public void testInvalidUpdateIntake1() {
        assertFalse(databaseHandler.updateIntakes(new Intake(-1,0, new Date(),"","",0,0)));
    }

    @Test
    public void testValidUpdateIntake1() {
        Intake intake = new Intake(0, new Date(),"name","",0,0);
        int id = (int)databaseHandler.addIntake(intake);
        intake.setId(id);
        intake.setName("new name");
        assertTrue(databaseHandler.updateIntakes(intake));
        assertEquals("new name", databaseHandler.getIntake(id).getName());
    }

    @Test
    public void testValidGetIntake1() {
        Intake intake = new Intake(0,"2019-12-18","name","",0,0);
        int id = (int)databaseHandler.addIntake(intake);
        assertEquals("name", databaseHandler.getIntake(id).getName());
    }

    @Test
    public void testInValidGetIntake1() {
        assertNull(databaseHandler.getIntake(-1));
    }

    @Test
    public void testValidGetCategory1() {
        Category category = new Category("category");
        int id = (int)databaseHandler.addCategory(category);
        assertEquals("category", databaseHandler.getCategory(id).getName());
    }

    @Test
    public void testInvalidGetCategory1() {
        assertNull(databaseHandler.getCategory(-1));
    }

    @Test
    public void testValidGetCategories1() {
        Category category = new Category("category");
        int id = (int)databaseHandler.addCategory(category);
        assertEquals(1, databaseHandler.getCategories().size());
        assertEquals(id, databaseHandler.getCategories().get(0).getId());
    }

    @Test
    public void testInvalidGetCategories1() {
        assertEquals(0,databaseHandler.getCategories().size());
    }



    @Test
    public void addIntake(){

        List<Intake> intakes_before = databaseHandler.getIntakes();
        int before = intakes_before.size();

        Intake intake_new = new Intake( 23,"2018-02-12","Intake_test","Test",1,2);


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

    @Test
    public void testGetPayments() {
        String name = "test payment method";
        Payment payment = new Payment(name);
        long id = databaseHandler.addPayment(payment);
        List<Payment> payments = databaseHandler.getPayments();
        boolean found = false;
        for (Payment p : payments) {
            if (p.getId() == id && p.getName().equals(name)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}