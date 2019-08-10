package com.jenway.slidenewslist.dagger;

import com.jenway.slidenewslist.nestedNews.NestedNewsListFragment;

import dagger.Component;

@MainScope
@Component(dependencies = AppComponent.class, modules = NestedListInfoModule.class)
public interface NestedListInfoComponent {
    void inject(NestedNewsListFragment fragment);
}
