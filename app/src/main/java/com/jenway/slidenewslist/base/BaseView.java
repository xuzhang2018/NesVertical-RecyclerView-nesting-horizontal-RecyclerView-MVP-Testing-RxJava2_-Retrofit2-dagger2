package com.jenway.slidenewslist.base;

public interface BaseView {
    void showLoadingDialog(String msg);

    void dismissLoadingDialog();

    void showToast(String message);
}
