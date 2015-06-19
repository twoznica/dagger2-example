package net.pawelhajduk.daggerdemo.Tests;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import net.pawelhajduk.daggerdemo.BaseTest;
import net.pawelhajduk.daggerdemo.Mocks;
import net.pawelhajduk.daggerdemo.R;
import net.pawelhajduk.daggerdemo.Requests;

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
    public void mockedTestWithDelay() throws Exception {
        onView(ViewMatchers.withId(R.id.goToRepositoriesList)).perform(click());

        //Given request "listReposWithTwoSecsDelay" with parameter "FutureProcessing" return MOCKED_LIST and wait for result.
        Requests.listReposWithTwoSecsDelay("FutureProcessing", Mocks.MOCKED_LIST);

        //Standard Espresso code - then Verify if elements are displayed (TODO rethink if Page Object could be used here.)
        checkElements();
    }

    @Test
    public void mockedTest() throws Exception {
        onView(withId(R.id.goToRepositoriesList)).perform(click());
        Requests.listRepos("FutureProcessing", Mocks.MOCKED_LIST);
        checkElements();
    }

    @Test
    public void mockedTestWithMockingSameRequestTwice() throws Exception {
        onView(withId(R.id.goToRepositoriesList)).perform(click());
        Requests.listReposWithTwoSecsDelay("FutureProcessing", Mocks.MOCKED_LIST);
        checkElements();

        //Repeat the test with different mock
        Espresso.pressBack();
        rebindMocks();
        onView(withId(R.id.goToRepositoriesList)).perform(click());
        Requests.listRepos("FutureProcessing", Mocks.MOCKED_LIST);
        checkElements();

    }

    @Test
    public void mockedTestScrewingPrefs() throws Exception {
        onView(withId(R.id.goToRepositoriesList)).perform(click());
        Requests.listRepos("FutureProcessing", Mocks.MOCKED_LIST);
        checkElements();
        //Try to screw the prefs
        Espresso.pressBack();
        onView(withId(R.id.main_checkbox)).perform(click());
    }

    private void checkElements() {
        onView(withText(Mocks.MOCKED_LIST.get(0).name)).check(matches(isDisplayed()));
        onView(withText("element 1")).check(matches(isDisplayed()));
        onView(withText("element 2")).check(matches(isDisplayed()));
        onView(withText("element 3")).check(matches(isDisplayed()));
    }
}