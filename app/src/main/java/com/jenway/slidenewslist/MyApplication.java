package com.jenway.slidenewslist;

import android.app.Application;

import com.jenway.slidenewslist.api.RetrofitService;
import com.jenway.slidenewslist.dagger.AppComponent;
import com.jenway.slidenewslist.dagger.AppModule;
import com.jenway.slidenewslist.dagger.DaggerAppComponent;

public class MyApplication extends Application {

    /**
     * The constant DEBUG.
     */
    public static boolean DEBUG = false;

    private static MyApplication singleton;
    private static AppComponent appComponent;

    public static MyApplication getInstance() {
        return singleton;
    }
    //private RefWatcher mRefWatcher;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        if (BuildConfig.DEBUG) {
            DEBUG = true;
        }
        InitInjector();
        InitConfig();

    }

    /**
     * init the injection
     */
    private void InitInjector() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void InitConfig() {
        //init the LeakCanary
        //mRefWatcher = LeakCanary.install(this);
        //init the retrofit
        RetrofitService.init();
    }
    /**
     * monitor the memory
     */
//    public void mustDie(Object object) {
//        if (mRefWatcher != null) {
//            mRefWatcher.watch(object);
//        }
//    }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//    }
}