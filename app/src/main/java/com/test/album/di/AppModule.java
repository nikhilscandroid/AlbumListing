package com.test.album.di;

import android.content.Context;

import com.test.album.app.AlbumApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */

@Module
public class AppModule {
    AlbumApplication mApplication;

    public AppModule(AlbumApplication application) {
        mApplication = application;
    }

    @Provides
    public Context getAppContext() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    AlbumApplication providesApplication() {
        return mApplication;
    }
}
