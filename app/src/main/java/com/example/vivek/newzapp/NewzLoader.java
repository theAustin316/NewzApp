package com.example.vivek.newzapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class NewzLoader extends AsyncTaskLoader<List<Newz>> {

    public String mUrl;
    private static final String LOG_TAG = NewzLoader.class.getName();


    public NewzLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Newz> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Newz> news = QueryUtils.fetchNewzData(mUrl);
        return news;
    }
}
