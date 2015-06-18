package net.pawelhajduk.daggerdemo;



import com.novoda.rxmocks.RxMocks;

import net.pawelhajduk.daggerdemo.api.DebugApiModule;
import net.pawelhajduk.daggerdemo.api.GitHubService;
import net.pawelhajduk.daggerdemo.api.MockGitHubService;
import net.pawelhajduk.daggerdemo.data.UseMockBackend;
import net.pawelhajduk.daggerdemo.data.prefs.BooleanPreference;

import dagger.Module;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;

@Module
public class TestApiModule extends DebugApiModule {

    @Override
    protected GitHubService provideGitHubService(RestAdapter restAdapter,
                                                 MockRestAdapter mockRestAdapter,
                                                 MockGitHubService mockService,
                                                 @UseMockBackend BooleanPreference useMockMode) {
        return RxMocks.mock(GitHubService.class);
    }
}
