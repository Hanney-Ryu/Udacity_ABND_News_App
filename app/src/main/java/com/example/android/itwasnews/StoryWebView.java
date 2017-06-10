package com.example.android.itwasnews;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.itwasnews.databinding.WebViewStoryBinding;

/**
 * This class covers content related to WebView.
 * When you click on a news list item, you are connected to the web view by the intent.
 */
public class StoryWebView extends AppCompatActivity {
    private static final String LOG_TAG = CategoryAdapter.class.getSimpleName();
    private WebViewStoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.web_view_story);

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");

        // set webview
        binding.webViewStory.getSettings().setLoadWithOverviewMode(true);
        binding.webViewStory.setWebViewClient(new MyWebViewClient());
        binding.webViewStory.loadUrl(url);

        binding.toolbarStory.toolbarTitle.setText(title);
        binding.toolbarStory.toolbarUp.setOnClickListener(new UpClickListener());
    }

    /**
     * This listener handles what happens when the back button is pressed.
     */
    private class UpClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (binding.webViewStory.canGoBack()) {
                binding.webViewStory.goBack();
            } else {
                StoryWebView.super.onBackPressed();
            }
        }
    }

    /**
     * This class has been overridden for the following reasons:
     * 1. Change the state of the progressbar and the webview depending on the loading state
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            binding.storyLoadingIndicator.setVisibility(View.GONE);
            binding.webViewStory.setVisibility(View.VISIBLE);
        }
    }
}
