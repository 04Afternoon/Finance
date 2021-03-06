package me.finance.finance;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.finance.finance.Model.Permanent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static me.finance.finance.Utils.convertDate;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
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
    private String value = "10";
    private String startDate = "2018-01-01";
    private String endDate = "2018-02-01";
    private String intervalMonthly = "M";
    private String intervalWeekly = "W";
    private String paymentMethod = "Cash";
    private String category = "no category";

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
        onView(withId(R.id.navigation_permanents)).perform(click());
        onView(withId(R.id.add_perms_button)).perform(click());
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testValidAddPermanent01() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.radioButtonIncome)).perform(click());
        onView(withId(R.id.value_text_field)).perform(typeText(value), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(click());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(startDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.end_text_field)).perform(click());
        calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(endDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());


        onView(withId(R.id.radioButtonMonthly)).perform(click());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(paymentMethod))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        // check if the permanent was correctly added to the database
        boolean found = false;
        List<Permanent> permanents = database.getPermanents();
        for (Permanent permanent : permanents) {
            if (permanent.getName().equals(name) && permanent.getValue() == Double.valueOf(value) &&
                    convertDate(permanent.getStartDate()).equals(startDate) && convertDate(permanent.getEndDate()).equals(endDate) &&
                    permanent.getIteration().equals(intervalMonthly) && permanent.getCategory() == null &&
                    database.getPayment(permanent.getPayment_opt()).getName().equals(paymentMethod)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    public void testValidAddPermanent02() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.radioButtonOutgoing)).perform(click());
        onView(withId(R.id.value_text_field)).perform(typeText(value), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(click());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(startDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.end_text_field)).perform(click());
        calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(endDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.radioButtonWeekly)).perform(click());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(paymentMethod))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        // check if the permanent was correctly added to the database
        boolean found = false;
        List<Permanent> permanents = database.getPermanents();
        for (Permanent permanent : permanents) {
            if (permanent.getName().equals(name) && permanent.getValue() == -1*Double.valueOf(value) &&
                    convertDate(permanent.getStartDate()).equals(startDate) && convertDate(permanent.getEndDate()).equals(endDate) &&
                    permanent.getIteration().equals(intervalWeekly) && permanent.getCategory() == null &&
                    database.getPayment(permanent.getPayment_opt()).getName().equals(paymentMethod)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testValidAddPermanent03() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.radioButtonIncome)).perform(click());
        onView(withId(R.id.value_text_field)).perform(typeText(value), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(click());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(startDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.end_text_field)).perform(click());
        calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(endDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.radioButtonMonthly)).perform(click());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(paymentMethod))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        // check if the permanent was correctly added to the database
        boolean found = false;
        List<Permanent> permanents = database.getPermanents();
        for (Permanent permanent : permanents) {
            if (permanent.getName().equals(name) && permanent.getValue() == Double.valueOf(value) &&
                    convertDate(permanent.getStartDate()).equals(startDate) && convertDate(permanent.getEndDate()).equals(endDate) &&
                    permanent.getIteration().equals(intervalMonthly) && permanent.getCategory() == null &&
                    database.getPayment(permanent.getPayment_opt()).getName().equals(paymentMethod)) {
                found = true;
                break;
            }
        }
        assertTrue(found);

        //check if intakes have been added for the permanent
        onView(withId(R.id.navigation_balance)).perform(click());
        onView(withId(R.id.einnahmen_monat)).check(matches(withText("20.00")));
        onView(withId(R.id.ausgaben_monat)).check(matches(withText("0.00")));
    }

    @Test
    public void testValidAddPermanent04() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.radioButtonOutgoing)).perform(click());
        onView(withId(R.id.value_text_field)).perform(typeText(value), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(click());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(startDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.end_text_field)).perform(click());
        calendar = Calendar.getInstance();
        calendar.setTime(Utils.convertDate(endDate));
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.radioButtonWeekly)).perform(click());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(paymentMethod))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(category))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        // check if the permanent was correctly added to the database
        boolean found = false;
        List<Permanent> permanents = database.getPermanents();
        for (Permanent permanent : permanents) {
            if (permanent.getName().equals(name) && permanent.getValue() == -1*Double.valueOf(value) &&
                    convertDate(permanent.getStartDate()).equals(startDate) && convertDate(permanent.getEndDate()).equals(endDate) &&
                    permanent.getIteration().equals(intervalWeekly) && permanent.getCategory() == null &&
                    database.getPayment(permanent.getPayment_opt()).getName().equals(paymentMethod)) {
                found = true;
                break;
            }
        }
        assertTrue(found);

        //check if intakes have been added for the permanent
        onView(withId(R.id.navigation_balance)).perform(click());
        onView(withId(R.id.einnahmen_monat)).check(matches(withText("0.00")));
        onView(withId(R.id.ausgaben_monat)).check(matches(withText("-50.00")));
    }

    @Test
    public void testGetDuePermanents() {
        database.deletePermanents();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 12, 31);
        Calendar nextExecDate = Calendar.getInstance();
        nextExecDate.set(2010, 10, 10);
        Calendar queryDate = Calendar.getInstance();
        queryDate.set(2015, 10, 10);

        Permanent permanent = new Permanent(1, startDate.getTime(), "M", endDate.getTime(), "name", "comment", 1, 1, nextExecDate.getTime());

        database.addPermanet(permanent);
        List<Permanent> perms = database.getDuePermanents();

        assertEquals(perms.size(), 1);
    }


    @Test
    public void testGetDuePermanentsEmpty() {
        database.deletePermanents();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 12, 31);
        Calendar nextExecDate = Calendar.getInstance();
        nextExecDate.set(2019, 10, 10);

        Permanent permanent = new Permanent(1, startDate.getTime(), "M", endDate.getTime(), "name", "comment", 1, 1, nextExecDate.getTime());

        database.addPermanet(permanent);
        List<Permanent> perms = database.getDuePermanents();

        assertEquals(0, perms.size());
    }



    @After
    public void closeDb() {
        database.close();
    }
}
