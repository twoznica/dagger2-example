package net.pawelhajduk.daggerdemo;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import net.pawelhajduk.daggerdemo.api.GitHubService;
import net.pawelhajduk.daggerdemo.api.model.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RepositoriesListActivity extends ListActivity {

    @Inject
    GitHubService github;
    int wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerDemoApplication.component().inject(this);
        Bundle bundle = getIntent().getExtras();
        wait = bundle.getInt("wait", 0);
            List<String> list = bundle.getStringArrayList("key");
            if(list.size() != 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RepositoriesListActivity.this, android.R.layout.simple_list_item_1, list);
                setListAdapter(adapter);
            } else {
                loadData();
            }
    }

    private void loadData() {
        github.listRepos("FutureProcessing")
              .delay(wait, TimeUnit.SECONDS)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(repositories -> {
                  List<String> names = new ArrayList<>();
                  for(Repository repository : repositories) {
                      names.add(repository.name);
                  }
                  ArrayAdapter<String> adapter = new ArrayAdapter<>(RepositoriesListActivity.this, android.R.layout.simple_list_item_1, names);
                  setListAdapter(adapter);
              }, new Action1<Throwable>() {
                  @Override
                  public void call(Throwable throwable) {
                      Log.e("RepoListActivity", "Error receiving list of repos", throwable);
                  }
              });
    }
}
