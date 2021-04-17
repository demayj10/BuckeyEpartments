package android.ohiostate.buckeyepartments;

import android.widget.PopupMenu;

import androidx.test.espresso.Root;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class ViewMapTest {
    @Rule
    public ActivityScenarioRule<viewMapActivity> activityActivityTestRule = new ActivityScenarioRule<>(viewMapActivity.class);

    @Test
    public void checkListVisibility() {
        onView(withText("Unlimited")).check(doesNotExist());
        onView(withId(R.id.radius_button)).perform(click());
        onView(withText("Unlimited")).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
    }
}