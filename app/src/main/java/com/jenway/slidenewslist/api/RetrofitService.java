package com.jenway.slidenewslist.api;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.jenway.slidenewslist.MyApplication;
import com.jenway.slidenewslist.api.myGsonConverter.MyGsonConverterFactory;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.model.Config;
import com.jenway.slidenewslist.model.NewsEntity;
import com.jenway.slidenewslist.network.NetUtil;
import com.jenway.slidenewslist.network.bean.NetData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * by Xu
 * Description: provide the retrofit service
 * Only load the local data when the debug mod is on
 */

public class RetrofitService {

    //Cache is valid for 1 day
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //set the Cache-Control for to use catch only
    // max-stale set Cache expiration time
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    private static final String TAG = "RetrofitService";
    /**
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable(MyApplication.getInstance().getApplicationContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                Log.e(TAG, "no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtil.isNetworkAvailable(MyApplication.getInstance().getApplicationContext())) {
                //Configure the Header information
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };
    /**
     * check the request information and JSON file
     */
    private static final Interceptor DogApiInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(requestBuffer);
            } else {
                Log.d(TAG, "request.body() == null");
            }
            final Response response = chain.proceed(request);
            return response;
        }
    };
    //only use for test
    public static String mockDataName = "news.json";
    public static IBaseApi baseApi;

    private RetrofitService() {
        throw new AssertionError();
    }

    /**
     * Initialize network communication service
     */
    public static void init() {
        if (MyApplication.DEBUG) {
            Log.d("RetrofitService", "Debug");
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new RetrofitService.MockInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.API_BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MyGsonConverterFactory.create())
                    .build();
            baseApi = retrofit.create(IBaseApi.class);

        } else {
            // Catch path and catch size for example 100Mb
            Cache cache = new Cache(new File(MyApplication.getInstance().getApplicationContext().getCacheDir(), "HttpCache"),
                    1024 * 1024 * 100);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(DogApiInterceptor)
                    .addInterceptor(sRewriteCacheControlInterceptor)
                    .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MyGsonConverterFactory.create())
                    .baseUrl(Config.API_BASE_URL)
                    .build();
            baseApi = retrofit.create(IBaseApi.class);
        }

    }

    /**
     * to get NetData from new thread
     * to get the News Entity from IO thread
     */
    public static Observable<ArrayList<BaseEntity>> getData(int Count) {

        if (Count == 0)
            Count = Config.maxloadingNewSCount;

        return Observable.just(Count)
                .subscribeOn(Schedulers.newThread()).flatMap(new Function<Integer, ObservableSource<NetData>>() {
                    @Override
                    public ObservableSource<NetData> apply(Integer count) throws Exception {
                        //load the local data in the test
                        if (MyApplication.DEBUG) {
                            mockDataName = "news.json";
                        }
                        return baseApi.getData(count, 0);
                    }
                }).observeOn(Schedulers.io()).map(new Function<NetData, ArrayList<BaseEntity>>() {
                    @Override
                    public ArrayList<BaseEntity> apply(NetData netData) throws Exception {
                        return NetUtil.covertNetDataToListData(netData);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


    /*--------------------------------- API ---------------------------------*/

    /**
     * to use interable to achieve the list of request
     * to use the flatmap to acheive the multiple requests
     */
    public static Single<ArrayList<BaseEntity>> getNewsImage(final ArrayList<BaseEntity> dataList) {
        return Observable.fromIterable(dataList)
                .subscribeOn(Schedulers.newThread()).concatMap(new Function<BaseEntity, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(BaseEntity data) throws Exception {
                        //load the local data in the test
                        if (MyApplication.DEBUG) {
                            mockDataName = "Bitmap";
                        }
                        return baseApi.getNewsImage(((NewsEntity) data).getImageUrl());
                    }
                }).observeOn(Schedulers.io()).map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody body) throws Exception {
                        return NetUtil.getBitmapFromResponseBody(body);
                    }
                }).toList()//cover to the list
                .map(new Function<List<Bitmap>, ArrayList<BaseEntity>>() {
                    @Override
                    public ArrayList<BaseEntity> apply(List<Bitmap> bitmaps) throws Exception {
//                        NetUtil.singTheImageToList(dataList, bitmaps);
                        return NetUtil.covertBaseDataToListData(dataList, bitmaps);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    /*--------------------------------- mock the local data ---------------------------------*/
    private static class MockInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            ResponseBody body = null;
            if (mockDataName.equals("Bitmap")) {//if the request is Image, then return the n02097130_5347.jpg
                Bitmap image = NetUtil.getBitmapFromAssets("test.jpg");
                byte[] byteArray = NetUtil.getBitmapByte(image);
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                body = ResponseBody
                        .create(MediaType.parse(          // Convert string to MediaType
                                mimeTypeMap.getMimeTypeFromExtension("jpg") // currently only support JPG format, but we can add more image format
                                ), byteArray // the image is saved in the value
                        );
                Response response = new Response.Builder().request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .body(body)
                        .message("")
                        .build();
                return response;


            } else if (mockDataName.equals("uri")) {//send mock uri
                // get local json/data
                String content = NetUtil.getJson("news.json");
                body = ResponseBody.create(MediaType.parse("application/json"), content);
                Response response = new Response.Builder().request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .body(body)
                        .message("")
                        .build();
                return response;
            } else {
                // get local json/data
                String content = NetUtil.getJson(mockDataName);
                body = ResponseBody.create(MediaType.parse("application/json"), content);
                Response response = new Response.Builder().request(chain.request())
                        .protocol(Protocol.HTTP_1_1)
                        .code(200)
                        .body(body)
                        .message("")
                        .build();
                return response;
            }


        }
    }
}
