package com.kanchi.periyava

/**
 * Created by m84098 on 9/26/15.
 */

import android.app.Application

import com.android.volley.RequestQueue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kanchi.periyava.model.YoutubeMetaDataService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder


class MyApplication : Application() {

    private var mRequestQueue: RequestQueue? = null

    companion object {

        val TAG = MyApplication::class.java.simpleName

        var instance: MyApplication? = null
            private set
        //Firebase Storage
        var firebaseStorage: FirebaseStorage? = null
            private set

       //Firebase Auth
        var firebaseAuth: FirebaseAuth? = null
            private set

        //Data base & Reference
        var firebaseDatabase: FirebaseDatabase? = null
            private set
        var refAcharayas:DatabaseReference?= null
                private set
        var refAnushamCenters:DatabaseReference?= null
            private set
        var refAudio:DatabaseReference?= null
            private set
        var refBlog:DatabaseReference?= null
            private set
        var refBooks:DatabaseReference?= null
            private set
        var refContactUs:DatabaseReference?= null
            private set
        var refImage:DatabaseReference?= null
            private set
        var refLineage:DatabaseReference?= null
            private set
        var refPeetam:DatabaseReference?= null
            private set
        var refPhotos:DatabaseReference?= null
            private set
        var refRaadioSchedule:DatabaseReference?= null
            private set
        var refVideos:DatabaseReference?= null
            private set

        //Youtube Retrofit & Service
        var  youtubeRetrofit:Retrofit?=null
            private set

        var  youtubeService:YoutubeMetaDataService?=null
            private set
    }




    override fun onCreate() {
        super.onCreate()
        instance = this
        InitFirebase()
        InitRetroFit()
    }


    //Retrofit
    fun InitRetroFit(){

        //For Youtube : Meta data htto://www.youtube.come/oembed?url=". $url ."&format=json
        youtubeRetrofit = Retrofit.Builder()
                .baseUrl("http://www.youtube.com/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .build()

        youtubeService = youtubeRetrofit!!.create(YoutubeMetaDataService::class.java)

    }
    //Firebase Database
    fun InitFirebase() {

        //Firebase Crash
        FirebaseCrash.setCrashCollectionEnabled(true)

        //Storage
        firebaseStorage = FirebaseStorage.getInstance("gs://periyava-radio.appspot.com")

        //Databse
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase!!.setPersistenceEnabled(true)

        refAcharayas=firebaseDatabase!!.getReference("acharayas")
        refAnushamCenters=firebaseDatabase!!.getReference("anusham_centers")
        refAudio=FirebaseDatabase.getInstance("https://periyava-radio-audio.firebaseio.com/")!!.reference
        refBlog=firebaseDatabase!!.getReference("blog")
        refBooks=firebaseDatabase!!.getReference("books")
        refContactUs=firebaseDatabase!!.getReference("contact_us")
        refImage=firebaseDatabase!!.getReference("image")
        refLineage=FirebaseDatabase.getInstance("https://periyava-radio-lineage.firebaseio.com/")!!.reference
        refPeetam=firebaseDatabase!!.getReference("peetam")
        refPhotos=firebaseDatabase!!.getReference("photos")
        refRaadioSchedule=firebaseDatabase!!.getReference("raadio_schedule")
        refVideos=firebaseDatabase!!.getReference("videos")

        refAcharayas!!.keepSynced(true)
        refAnushamCenters  !!.keepSynced(true)
        refAudio!!.keepSynced(true)
        refBlog !!.keepSynced(true)
        refBooks!!.keepSynced(true)
        refContactUs!!.keepSynced(true)
        refImage!!.keepSynced(true)
        refLineage!!.keepSynced(true)
        refPeetam!!.keepSynced(true)
        refPhotos!!.keepSynced(true)
        refRaadioSchedule!!.keepSynced(true)
        refVideos!!.keepSynced(true)
    }



}
