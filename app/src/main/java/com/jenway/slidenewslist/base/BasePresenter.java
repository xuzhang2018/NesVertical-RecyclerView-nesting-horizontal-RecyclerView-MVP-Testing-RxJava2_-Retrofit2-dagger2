package com.jenway.slidenewslist.base;

public interface BasePresenter<K extends BasePresenter, T extends BaseView> {
    K attachView(T view);

    void unSubscribe();
}