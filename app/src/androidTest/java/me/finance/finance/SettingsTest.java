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
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Permanent;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.JMock1Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SettingsTest {

    private DatabaseHandler databaseHandler;
    private String categoryName = "foo";
    private String paymentName = "bar";
    private String categoryName2 = "brst";
    private String paymentName2 = "yelp";

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();

        databaseHandler = DatabaseHandler.getInstance(context);
        databaseHandler.open();
        databaseHandler.createTables();
        databaseHandler.deleteTableContents();
    }

    @After
    public void closeDb() {
        databaseHandler.close();
    }

    @Test
    public void clearDb() {
        databaseHandler.insertDummyValues();
        assertTrue(databaseHandler.getCategories().size() > 0);
        assertTrue(databaseHandler.getIntakes().size() > 0);
        assertTrue(databaseHandler.getPayments().size() > 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.clear_database_button)).perform(click());
        onView(withText("Yes")).perform(click());

        assertTrue(databaseHandler.getCategories().size() == 0);
        assertTrue(databaseHandler.getIntakes().size() == 0);
        assertTrue(databaseHandler.getPayments().size() == 1);
        assertTrue(databaseHandler.getPermanents().size() == 0);
    }

    @Test
    public void addCategory () {

        assertTrue(databaseHandler.getCategories(categoryName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.categories_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(categoryName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getCategories(categoryName).size() == 1);
        onData(is(categoryName)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void addDuplicateCategory () {

        assertTrue(databaseHandler.getCategories(categoryName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.categories_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(categoryName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getCategories(categoryName).size() == 1);
        onData(is(categoryName)).check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.categoryName)).perform(typeText(categoryName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());


        assertTrue(databaseHandler.getCategories(categoryName).size() == 1);
        onData(is(categoryName)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void removeCategory () {

        assertTrue(databaseHandler.getCategories(categoryName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.categories_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(categoryName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getCategories(categoryName).size() == 1);
        onData(is(categoryName)).check(matches(isCompletelyDisplayed()));
        onData(is(categoryName)).perform(click());

        onView(withId(R.id.delete)).perform(click());
        assertTrue(databaseHandler.getCategories(categoryName).size() == 0);
    }

    @Test
    public void changeCategory() {

        assertTrue(databaseHandler.getCategories(categoryName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.categories_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(categoryName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getCategories(categoryName).size() == 1);
        onData(is(categoryName)).check(matches(isCompletelyDisplayed()));
        onData(is(categoryName)).perform(click());

        onView(withId(R.id.accountNameInput)).perform(typeText(categoryName2), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
        assertTrue(databaseHandler.getCategories(categoryName).size() == 0);
        assertTrue(databaseHandler.getCategories(categoryName2).size() == 1);
        onData(is(categoryName2)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void changeCategoryCancel() {

        assertTrue(databaseHandler.getCategories(categoryName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.categories_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(categoryName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getCategories(categoryName).size() == 1);
        onData(is(categoryName)).check(matches(isCompletelyDisplayed()));
        onData(is(categoryName)).perform(click());

        onView(withId(R.id.accountNameInput)).perform(typeText(categoryName2), closeSoftKeyboard());
        onView(withId(R.id.cancel)).perform(click());
        assertTrue(databaseHandler.getCategories(categoryName).size() == 1);
        assertTrue(databaseHandler.getCategories(categoryName2).size() == 0);
        onData(is(categoryName)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void addPayment() {

        assertTrue(databaseHandler.getPayments().size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.accounts_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(paymentName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getPayments(paymentName).size() == 1);
        onData(is(paymentName)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void addDuplicatePayment() {

        assertTrue(databaseHandler.getPayments(paymentName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.accounts_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(paymentName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getPayments(paymentName).size() == 1);
        onData(is(paymentName)).check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.categoryName)).perform(typeText(paymentName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());


        assertTrue(databaseHandler.getPayments(paymentName).size() == 1);
        onData(is(paymentName)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void removePayment() {

        assertTrue(databaseHandler.getPayments(paymentName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.accounts_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(paymentName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getPayments(paymentName).size() == 1);
        onData(is(paymentName)).check(matches(isCompletelyDisplayed()));
        onData(is(paymentName)).perform(click());

        onView(withId(R.id.delete)).perform(click());
        assertTrue(databaseHandler.getPayments(paymentName).size() == 0);
    }

    @Test
    public void changePayment() {

        assertTrue(databaseHandler.getPayments(paymentName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.accounts_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(paymentName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getPayments(paymentName).size() == 1);
        onData(is(paymentName)).check(matches(isCompletelyDisplayed()));
        onData(is(paymentName)).perform(click());

        onView(withId(R.id.accountNameInput)).perform(typeText(paymentName2), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
        assertTrue(databaseHandler.getPayments(paymentName).size() == 0);
        assertTrue(databaseHandler.getPayments(paymentName2).size() == 1);
        onData(is(paymentName2)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void changePaymentCancel() {

        assertTrue(databaseHandler.getPayments(paymentName).size() == 0);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.accounts_settings_button)).perform(click());
        onView(withId(R.id.categoryName)).perform(typeText(paymentName), closeSoftKeyboard());
        onView(withId(R.id.create_category)).perform(click());

        assertTrue(databaseHandler.getPayments(paymentName).size() == 1);
        onData(is(paymentName)).check(matches(isCompletelyDisplayed()));
        onData(is(paymentName)).perform(click());

        onView(withId(R.id.accountNameInput)).perform(typeText(paymentName2), closeSoftKeyboard());
        onView(withId(R.id.cancel)).perform(click());
        assertTrue(databaseHandler.getPayments(paymentName).size() == 1);
        assertTrue(databaseHandler.getPayments(paymentName2).size() == 0);
        onData(is(paymentName)).check(matches(isCompletelyDisplayed()));
    }/*

    @Test
    private void removePaymentCash() {

        assertTrue(databaseHandler.getPayments("Cash").size() == 1);

        onView(withId(R.id.navigation_settings)).perform(click());
        onView(withId(R.id.accounts_settings_button)).perform(click());

        onData(is("Cash")).check(matches(isCompletelyDisplayed()));
        onData(is("Cash")).perform(click());
        // can still be clicked on
        onData(is("Cash")).check(matches(isCompletelyDisplayed()));
        onData(is("Cash")).perform(click());
    }*/
}
