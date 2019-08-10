package com.jenway.slidenewslist.api;

import com.jenway.slidenewslist.model.Config;
import com.jenway.slidenewslist.network.bean.NetData;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IBaseApi {


    /**
     * Get the News list
     * eg:https://contentapi.celltick.com/mediaApi/v1.0/content?key=t4QCg6zCkFrCW5CTJii52IAQojqNmyeJ&publisherId=Magazine_from_AppDevWebsite&userId=eccc7785-001c-4341-88f8-eddf15f3aa4a&countryCode=US&language=en&limit=2&offset=0
     * limit={NEWS_COUNT}&offset=0"
     *
     * @param newsCount how many news can be loaded for each refresh
     * @return NetData
     */
    @GET(Config.NEWS_URL)
    Observable<NetData> getData(@Query("limit") int newsCount, @Query("offset") int offset);


    /**
     * get the ResponseBody(Image/JPG)
     * eg:https://imageca.thestartmagazine.com/fetch/d_magazineDefault.jpg,c_fill,g_face:auto,fl_lossy,q_70,w_270,h_190/https%3A%2F%2Fcdn.cnn.com%2Fcnnnext%2Fdam%2Fassets%2F190628173546-child-detention-center-video-synd-2.jpg
     *
     * @param uri Image Uri
     * @return Bitmap
     */
    @GET
    Observable<ResponseBody> getNewsImage(@Url String uri);


}

