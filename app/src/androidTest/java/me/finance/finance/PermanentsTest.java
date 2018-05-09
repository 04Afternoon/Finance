package me.finance.finance;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.List;

import me.finance.finance.Model.Permanent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static android.support.test.espresso.Espresso.onData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PermanentsTest {

    private static DatabaseHandler database;
    private String name = "test";
    private String value = "12.34";
    private String startDate = "2000-01-01";
    private String endDate = "2019-12-31";
    private String intervalMonthly = "M";
    private String intervalWeekly = "W";
    private String paymentMethod = "Cash";
    private String category = "no category";

    @Before
    public void createDB() {
        database = DatabaseHandler.getInstance(InstrumentationRegistry.getContext());
        database.open();
        database.createTables();
        database.deletePermanents();
    }

    @Before
    public void navigateToAddPerm() {
        onView(withId(R.id.navigation_permanents)).perform(click());
        onView(withId(R.id.add_perms_button)).perform(click());
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testValidAddPermanent01() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.value_text_field)).perform(typeText(value), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(typeText(startDate), closeSoftKeyboard());
        onView(withId(R.id.end_text_field)).perform(typeText(endDate), closeSoftKeyboard());
        onView(withId(R.id.intervall_text_field)).perform(typeText(intervalMonthly), closeSoftKeyboard());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(paymentMethod))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        List<Permanent> permanents = database.getPermanents();
        for (Permanent permanent : permanents) {
            if (permanent.getName().equals(name) && permanent.getValue() == Double.valueOf(value) &&
                    permanent.getStartDate().equals(startDate) && permanent.getEndDate().equals(endDate) &&
                    permanent.getIteration().equals(intervalMonthly) && permanent.getCategory() == null &&
                    database.getPayment(permanent.getPayment_opt()).getName().equals(paymentMethod)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @After
    public void closeDb() {
        database.close();
    }
}
