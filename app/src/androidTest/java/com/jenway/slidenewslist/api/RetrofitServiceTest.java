package com.jenway.slidenewslist.api;

import android.graphics.Bitmap;

import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.network.NetUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.observers.TestObserver;
import okhttp3.ResponseBody;

import static org.junit.Assert.assertEquals;

/**
 * by Xu
 * test the retrofit service
 * Only load the local data when the debug mod is on
 */
public class RetrofitServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoadNewsList() throws Exception {
        RetrofitService.mockDataName = "news.json";
        TestObserver<ArrayList<BaseEntity>> testObserver = new TestObserver<>();
        RetrofitService.getData(15).subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        ArrayList<BaseEntity> data = testObserver.values().get(0);
        assertEquals(data.size(), 15);
    }

    @Test
    public void testLoadBreedImage() throws Exception {
        RetrofitService.mockDataName = "Bitmap";
        TestObserver<ResponseBody> testObserver = new TestObserver<>();
        RetrofitService.baseApi.getNewsImage("https://imageca.thestartmagazine.com/fetch/d_magazineDefault.jpg,c_fill,g_face:auto,fl_lossy,q_70,w_270,h_190/https%3A%2F%2Fcdn.cnn.com%2Fcnnnext%2Fdam%2Fassets%2F190628173546-child-detention-center-video-synd-2.jpg")
                .subscribe(testObserver);
        testObserver.awaitTerminalEvent();
        ResponseBody data = testObserver.values().get(0);
        assertEquals(data.contentType().toString(), "image/jpeg");
        Bitmap image = NetUtil.getBitmapFromAssets("test.jpg");
        assertEquals(data.contentLength(), NetUtil.getBitmapByte(image).length);
    }
}