package com.jenway.slidenewslist.nestedNews.mvp;

import com.jenway.slidenewslist.base.BasePresenter;
import com.jenway.slidenewslist.base.BaseView;
import com.jenway.slidenewslist.model.BaseEntity;

import java.util.ArrayList;

public class NestedNewsListContract {
    public interface View extends BaseView {
        void setRecyclerView(ArrayList<BaseEntity> data);

        void setTheRecyclerViewPosition();

        void onLoadInfoSuccess();

        void onLoadInfoFail(String errorMsg);
    }

    public interface Presenter extends BasePresenter<Presenter, View> {
        void loadNewsListData();

        void loadImage();

    }
}
