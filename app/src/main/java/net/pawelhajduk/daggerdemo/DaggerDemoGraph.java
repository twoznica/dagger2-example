package net.pawelhajduk.daggerdemo;

import net.pawelhajduk.daggerdemo.api.GitHubService;

public interface DaggerDemoGraph {
    void inject(MainActivity mainActivity);

    void inject(RepositoriesListActivity repositoriesListActivity);

    GitHubService getGithubService();
}
