package me.finance.finance;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import me.finance.finance.Model.Intake;

@RunWith(AndroidJUnit4.class)
public class InOutPermsTest {

    private DatabaseHandler databaseHandler;
    private String name = "test intake";
    private String value_string = "12.34";
    private double value = Double.valueOf(value_string);
    private String startDate = "2000-01-01";
    private String paymentMethod = "Bar";
    private String invalidStartDate = "2000-25-05";

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

    @Rule
    public ActivityTestRule<InOutPermsActivity> activityRule = new ActivityTestRule<>(InOutPermsActivity.class);

    @Test
    public void testValidAddIntakeGUI1() {
        onView(withId(R.id.name_text_field)).perform(typeText(name));
        onView(withId(R.id.value_text_field)).perform(typeText(value_string));
        onView(withId(R.id.start_text_field)).perform(typeText(startDate));
        onView(withId(R.id.payment_text_field)).perform(typeText(paymentMethod), closeSoftKeyboard());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getPayment_opt() == 0) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testInvalidAddIntakeGUI1() {
        onView(withId(R.id.name_text_field)).perform(typeText(""));
        onView(withId(R.id.value_text_field)).perform(typeText(value_string));
        onView(withId(R.id.start_text_field)).perform(typeText(startDate));
        onView(withId(R.id.payment_text_field)).perform(typeText(paymentMethod), closeSoftKeyboard());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals("") && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getPayment_opt() == 0) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI2() {
        onView(withId(R.id.name_text_field)).perform(typeText(name));
        onView(withId(R.id.value_text_field)).perform(typeText(""));
        onView(withId(R.id.start_text_field)).perform(typeText(startDate));
        onView(withId(R.id.payment_text_field)).perform(typeText(paymentMethod), closeSoftKeyboard());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getDateFormatted().equals(startDate) && intake.getPayment_opt() == 0) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI3() {
        onView(withId(R.id.name_text_field)).perform(typeText(name));
        onView(withId(R.id.value_text_field)).perform(typeText(value_string));
        onView(withId(R.id.start_text_field)).perform(typeText(""));
        onView(withId(R.id.payment_text_field)).perform(typeText(paymentMethod), closeSoftKeyboard());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getValue() == value && intake.getDateFormatted().equals("") && intake.getPayment_opt() == 0) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI4() {
        onView(withId(R.id.name_text_field)).perform(typeText(name));
        onView(withId(R.id.value_text_field)).perform(typeText(value_string));
        onView(withId(R.id.start_text_field)).perform(typeText(startDate));
        onView(withId(R.id.payment_text_field)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getPayment_opt() == 0) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testInvalidAddIntakeGUI5() {
        onView(withId(R.id.name_text_field)).perform(typeText(name));
        onView(withId(R.id.value_text_field)).perform(typeText(value_string));
        onView(withId(R.id.start_text_field)).perform(typeText(invalidStartDate));
        onView(withId(R.id.payment_text_field)).perform(typeText(paymentMethod), closeSoftKeyboard());
        onView(withId(R.id.auftrag_finish_button)).perform(click());

        boolean found = false;
        for (Intake intake : databaseHandler.getIntakes()) {
            if (intake.getName().equals(name) && intake.getValue() == value && intake.getDateFormatted().equals(startDate) && intake.getPayment_opt() == 0) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }
}
