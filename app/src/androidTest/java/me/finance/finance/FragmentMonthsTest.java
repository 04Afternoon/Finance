package me.finance.finance;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.finance.finance.Model.Sort;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class FragmentMonthsTest {


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

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testAllMonthItems() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }

    @Test
    public void testCalendarDialogCancle() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.calender_button)).perform(click());
        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }

    @Test
    @Ignore //TODO: fix tests
    public void testCalendarDialog() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.calender_button)).perform(click());
        onView(withText("May 2018")).perform(scrollTo());
        onView(FinanceMatchers.withIndex(withText("30"), 2)).perform();
        onView(FinanceMatchers.withIndex(withText("2"), 1)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(3)));
    }

    @Test
    public void testSortDialogCancel() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.months_search_button)).perform(click());
        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }

    @Test
    public void testSortDialogOKWithoutChange() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.months_search_button)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }

    @Test
    public void testSortDialogOKWithChange() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.months_search_button)).perform(click());
        onView(withId(R.id.sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(Sort.class)),
                is(new Sort(Sort.Column.VALUE, Sort.Order.ASC))))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }


    @Test
    public void testSortDialogOK2WithChange() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.months_search_button)).perform(click());



        onView(withId(R.id.sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(Sort.class)),
                is(new Sort(Sort.Column.VALUE, Sort.Order.ASC))))
                .inRoot(isPlatformPopup())
                .perform(click());



        onView(withId(R.id.filterCategorieSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("All categories")))
                .inRoot(isPlatformPopup())
                .perform(click());


        onView(withId(R.id.filterPaymentOptionSpinner)).perform(click());

        onData(allOf(is(instanceOf(String.class)),
                is("All payment options")))
                .inRoot(isPlatformPopup())
                .perform(click());


        onView(withText("OK")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }

    @Test
    public void testFilterDialogOKWithoutChange() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.months_search_button)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }

    @Test
    public void testFilterDialogOKWithoutChangeCancel() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.months_search_button)).perform(click());
        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }

    @Test
    public void testFilterDialogOKWithChange() {
        onView(withId(R.id.navigation_months)).perform(click());
        onView(withId(R.id.months_search_button)).perform(click());

        onView(withId(R.id.filterCategorieSpinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)),
        (is("All categories")))).inRoot(isPlatformPopup())
                .perform(click());

        onView(withText("OK")).perform(click());
        onView(withId(R.id.monthly_list)).check(matches(FinanceMatchers.withListSize(2)));
    }


    static class FinanceMatchers {

        public static Matcher<Object> withSort(final Sort actual) {
            return new BaseMatcher<Object>() {
                @Override
                public void describeTo(Description description) {
                    description.appendText("Element should be " + actual);
                }

                @Override
                public boolean matches(Object item) {
                    return actual.equals(item);
                }
            };
        }

        public static Matcher<View> withListSize(final int size) {
            return new TypeSafeMatcher<View>() {
                int actualcount;

                @Override
                public boolean matchesSafely(final View view) {
                    actualcount = ((ListView) view).getCount();
                    return actualcount == size;
                }

                @Override
                public void describeTo(final Description description) {
                    description.appendText("ListView should have " + size + " items, but has " + actualcount);
                }
            };
        }

        public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
            return new TypeSafeMatcher<View>() {
                int currentIndex = 0;

                @Override
                public void describeTo(Description description) {
                    description.appendText("with index: ");
                    description.appendValue(index);
                    matcher.describeTo(description);
                }

                @Override
                public boolean matchesSafely(View view) {
                    return matcher.matches(view) && currentIndex++ == index;
                }
            };
        }
    }

}