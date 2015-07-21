package net.pawelhajduk.daggerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.pawelhajduk.daggerdemo.api.GitHubService;
import net.pawelhajduk.daggerdemo.api.model.Repository;
import net.pawelhajduk.daggerdemo.data.UseMockBackend;
import net.pawelhajduk.daggerdemo.data.prefs.BooleanPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends ActionBarActivity {
    @InjectView(R.id.main_tv_text)
    TextView text;

    @InjectView(R.id.goToRepositoriesList)
    Button goAndLoad;

    @InjectView(R.id.loadDataThenGo)
    Button loadAndGo;

    @InjectView(R.id.goToRepositoriesListWithWAit)
    Button goAndLoadWithWAit;

    @Inject
    @UseMockBackend
    BooleanPreference useMock;

    @InjectView(R.id.main_checkbox)
    CheckBox checkBox;

    @Inject
    Resources resources;

    @Inject
    GitHubService github;

    @Inject
    SharedPreferences preferences;

    private Subscription subscription;
    private Subscription secondSubscription;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        DaggerDemoApplication.component().inject(this);
        Bundle bundle = new Bundle();

        text.setText(resources.getString(R.string.injected_text));
        checkBox.setChecked(useMock.get());
        loadAndGo.setOnClickListener(v -> subscription = github.listRepos("FutureProcessing")
                             .subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .subscribe(repositories -> {
                                 List<String> names = new ArrayList<>();
                                 for(Repository repository : repositories) {
                                     names.add(repository.name);
                                 }
                                 bundle.putStringArrayList("key", (ArrayList<String>) names);
                                 Intent i = new Intent(MainActivity.this, RepositoriesListActivity.class);
                                 i.putExtras(bundle);
                                 startActivity(i);
                             }));

    }

    @OnCheckedChanged(R.id.main_checkbox)
    public void onRememberMeCheckChanged(CompoundButton button, boolean checked) {
        useMock.set(checked);

        // Need to rebuild graph due to changed setting which directly influences object creation (mock mode)
        DaggerDemoApplication.buildComponentAndInject();
    }




    @OnClick(R.id.goToRepositoriesList)
    public void onGoToRepositoriesListClicked() {
        Intent i = new Intent(MainActivity.this, RepositoriesListActivity.class);
        bundle = new Bundle();
        bundle.putStringArrayList("key", new ArrayList<String>());
        bundle.putInt("wait", 0);
        i.putExtras(bundle);
        startActivity(i);
    }

    @OnClick(R.id.goToRepositoriesListWithWAit)
    public void onGoToRepositoriesListClickedWait() {
        Intent i = new Intent(MainActivity.this, RepositoriesListActivity.class);
        bundle = new Bundle();
        bundle.putStringArrayList("key", new ArrayList<String>());
        bundle.putInt("wait", 5);
        i.putExtras(bundle);
        startActivity(i);
    }


}
