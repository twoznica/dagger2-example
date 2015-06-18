package net.pawelhajduk.daggerdemo;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.novoda.rxmocks.RxMatcher;
import com.novoda.rxpresso.RxPresso;

import net.pawelhajduk.daggerdemo.api.model.Repository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import rx.Notification;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RxPressoTests {

    private RxPresso rxPresso;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            DaggerDemoApplication globalApplication = (DaggerDemoApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
            testComponent = DaggerDaggerDemoComponent.builder()
                                                     .debugApiModule(new TestApiModule())
                                                     .mainModule(new MainModule(globalApplication))
                                                     .build();
            DaggerDemoApplication.setGraph(testComponent);
            rxPresso = new RxPresso(testComponent.getGithubService());
            Espresso.registerIdlingResources(rxPresso);
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            Espresso.unregisterIdlingResources(rxPresso);
            rxPresso.resetMocks();
        }
    };

    List<Repository> test = Arrays.asList(new Repository("testtrzy", 3, 3), new Repository("testcztery", 3, 3));
    private DaggerDemoComponent testComponent;

    @Test
    public void mockedTest() throws Exception {
        onView(withId(R.id.goToRepositoriesList)).perform(click());
        rxPresso.given(testComponent.getGithubService().listRepos("FutureProcessing"))
                .withEventsFrom(Observable.just(test))
                .expect(new RxMatcher<Notification<List<Repository>>>() {
                    @Override
                    public boolean matches(Notification<List<Repository>> actual) {
                        return actual.getValue() == test;
                    }

                    @Override
                    public String description() {
                        return null;
                    }
                })
                .thenOnView(withText("testtrzy")).check(matches(isDisplayed()));
    }

    //    @Test
    //    public void normalTest() throws Exception {
    //        onView(withId(R.id.goToRepositoriesList)).perform(click());
    //        onView(withText("HazelcastShowAndTell")).check(matches(isDisplayed()));
    //        onView(withText("Mock repository 1")).check(matches(isDisplayed()));
    //    }
}