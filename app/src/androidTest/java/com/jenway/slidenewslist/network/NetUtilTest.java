package com.jenway.slidenewslist.network;

import android.graphics.Bitmap;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.network.bean.NetData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static org.junit.Assert.assertEquals;

public class NetUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_getJson() {
        String strJson = NetUtil.getJson("news.json");
        Gson gson = new Gson();
        NetData netData = gson.fromJson(strJson, NetData.class);
        assertEquals(netData.getContent().size(), 15);
    }

    @Test
    public void test_getBitmapFromAssets() {
        Bitmap image = NetUtil.getBitmapFromAssets("test.jpg");
        assertEquals(image.getByteCount(), 205200);
    }

    @Test
    public void test_getBitmapByte() {
        Bitmap image = NetUtil.getBitmapFromAssets("test.jpg");
        byte[] data = NetUtil.getBitmapByte(image);
        assertEquals(data.length, 117161);
    }

    @Test
    public void test_getBitmapFromResponseBody() {
        Bitmap image = NetUtil.getBitmapFromAssets("test.jpg");
//then create a copy of bitmap bmp1 into bmp2
        Bitmap image2 = image.copy(image.getConfig(), true);
        byte[] byteArray = NetUtil.getBitmapByte(image);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        ResponseBody body = ResponseBody
                .create(MediaType.parse(          // Convert string to MediaType
                        mimeTypeMap.getMimeTypeFromExtension("jpg") // currently only support JPG format, but we can add more image format
                        ), byteArray // the image is saved in the value
                );

        Bitmap data = NetUtil.getBitmapFromResponseBody(body);
        assertEquals(image2.getByteCount(), data.getByteCount());

    }

    @Test
    public void test_covertNetDataToListData() {
        Gson gson = new Gson();
        String strJson = NetUtil.getJson("news.json");
        NetData netData = gson.fromJson(strJson, NetData.class);
        ArrayList<BaseEntity> baseData = NetUtil.covertNetDataToListData(netData);
        assertEquals(baseData.size(), 15);
        //
        strJson = NetUtil.getJson("news1.json");
        netData = gson.fromJson(strJson, NetData.class);
        baseData = NetUtil.covertNetDataToListData(netData);
        assertEquals(baseData.size(), 35);
    }

    @Test
    public void test_covertBaseDataToListData() {
        Gson gson = new Gson();
        Bitmap image = NetUtil.getBitmapFromAssets("test.jpg");

        //
        String strJson = NetUtil.getJson("news.json");
        NetData netData = gson.fromJson(strJson, NetData.class);
        ArrayList<BaseEntity> baseData = NetUtil.covertNetDataToListData(netData);
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < baseData.size(); i++) {
            bitmaps.add(image);
        }
        ArrayList<BaseEntity> data = NetUtil.covertBaseDataToListData(baseData, bitmaps);
        assertEquals(data.size(), 11);
        //
        strJson = NetUtil.getJson("news1.json");
        netData = gson.fromJson(strJson, NetData.class);
        baseData = NetUtil.covertNetDataToListData(netData);
        bitmaps = new ArrayList<>();
        for (int i = 0; i < baseData.size(); i++) {
            bitmaps.add(image);
        }
        data = NetUtil.covertBaseDataToListData(baseData, bitmaps);
        assertEquals(data.size(), 24);
    }

}