package net.pawelhajduk.daggerdemo;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.novoda.rxmocks.RxMatcher;
import com.novoda.rxpresso.RxPresso;

import net.pawelhajduk.daggerdemo.api.model.Repository;

import org.junit.Rule;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;

//This is base test class, which contains no tests, but is extended by every other test class
//It contains proper @Rule with setUp and tearDown

public class BaseTest {

    private RxPresso rxPresso;
    private DaggerDemoComponent testComponent;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected void beforeActivityLaunched() {

            //change dagger graph
            DaggerDemoApplication globalApplication = (DaggerDemoApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
            testComponent = DaggerDaggerDemoComponent.builder()
                                                     .debugApiModule(new TestApiModule())
                                                     .mainModule(new MainModule(globalApplication))
                                                     .build();
            DaggerDemoApplication.setGraph(testComponent);

            //initialize RxPresso
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

    //mocked requests below

    //this one returns given list with 2 seconds delay (to test if the test waits for the result)
    //TODO - think if this can be moved somewhere else

    protected void listRepos(String companyName, final List<Repository> list) {
        rxPresso.given(testComponent.getGithubService().listRepos(companyName))
                .withEventsFrom(Observable.just(list).delay(2, TimeUnit.SECONDS))
                .expect(new RxMatcher<Notification<List<Repository>>>() {
                    @Override
                    public boolean matches(Notification<List<Repository>> actual) {
                        return actual.getValue() == list;
                    }

                    @Override
                    public String description() {
                        return null;
                    }
                });
    }
}
