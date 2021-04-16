package android.ohiostate.buckeyepartments;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checkButtonVisibility() {
        onView(withId(R.id.db_button)).check(matches((isDisplayed())));
        onView(withId(R.id.map_button)).check(matches(isDisplayed()));
    }

    @Test
    public void checkButtonContent() {
        onView(withId(R.id.map_button)).check(matches(withText("View Map")));
    }
}