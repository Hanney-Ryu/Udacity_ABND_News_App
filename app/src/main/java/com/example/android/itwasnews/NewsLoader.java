package com.example.android.itwasnews;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private static final String LOG_TAG = NewsLoader.class.getSimpleName();
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        return QueryUtils.fetchNewsData(url);
    }
}
