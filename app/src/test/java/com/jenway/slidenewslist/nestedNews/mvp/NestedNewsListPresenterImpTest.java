package com.jenway.slidenewslist.nestedNews.mvp;

import android.graphics.Bitmap;

import com.jenway.slidenewslist.api.RetrofitService;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.model.Config;
import com.jenway.slidenewslist.model.NewsEntity;
import com.jenway.slidenewslist.network.NetUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * by Xu
 * Description: only test the MVP functions, the other function test is in Android Test folder
 */

@PrepareForTest({NestedNewsListPresenterImp.class, RetrofitService.class})
@RunWith(PowerMockRunner.class)
public class NestedNewsListPresenterImpTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    private NestedNewsListContract.View view;
    private NestedNewsListPresenterImp presenterImp;
    private NestedNewsListPresenterImp presenter;
    //
    private ArrayList<BaseEntity> baseData;
    private ArrayList<BaseEntity> data;
    private ArrayList<Bitmap> bitmaps;


    @Before
    public void setUp() throws Exception {
        NewsEntity entity1 = new NewsEntity(0, "name0", "summary0", "url0");
        NewsEntity entity2 = new NewsEntity(1, "name1", "summary1", "url1");
        NewsEntity entity3 = new NewsEntity(2, "name2", "summary2", "url2");
        NewsEntity entity4 = new NewsEntity(3, "name3", "summary3", "url3");
        NewsEntity entity5 = new NewsEntity(4, "name4", "summary4", "url4");
        NewsEntity entity6 = new NewsEntity(5, "name5", "summary5", "url5");
        NewsEntity entity7 = new NewsEntity(6, "name6", "summary6", "url6");
        NewsEntity entity8 = new NewsEntity(7, "name7", "summary7", "url7");
        NewsEntity entity9 = new NewsEntity(8, "name8", "summary8", "url8");
        NewsEntity entity10 = new NewsEntity(9, "name9", "summary9", "url9");

        baseData = new ArrayList<>();
        baseData.add(entity1);
        baseData.add(entity2);
        baseData.add(entity3);
        baseData.add(entity4);
        baseData.add(entity5);
        baseData.add(entity6);
        baseData.add(entity7);
        baseData.add(entity8);
        baseData.add(entity9);
        baseData.add(entity10);

        Bitmap bitmap = PowerMockito.mock(Bitmap.class);
        bitmaps = new ArrayList<>();
        for (int i = 0; i < baseData.size(); i++) {
            bitmaps.add(bitmap);
        }

        data = NetUtil.covertBaseDataToListData(baseData, bitmaps);
        //
        view = mock(NestedNewsListContract.View.class);
        presenterImp = new NestedNewsListPresenterImp();
        presenterImp.attachView(view);
        //presenterImp.setBaseData(baseData);
        presenter = PowerMockito.spy(presenterImp);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_loadListBaseData_onSuccess() throws Exception {
        PowerMockito.mockStatic(RetrofitService.class);
        PowerMockito.when(RetrofitService.getData(Config.maxloadingNewSCount)).thenReturn(Observable.just(baseData));
        PowerMockito.doCallRealMethod().when(presenter).loadNewsListData();
        //
        PowerMockito.when(RetrofitService.getNewsImage(baseData)).thenReturn(Single.just(data));
        PowerMockito.doCallRealMethod().when(presenter).loadImage();
        //
        presenter.loadNewsListData();
        verify(view).setRecyclerView((ArrayList<BaseEntity>) any());

    }

    @Test
    public void test_loadBreedListData_onError() throws Exception {
        PowerMockito.mockStatic(RetrofitService.class);
        Exception e = new Exception();
        PowerMockito.when(RetrofitService.getData(Config.maxloadingNewSCount)).thenReturn(Observable.<ArrayList<BaseEntity>>error(e));
        presenter.loadNewsListData();
        verify(view).showLoadingDialog(anyString());
        verify(view).onLoadInfoFail(e.getMessage());

    }

    @Test
    public void test_onLoadedImageData_onSuccess() throws Exception {
        PowerMockito.mockStatic(RetrofitService.class);
        PowerMockito.when(RetrofitService.getNewsImage(baseData)).thenReturn(Single.just(data));
        PowerMockito.doCallRealMethod().when(presenter).loadImage();
        presenter.setBaseData(baseData);
        presenter.loadImage();
        verify(view).showLoadingDialog(anyString());
        verify(view).setRecyclerView((ArrayList<BaseEntity>) any());
        verify(view).onLoadInfoSuccess();
    }

    @Test
    public void test_onLoadedImageData_onError() throws Exception {
        PowerMockito.mockStatic(RetrofitService.class);
        Exception e = new Exception();
        PowerMockito.when(RetrofitService.getNewsImage(baseData)).thenReturn(Single.<ArrayList<BaseEntity>>error(e));
        PowerMockito.doCallRealMethod().when(presenter).loadImage();
        presenter.setBaseData(baseData);
        presenter.loadImage();
        verify(view).showLoadingDialog(anyString());
        verify(view).onLoadInfoFail(e.getMessage());
    }
}