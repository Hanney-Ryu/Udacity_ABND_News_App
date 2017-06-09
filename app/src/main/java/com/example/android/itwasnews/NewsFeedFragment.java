package com.example.android.itwasnews;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.android.itwasnews.databinding.NewsListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String LOG_TAG = NewsFeedFragment.class.getSimpleName();

    private NewsListBinding binding;
    private View rootView;
    private int position;
    private NewsAdapter newsAdapter;
    private Loader<List<News>> newsLoader;
    private static final String GUARDION_REQUEST_URL = "https://content.guardianapis.com/search?section=technology";
    private static final String API_KEY = "test";

    public static Fragment newInstance(int position) {
        NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
        newsFeedFragment.position = position;
        return newsFeedFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.news_list, container, false);
        rootView = binding.getRoot();
        newsAdapter = new NewsAdapter(getContext(), new ArrayList<News>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(newsAdapter);

        if(isConnected()){
            getActivity().getSupportLoaderManager().initLoader(position, getArguments(), this);
        } else {
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.noNetworkStateTextView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(GUARDION_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("from-date",QueryUtils.getFromDate());
        uriBuilder.appendQueryParameter("to-date",QueryUtils.getToDate());
        uriBuilder.appendQueryParameter("order-by","relevance");
        uriBuilder.appendQueryParameter("q",CategoryAdapter.categories[position]);
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        Log.i(LOG_TAG, "query : " + uriBuilder.toString());
        newsLoader = new NewsLoader(getContext(), uriBuilder.toString());
        return newsLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsFeed) {
        binding.loadingIndicator.setVisibility(View.GONE);
        if(newsFeed.isEmpty() || newsFeed.size() == 0 ){
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyNewsStateTextView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyNewsStateTextView.setVisibility(View.GONE);
        }
        newsAdapter.swapNewsFeed(newsFeed);
    }



    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.swapNewsFeed(null);
    }

    // Check whether the network is connected or not.
    public boolean isConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null  && networkInfo.isConnected();
    }
}
