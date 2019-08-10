package com.jenway.slidenewslist.model;

import android.os.Parcel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BaseEntityTest {

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
        BaseEntity createdFromParcel = BaseEntity.CREATOR.createFromParcel(parcel);
        assertEquals(createdFromParcel.getIndex() == newsEntity0.getIndex(), true);
        parcel.recycle();
    }
}