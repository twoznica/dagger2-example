package net.pawelhajduk.daggerdemo.TestModules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import net.pawelhajduk.daggerdemo.data.DataModule;

public class TestDataModule extends DataModule {

    @Override
    protected SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("daggertestdemo", Context.MODE_PRIVATE);
    }



}
