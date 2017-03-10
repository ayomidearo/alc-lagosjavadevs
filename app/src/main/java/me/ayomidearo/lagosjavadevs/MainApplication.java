package me.ayomidearo.lagosjavadevs;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.prefs.Preferences;

/**
 * Created by aro on 3/10/17.
 */

public class MainApplication extends Application {

    private static MainApplication mInstance;



    Preferences pref;

    private RequestQueue mRequestQueue;

    public static final String TAG = MainApplication.class.getSimpleName();
    public MainApplication() {
        mInstance = this;
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();


    }

    public static Context getContext()
    {
        return mInstance;
    }
}
