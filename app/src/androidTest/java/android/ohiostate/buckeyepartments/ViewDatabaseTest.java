package android.ohiostate.buckeyepartments;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ViewDatabaseTest {
    @Rule
    public ActivityScenarioRule<viewDatabaseActivity> activityActivityTestRule = new ActivityScenarioRule<>(viewDatabaseActivity.class);

    @Test
    public void checkListVisibility() {
        onView(withId(R.id.recycler_view)).check(matches((isDisplayed())));
    }
}