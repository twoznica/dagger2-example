package net.pawelhajduk.daggerdemo;

import com.novoda.rxmocks.RxMatcher;
import com.novoda.rxpresso.RxPresso;

import net.pawelhajduk.daggerdemo.api.model.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Subscriber;

public class Requests {
    //mocked requests below

    public static void listRepos(String companyName, final List<Repository> list) {
        RxPresso rxPresso = BaseTest.getRxPresso();
        rxPresso.given(BaseTest.getTestComponent().getGithubService().listRepos(companyName))
                .withEventsFrom(Observable.create(new Observable.OnSubscribe<List<Repository>>() {
                                    @Override
                                    public void call(Subscriber<? super List<Repository>> subscriber) {
                                        subscriber.onNext(list);
                                        subscriber.onCompleted();
                                    }
                                })
                                                  .delay(3,TimeUnit.SECONDS)
                )
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
