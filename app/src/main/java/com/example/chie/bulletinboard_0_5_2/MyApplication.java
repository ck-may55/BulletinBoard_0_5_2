package com.example.chie.bulletinboard_0_5_2;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by chie on 2017/04/01.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
