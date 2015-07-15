package UtilityClasses;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import ParseClasses.Video;

/**
 * Created by Saboor Salaam on 4/29/2015.
 */

public class VideoComparator implements Comparator<Video> {
    @Override
    public int compare(Video v1, Video v2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date v1_date = null;
        Date v2_date = null;

        Log.d("VideoComparator", "Attempting to Compare " + v1.getVideoId() + " to " + v1.getVideoId());

        //2014-06-02T21:58:18.000Z

        //parse date string to get Data object
        try {
            v1_date = simpleDateFormat.parse(v1.getDatePublished());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            v2_date = simpleDateFormat.parse(v2.getDatePublished());
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return 1; //v2_date.compareTo(v1_date);
    }
}
