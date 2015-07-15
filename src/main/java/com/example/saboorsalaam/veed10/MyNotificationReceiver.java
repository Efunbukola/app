package com.example.saboorsalaam.veed10;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParsePushBroadcastReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Saboor Salaam on 5/26/2015.
 */
public class MyNotificationReceiver extends ParsePushBroadcastReceiver {
    public MyNotificationReceiver() {
        super();
    }

    String thumb, title, alert;
    Bitmap bitmap;
    int size;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return R.drawable.app_icon;
    }

    @Override
    protected Bitmap getLargeIcon(Context context, Intent intent) {
        return super.getLargeIcon(context, intent);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {

        Log.d("Push received", "Notification received!!!!");
        Bundle extras = intent.getExtras();
        String message = extras != null ? extras.getString("com.parse.Data") : "";
        JSONObject jObject = null;
        try {
            try {
                jObject = new JSONObject(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            alert = jObject.getString("alert");
            thumb = jObject.getString("thumb");
            title = jObject.getString("title");
            size = jObject.getInt("size");
            Log.d("Thumb received", thumb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Thread client = new Thread(new Runnable() {
            public void run() {bitmap = getBitmapFromURL(thumb);}
        });
        client.start();
        //wait for background thread to finish
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent NewVideoIntent; //Start home activity for now

        return new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(alert)
                .setSmallIcon(R.drawable.app_icon)
                //.setLargeIcon(bitmap)
                .setNumber(size)
                .setAutoCancel(true).build();
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
