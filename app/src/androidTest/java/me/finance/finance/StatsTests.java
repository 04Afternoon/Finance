package me.finance.finance;

import android.support.test.InstrumentationRegistry;
import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class StatsTests {
    private static DatabaseHandler database;

    @Before
    public void createDB() {
        database = DatabaseHandler.getInstance(InstrumentationRegistry.getContext());
        database.open();
        database.createTables();
        database.deleteIntakes();
        database.deletePermanents();
    }

    @Before
    public void navigateToAddPerm() {
        onView(withId(R.id.navigation_stats)).perform(click());
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void statsTest1() {
        withId(R.id.in_out_chart).matches(isDisplayed());
        onView(withId(R.id.stats_calender_button)).perform(click());
        onView(withText("OK")).perform(click());
        assertTrue(true);
    }
}
