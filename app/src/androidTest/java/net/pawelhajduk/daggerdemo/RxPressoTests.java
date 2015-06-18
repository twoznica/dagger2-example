package net.pawelhajduk.daggerdemo;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RxPressoTests extends BaseTest {

    @Test
    public void mockedTest() throws Exception {
        onView(withId(R.id.goToRepositoriesList)).perform(click());

        //Given request "listRepos" with parameter "FutureProcessing" return MOCKED_LIST and wait for result.
        listRepos("FutureProcessing", Mocks.MOCKED_LIST);

        //Standard Espresso code - then Verify if elements are displayed (TODO rethink if Page Object could be used here.)
        onView(withText("element 0")).check(matches(isDisplayed()));
        onView(withText("element 1")).check(matches(isDisplayed()));
        onView(withText("element 2")).check(matches(isDisplayed()));
    }
}