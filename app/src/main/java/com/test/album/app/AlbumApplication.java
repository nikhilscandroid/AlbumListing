package com.test.album.app;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.test.album.BuildConfig;
import com.test.album.di.AppComponent;
import com.test.album.di.AppModule;
import com.test.album.di.DaggerAppComponent;
import com.test.album.di.RetrofitModule;
import com.test.album.di.RoomDbModule;

import timber.log.Timber;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
public class AlbumApplication extends Application {

    private AppComponent albumViewModelComponent;
    private static Context context;


    private static AlbumApplication myApplication;

    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        myApplication = this;
        AlbumApplication.context = getApplicationContext();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            initializeStetho();
        }
        initDagger();
    }

    public void initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void initDagger() {

        albumViewModelComponent = DaggerAppComponent.builder()
                .retrofitModule(new RetrofitModule())
                .appModule(new AppModule(this))
                .roomDbModule(new RoomDbModule(this))
                .build();
    }

    public AppComponent getAlbumViewModelComponent() {
        return albumViewModelComponent;
    }

    public static AlbumApplication getMyApplication() {
        return myApplication;
    }

    public static Context getAppContext() {
        return AlbumApplication.context;
    }
}
