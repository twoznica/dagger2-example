package net.pawelhajduk.daggerdemo;

import android.app.Application;
import android.content.SharedPreferences;

import net.pawelhajduk.daggerdemo.api.GitHubService;

public interface DaggerDemoGraph {
    void inject(MainActivity mainActivity);

    void inject(RepositoriesListActivity repositoriesListActivity);

    GitHubService getGithubService();

    SharedPreferences getSharedPreferences();
}
