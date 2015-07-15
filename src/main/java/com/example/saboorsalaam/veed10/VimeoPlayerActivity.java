package com.example.saboorsalaam.veed10;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import ObservableScrollView.BaseActivity;
import UtilityClasses.HTML5WebView;


public class VimeoPlayerActivity extends BaseActivity {

    HTML5WebView mWebView;
    String id;
    FrameLayout mFrameLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo_player);

        mFrameLayout = (FrameLayout) findViewById(R.id.frame);

        android.support.v7.widget.Toolbar customActionBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        customActionBar.showOverflowMenu();
        customActionBar.setTitle("");
        setSupportActionBar(customActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            id = extras.getString("video_id");
        }

        mWebView = new HTML5WebView(this);
        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.addJavascriptInterface(new AppJavaScriptProxy(this, mWebView, id), "androidAppProxy");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.contains(id))
                {
                    return true;
                }
                return false;
            }

            // autoplay when finished loading via javascript injection
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
            }
        });


        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else {
            mWebView.loadUrl("http://vimeo.com/" + id + "/description");
            //mWebView.loadUrl("https://player.vimeo.com/video/" + id + "?autoplay=0&loop=1");
        }

        mFrameLayout.addView(mWebView.getLayout(), COVER_SCREEN_PARAMS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }

    @Override
    public boolean onSupportNavigateUp() {
        mWebView.onPause();
        finish();
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.add_to_favorites:
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.inCustomView()) {
                mWebView.hideCustomView();
                mWebView.goBack();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
            new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

}
