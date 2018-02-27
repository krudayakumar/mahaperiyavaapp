package com.kanchi.periyava.model

import android.view.View
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by m84098 on 2/16/18.
 */
// Define the listener interface
interface YoutubeMetaDataService {

    //oembed?url=". $url ."&format=json
    @GET("oembed?url={url}&format=json")
    fun getMetaData(@Path("user") url: String): Call<YouTubeMetaData>
}
