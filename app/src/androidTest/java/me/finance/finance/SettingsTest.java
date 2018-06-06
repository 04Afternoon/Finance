package me.finance.finance;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import me.finance.finance.Model.Permanent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SettingsTest {

    private DatabaseHandler databaseHandler;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

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
    public void clearDb() {
        assertTrue(databaseHandler.getCategories().size() > 0);
        assertTrue(databaseHandler.getIntakes().size() > 0);
        assertTrue(databaseHandler.getPayments().size() > 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.clear_database_button)).perform(click());

        assertTrue(databaseHandler.getCategories().size() == 0);
        assertTrue(databaseHandler.getIntakes().size() == 0);
        assertTrue(databaseHandler.getPayments().size() == 1);
        assertTrue(databaseHandler.getPermanents().size() == 0);
    }
}
