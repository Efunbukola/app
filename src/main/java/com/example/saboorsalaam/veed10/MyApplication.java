package com.example.saboorsalaam.veed10;

import android.app.Application;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.PushService;


/**
 * Created by Saboor Salaam on 6/5/2015.
 */
public class MyApplication extends Application {

    private static final String SHARED_PREFS = "veed_prefs";

    @Override
    public void onCreate() {
        super.onCreate();

        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences(SHARED_PREFS, 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            Parse.enableLocalDatastore(getApplicationContext());
            Parse.initialize(getApplicationContext(), "4WShWGs2N5eF0WL3qBj3Gbqm61JyY3tPzSzVU6Q0", "TLe2dAXt8RUiYA1IwUmnm2He2DO0j12qq7bx02lu");
            PushService.setDefaultPushCallback(this, HomeActivity.class);
            settings = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time your app loads
        }
    }
}