package com.jenway.slidenewslist.dagger;

import android.content.Context;

import com.jenway.slidenewslist.MyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final MyApplication mApplication;

    public AppModule(MyApplication mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication.getApplicationContext();
    }
}
