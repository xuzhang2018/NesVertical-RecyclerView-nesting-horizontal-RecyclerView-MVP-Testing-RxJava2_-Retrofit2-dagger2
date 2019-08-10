package com.jenway.slidenewslist.dagger;

import com.jenway.slidenewslist.nestedNews.mvp.NestedNewsListContract;
import com.jenway.slidenewslist.nestedNews.mvp.NestedNewsListPresenterImp;

import dagger.Module;
import dagger.Provides;

@Module
public class NestedListInfoModule {
    private NestedNewsListContract.View mView;

    public NestedListInfoModule(NestedNewsListContract.View mView) {
        this.mView = mView;
    }

    @MainScope
    @Provides
    public NestedNewsListPresenterImp provideNestedListInfoPresenterImp() {
        NestedNewsListPresenterImp imp = new NestedNewsListPresenterImp();
        imp.attachView(mView);
        return imp;
    }
}
