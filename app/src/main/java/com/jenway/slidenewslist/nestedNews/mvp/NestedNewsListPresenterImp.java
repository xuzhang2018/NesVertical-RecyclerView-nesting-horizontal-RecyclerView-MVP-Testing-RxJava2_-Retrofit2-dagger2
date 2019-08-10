package com.jenway.slidenewslist.nestedNews.mvp;

import com.jenway.slidenewslist.api.RetrofitService;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.model.Config;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class NestedNewsListPresenterImp implements NestedNewsListContract.Presenter {

    private NestedNewsListContract.View mView;
    private ArrayList<BaseEntity> baseData = new ArrayList<>();
    private ArrayList<BaseEntity> data = new ArrayList<>();
    //collect the disposable
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ArrayList<BaseEntity> getData() {
        return data;
    }

    public void setData(ArrayList<BaseEntity> data) {
        this.data = data;
    }

    //for test only
    public void setBaseData(ArrayList<BaseEntity> baseData) {
        this.baseData = baseData;
    }

    @Override
    public void loadNewsListData() {
        mView.showLoadingDialog("Loading the News List");
        RetrofitService.getData(Config.maxloadingNewSCount).subscribe(new Observer<ArrayList<BaseEntity>>() {
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(ArrayList<BaseEntity> baseEntities) {
                //stop if has been disposed
                if (disposable.isDisposed()) {

                } else {
                    baseData = baseEntities;
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.onLoadInfoFail(e.getMessage());
            }

            @Override
            public void onComplete() {
                mView.onLoadInfoSuccess();
                loadImage();
            }
        });
    }

    @Override
    public void loadImage() {
        mView.showLoadingDialog("Loading the News Images");
        RetrofitService.getNewsImage(baseData).subscribe(new SingleObserver<ArrayList<BaseEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(ArrayList<BaseEntity> baseEntities) {
                data = baseEntities;
                mView.setRecyclerView(data);
                mView.onLoadInfoSuccess();
            }

            @Override
            public void onError(Throwable e) {
                mView.onLoadInfoFail(e.getMessage());
            }
        });
    }

    @Override
    public NestedNewsListContract.Presenter attachView(NestedNewsListContract.View view) {
        mView = view;
        return this;
    }

    //Prevent memory leaks
    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
