package com.jenway.slidenewslist.network;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jenway.slidenewslist.MyApplication;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.model.NewsEntity;
import com.jenway.slidenewslist.network.bean.NetData;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;


/**
 * by Xu
 * Description: the network operation
 */
public class NetUtil {
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return null != info && info.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //get asset manage
            AssetManager assetManager = MyApplication.getInstance().getApplicationContext().getAssets();
            //read the data
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static Bitmap getBitmapFromAssets(String fileName) {
        AssetManager assetManager = MyApplication.getInstance().getApplicationContext().getAssets();

        InputStream istr = null;
        try {
            istr = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    public static byte[] getBitmapByte(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        image.recycle();
        return byteArray;
    }

    public static Bitmap getBitmapFromResponseBody(ResponseBody value) {
        byte[] bys = new byte[0];
        try {
            bys = value.bytes();
            return BitmapFactory.decodeByteArray(bys, 0, bys.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<BaseEntity> covertNetDataToListData(NetData netData) {
        ArrayList<BaseEntity> baseData = new ArrayList<>();
        for (int i = 0; i < netData.getContent().size(); i++) {
            BaseEntity tempEntity = new NewsEntity(i, netData.getContent().get(i).getTitle(), netData.getContent().get(i).getSummary(), netData.getContent().get(i).getImages().getMainImage().getUrl());
            baseData.add(tempEntity);
        }
        return baseData;
    }

    public static ArrayList<BaseEntity> covertBaseDataToListData(ArrayList<BaseEntity> baseData, List<Bitmap> bitmaps) {
        ArrayList<BaseEntity> data = new ArrayList<>();
        //
        boolean isHorizantolItem = false;
        ArrayList<BaseEntity> tempList = new ArrayList<>();

        for (int i = 0; i < baseData.size(); i++) {
            //sing the image to the list
            try {
                ((NewsEntity) baseData.get(i)).setImage(bitmaps.get(i));
            } catch (Exception e) {

            }

            if (i != 0 && i % 3 == 0) {
                if (!isHorizantolItem) {//horizontal start
                    isHorizantolItem = true;
                    //create horizontal item
                    data.add(new NewsEntity(i, "title", "summary", "url"));
                } else {     //vertical start
                    isHorizantolItem = false;
                    ((NewsEntity) data.get(data.size() - 1)).setHorizontalNews(tempList);
                    //clear the inner list
                    tempList = new ArrayList<>();
                }
            }

            if (isHorizantolItem) {
                tempList.add(baseData.get(i));
                //in case the horizontal not reach to 3
                if (i == baseData.size() - 1) {
                    isHorizantolItem = false;
                    ((NewsEntity) data.get(data.size() - 1)).setHorizontalNews(tempList);
                    //clear the inner list
                    tempList = new ArrayList<>();
                }
            } else {
                baseData.get(i).setIndex(i);
                data.add(baseData.get(i));
            }
        }
        return data;
    }
}
