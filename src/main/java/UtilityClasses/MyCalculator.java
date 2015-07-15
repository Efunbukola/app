package UtilityClasses;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Saboor Salaam on 4/29/2015.
 */
public class MyCalculator {

    public static int max(List<Integer> nums) {
        int max = Collections.max(nums);
        return max;
    }

    public static String getTimeAgo(String dateinfo) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
       //parse date string to get Data object
        try {
            date = df.parse(dateinfo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Use pretty time to format date
        PrettyTime ptime = new PrettyTime();
        String timePassedString = (ptime.format(date));
        return timePassedString;
    }

}
