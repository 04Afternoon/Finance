package me.finance.finance;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Permanent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PermanentsExecutionTest {

    private static DatabaseHandler database;

    @Before
    public void createDB() {
        database = DatabaseHandler.getInstance(InstrumentationRegistry.getContext());
        database.open();
        database.createTables();
//        database.deleteIntakes();
        database.deletePermanents();
//        insertPermanentsValues();
    }

    private void insertPermanentsValuesPast() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 12, 31);
        Calendar nextExecDate = Calendar.getInstance();
        nextExecDate.set(Calendar.DATE, nextExecDate.get(Calendar.DATE) - 1);

        Permanent permanent = new Permanent(1, startDate.getTime(), "M", endDate.getTime(), "name", "comment", 1, 1, nextExecDate.getTime());
        database.addPermanet(permanent);
    }

    private void insertPermanentsValuesFuture() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 12, 31);
        Calendar nextExecDate = Calendar.getInstance();
        nextExecDate.set(Calendar.DATE, nextExecDate.get(Calendar.DATE) + 1);

        Permanent permanent = new Permanent(1, startDate.getTime(), "M", endDate.getTime(), "name", "comment", 1, 1, nextExecDate.getTime());
        database.addPermanet(permanent);
    }


    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    public void navigateToAddPermAndBack() {
        onView(withId(R.id.navigation_permanents)).perform(click());
        onView(withId(R.id.navigation_balance)).perform(click());
    }

    @Test
    public void testGetDuePermanents() {
        insertPermanentsValuesPast();
        database.deleteIntakes();
        navigateToAddPermAndBack();
        List<Permanent> perms = database.getDuePermanents();
        assertEquals(0, perms.size());
        List<Intake> intakes = database.getIntakes();
        assertEquals(1, intakes.size());
    }

    @Test
    public void testGetDuePermanentsZero() {
        insertPermanentsValuesFuture();
        database.deleteIntakes();
        navigateToAddPermAndBack();
        List<Permanent> perms = database.getDuePermanents();
        assertEquals(0, perms.size());
        List<Intake> intakes = database.getIntakes();
        assertEquals(0, intakes.size());
    }


    @After
    public void closeDb() {
        database.close();
    }
}
