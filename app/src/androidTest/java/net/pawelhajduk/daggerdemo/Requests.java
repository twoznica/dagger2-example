package net.pawelhajduk.daggerdemo;

import com.novoda.rxmocks.RxMatcher;
import com.novoda.rxpresso.RxPresso;

import net.pawelhajduk.daggerdemo.api.model.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;

public class Requests {
    //mocked requests below
    //this one returns given list with 2 seconds delay (to test if the test waits for the result)


    public static void listReposWithTwoSecsDelay(String companyName, final List<Repository> list) {
        RxPresso rxPresso = BaseTest.getRxPresso();
        rxPresso.given(BaseTest.getTestComponent().getGithubService().listRepos(companyName))
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

    public static void listRepos(String companyName, final List<Repository> list) {
        RxPresso rxPresso = BaseTest.getRxPresso();
        rxPresso.given(BaseTest.getTestComponent().getGithubService().listRepos(companyName))
                .withEventsFrom(Observable.just(list))
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
