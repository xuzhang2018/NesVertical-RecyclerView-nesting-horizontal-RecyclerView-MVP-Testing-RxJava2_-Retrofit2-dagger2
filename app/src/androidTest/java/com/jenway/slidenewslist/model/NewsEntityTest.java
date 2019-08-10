package com.jenway.slidenewslist.model;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.jenway.slidenewslist.network.NetUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class NewsEntityTest {

    private NewsEntity newsEntity0;
    private NewsEntity newsEntity1;
    private NewsEntity newsEntity2;
    private NewsEntity newsEntity3;

    @Before
    public void setUp() throws Exception {
        newsEntity0 = new NewsEntity(0, "test title 0", "test summary 0", "url 0");
        newsEntity1 = new NewsEntity(1, "test title 1", "test summary 1", "url 1");
        newsEntity2 = new NewsEntity(2, "test title 2", "test summary 2", "url 2");
        newsEntity3 = new NewsEntity(3, "test title 3", "test summary 3", "url 3");
        //
        newsEntity0.getHorizontalNews().add(newsEntity1);
        newsEntity0.getHorizontalNews().add(newsEntity2);
        newsEntity0.getHorizontalNews().add(newsEntity3);
        Bitmap bm = NetUtil.getBitmapFromAssets("test.jpg");
        newsEntity0.setImage(bm);
    }

    @After
    public void tearDown() throws Exception {
        newsEntity0 = null;
        newsEntity1 = null;
        newsEntity2 = null;
        newsEntity3 = null;
    }


    /**
     * Test parcelable.
     */
    @Test
    public void test_parcelable() throws Exception {
        Parcel parcel = Parcel.obtain();
        newsEntity0.writeToParcel(parcel, newsEntity0.describeContents());
        parcel.setDataPosition(0);
        NewsEntity createdFromParcel = NewsEntity.CREATOR.createFromParcel(parcel);
        assertEquals(createdFromParcel.getIndex() == newsEntity0.getIndex(), true);
        assertEquals(createdFromParcel.getTitle().equals(newsEntity0.getTitle()), true);
        assertEquals(createdFromParcel.getSummary().equals(newsEntity0.getSummary()), true);
        assertEquals(createdFromParcel.getImageUrl().equals(newsEntity0.getImageUrl()), true);
        assertEquals(createdFromParcel.getHorizontalNews().size() == newsEntity0.getHorizontalNews().size(), true);
        assertEquals(createdFromParcel.getHorizontalNews().get(0).getIndex() == newsEntity0.getHorizontalNews().get(0).getIndex(), true);
        assertEquals(createdFromParcel.getImage().sameAs(newsEntity0.getImage()), true);
        parcel.recycle();
    }
}