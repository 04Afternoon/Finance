package me.finance.finance;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.onData;

import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;

@RunWith(AndroidJUnit4.class)
public class InOutPermsTest {

    private static DatabaseHandler databaseHandler;
    private String name = "test intake";
    private String value_string = "12.34";
    private double value = Double.valueOf(value_string);
    private String startDate = "2000-01-01";
    private String paymentMethod = "Bar";
    private String invalidStartDate = "2000-25-05";
    private static Payment payment;

    @BeforeClass
    public static void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();

        databaseHandler = DatabaseHandler.getInstance(context);
        databaseHandler.open();
        databaseHandler.createTables();
        databaseHandler.deleteTableContents();
        payment = new Payment("Cash");
        payment.setId((int)databaseHandler.addPayment(payment));
    }

    @Before
    public void resetDb() {
        databaseHandler.deleteIntakes();
    }

    @AfterClass
    public static void closeDb() {
        databaseHandler.close();
    }

    @Rule
    public ActivityTestRule<InOutPermsActivity> activityRule = new ActivityTestRule<>(InOutPermsActivity.class);

    @Test
    public void testValidAddIntakeGUI1() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.value_text_field)).perform(typeText(value_string), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(typeText(startDate), closeSoftKeyboard());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(payment.getName()))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("no category"))).perform(click());
        List<Payment> payments = databaseHandler.getPayments();
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        List<Intake> intakes = databaseHandler.getIntakes();
        for (Intake intake : intakes) {
            if (intake.getName().equals(name) && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getCategory() == null && intake.getPayment_opt() == payment.getId()) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testInvalidAddIntakeGUI1() {
        onView(withId(R.id.name_text_field)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.value_text_field)).perform(typeText(value_string), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(typeText(startDate), closeSoftKeyboard());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(payment.getName()))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("no category"))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals("") && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getCategory() == null && intake.getPayment_opt() == payment.getId()) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI2() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.value_text_field)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(typeText(startDate), closeSoftKeyboard());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(payment.getName()))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("no category"))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getDateFormatted().equals(startDate) && intake.getCategory() == null && intake.getPayment_opt() == payment.getId()) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI3() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.value_text_field)).perform(typeText(value_string), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(payment.getName()))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("no category"))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getValue() == value && intake.getDateFormatted().equals("") && intake.getCategory() == null && intake.getPayment_opt() == payment.getId()) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI4() {
        onView(withId(R.id.name_text_field)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.value_text_field)).perform(typeText(value_string), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(typeText(startDate), closeSoftKeyboard());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(payment.getName()))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("no category"))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals("") && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getCategory() == null && intake.getPayment_opt() == payment.getId()) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI5() {
        onView(withId(R.id.name_text_field)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.value_text_field)).perform(typeText(value_string), closeSoftKeyboard());
        onView(withId(R.id.start_text_field)).perform(typeText(invalidStartDate), closeSoftKeyboard());
        onView(withId(R.id.payment_opt_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(payment.getName()))).perform(click());
        onView(withId(R.id.category_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("no category"))).perform(click());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getCategory() == null && intake.getPayment_opt() == payment.getId()) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }
}
