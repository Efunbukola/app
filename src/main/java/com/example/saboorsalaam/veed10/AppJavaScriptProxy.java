package com.example.saboorsalaam.veed10;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.net.URL;

/**
 * Created by Saboor Salaam on 5/28/2015.
 */

public class AppJavaScriptProxy {

    private Activity activity = null;
    private WebView  webView  = null;
    private String video_id;

    public AppJavaScriptProxy(Activity activity, WebView webview, String video_id) {
        this.video_id = video_id;
        this.activity = activity;
        this.webView  = webview;
    }

    @JavascriptInterface
    public void showMessage(final String message) {

        final Activity theActivity = this.activity;
        final WebView theWebView = this.webView;

        this.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(!theWebView.getUrl().startsWith("http://vimeo.com/" + video_id + "/description")){
                    return ;
                }

                Toast toast = Toast.makeText(
                        theActivity.getApplicationContext(),
                        message,
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        });
    }
}
