package ParseClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.apache.http.impl.client.DefaultHttpClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import DatabaseHandler.DatabaseHandler;
import UtilityClasses.MyCalculator;
import UtilityClasses.VideoComparator;

/**
 * Created by Saboor Salaam on 6/6/2014.
 */

public class ParseDBCommunicator {

    private Context context;
    static DefaultHttpClient httpClient;

    public ParseDBCommunicator(Context c) {
        this.context = c;
    }
    ParseQuery<ParseObject> mainQuery; //Global Declarations***************
    List<ParseObject> objects; //Global Declarations***********************



    public List<Channel> getSelectedChannels(List<String> ids, final connectionFailedListener cfl) //********************************
    {
        final ParseQuery<ParseObject> matchId = ParseQuery.getQuery("Channel");
        matchId.whereContainedIn("channel_id", ids);


        objects = new ArrayList<ParseObject>();
        List<Channel> channels = new ArrayList<Channel>();

        List<Channel> orderedChannels = new ArrayList<Channel>();

        if (haveNetworkConnection()) {   //If we can get a network connection

            Thread client = new Thread(new Runnable() {
                public void run() {
                    try {
                        objects = matchId.find();
                        ParseObject.pinAllInBackground(objects); //Pin objects to local datastore whenever we retrieve them
                    } catch (ParseException e) {
                        cfl.onCannotConnectToParse();
                        e.printStackTrace();
                    }

                }
            });
            client.start();
            //wait for background thread to finish
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d("Channel", "Retrieved " + objects.size() + " channels"); //Add channels to list!!

            for (int z = 0; z < objects.size(); z++) {
                List<String> provs = new ArrayList<>();
                List<String> cats = new ArrayList<>();
                if(objects.get(z).has("providers")) {
                    provs = objects.get(z).getList("providers");
                }
                if(objects.get(z).has("categories")) {
                    cats = objects.get(z).getList("categories");
                }
                channels.add(new Channel(objects.get(z).getString("channel_name"), objects.get(z).getString("channel_id"), objects.get(z).getString("cover_thumbnail"), objects.get(z).getString("cover_title"), objects.get(z).getString("description"),objects.get(z).getInt("size"),provs, cats));
            }

            boolean notDone = true;

            for (int z = 0; z < ids.size(); z++) {  //Reorder Channels
                for (int i = 0; i < channels.size() && notDone; i++) {
                    //Log.d("Reorderer", "Found Match Between " + ids.get(z) + " and " + channels.get(i).getChannel_name() + ", " + channels.get(i).getChannel_id());
                    if (ids.get(z).equals(channels.get(i).getChannel_id())) {
                        Log.d("Reorderer", "Found Match Between " + ids.get(z) + " and " + channels.get(i).getChannel_name() + ", " + channels.get(i).getChannel_id());
                        Channel ch = channels.get(i);
                        orderedChannels.add(ch);
                        notDone = false;
                    }
                }
                notDone = true;
            }
            cfl.onConnectionSuccessful(); //Connection is successful
        } else {
            cfl.onConnectionFailed(); // Connection failed
        }

        return orderedChannels;
    }


    public List<Video> getVideosofAChannel(final String channel_id, String channel_title, final connectionFailedListener cfl)
    {
        query = ParseQuery.getQuery("Channel"); // get all channels that mach that category
        query.whereEqualTo("channel_id", channel_id);
        query.fromLocalDatastore();
        List<Video> videos = new ArrayList<Video>();
        objects = new ArrayList<>();

        if (haveNetworkConnection()) {   //If we can get a network connection

            Thread client = new Thread(new Runnable() {
                public void run() {

                    try {
                        ParseObject channel;
                            channel =  query.getFirst(); //Try to get channel from local data store
                            if(channel == null)
                            {
                                ParseQuery _query = ParseQuery.getQuery("Channel"); //if not then get from cloud datastore
                                _query.whereEqualTo("channel_id", channel_id);
                                channel = _query.getFirst();
                            }

                            List<String> list = channel.getList("providers");
                            if(list != null) {
                                query = ParseQuery.getQuery("Video"); // get all channels that mach that category
                                query.whereContainedIn("provider_id", list);
                                objects = query.find();
                            }

                    } catch (ParseException e) {
                        cfl.onCannotConnectToParse();
                        e.printStackTrace();
                    }

                }
            });
            client.start();
            //wait for background thread to finish
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d("Videos", "Retrieved " + objects.size() + " videos");


            for (int z = 0; z < objects.size(); z++) {
                videos.add(new Video(objects.get(z).getString("thumbnail"), objects.get(z).getString("provider_name"), objects.get(z).getString("provider_id"),channel_title, channel_id, objects.get(z).getString("video_id"), objects.get(z).getString("date_published"), objects.get(z).getString("video_title"), objects.get(z).getString("video_description"), objects.get(z).getString("video_thumbnail"), objects.get(z).getString("video_type") ));
            }
            if(cfl != null)
                cfl.onConnectionSuccessful();
        } else {
            if(cfl != null)
                cfl.onConnectionFailed();
        }

        videos = new DatabaseHandler(context).cropBlockedVideos(videos);

        return videos;
    }



    ParseQuery<ParseObject> query; //*********************************************************


    public List<Category> getAllCategories(final connectionFailedListener cfl){

        List<Category> categories = new ArrayList<Category>();
        objects = new ArrayList<ParseObject>();
        query = ParseQuery.getQuery("Category");

        if(haveNetworkConnection()) {
            Thread client = new Thread(new Runnable() {
                public void run() {
                    try {
                        objects = query.find();
                    } catch (ParseException e) {
                        cfl.onCannotConnectToParse();
                        e.printStackTrace();
                    }
                }
            });
            client.start();
            //wait for background thread to finish
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < objects.size(); i++)//loop through each category
            {
                //int size = getAllChannelsOfACategory(objects.get(i).getString("name")).size();
                categories.add(new Category(objects.get(i).getString("name"), 10)); // add name to list of headers
                Log.d("Category", "Retrieved " + objects.get(i).getString("name"));
            }
            cfl.onConnectionSuccessful();
        }else{
            cfl.onConnectionFailed();
        }

        return categories;
    }

    public List<Channel> getAllChannelsOfACategory(String category, final connectionFailedListener cfl) {
        List<Channel> channels = new ArrayList<Channel>();

        query = ParseQuery.getQuery("Channel"); // get all channels that mach that category
        query.whereEqualTo("category", category);

        if(haveNetworkConnection())
        {
            Thread client = new Thread(new Runnable() {
                public void run() {
                    try {
                        objects = query.find();
                    } catch (ParseException e) {
                        cfl.onCannotConnectToParse();
                        e.printStackTrace();
                    }
                }
            });
            client.start();
            //wait for background thread to finish
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.d("Channel", "Retrieved " + objects.size() + " channels");
            for (int z = 0; z < objects.size(); z++) {
                List<String> provs = new ArrayList<>();
                List<String> cats = new ArrayList<>();
                if(objects.get(z).has("providers")) {
                    provs = objects.get(z).getList("providers");
                }
                if(objects.get(z).has("categories")) {
                    cats = objects.get(z).getList("categories");
                }
                channels.add(new Channel(objects.get(z).getString("channel_name"), objects.get(z).getString("channel_id"), objects.get(z).getString("cover_thumbnail"), objects.get(z).getString("cover_title"),objects.get(z).getString("description"), objects.get(z).getInt("size"),provs, cats
                ));
            }
            cfl.onConnectionSuccessful();
        }else {
            cfl.onConnectionFailed();
        }
        return channels;
    }

    List<ParseObject> myobjects = new ArrayList<>();



    public List<Channel> suggestChannels(final connectionFailedListener cfl)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<Channel> channels = databaseHandler.getAllLocalChannels(new connectionFailedListener() {
        @Override
        public void onConnectionFailed() {

        }

        @Override
        public void onConnectionSuccessful() {

        }

        @Override
        public void onCannotConnectToParse() {

        }
        });

        List<String> cats = new ArrayList<>();

        for(int i = 0; i < channels.size(); i ++)
        {
            for(int z = 0; z < channels.get(i).getCategories().size(); z ++)
            {
                if(!cats.contains(channels.get(i).getCategories().get(z))){
                    cats.add(channels.get(i).getCategories().get(z));
                }
            }
        }

        final ParseQuery<ParseObject> matchCats = ParseQuery.getQuery("Channel");
        matchCats.whereContainedIn("categories", cats);
        matchCats.setLimit(500);

        if (haveNetworkConnection()) {   //If we can get a network connection
            Thread client = new Thread(new Runnable() {
                public void run() {
                    try {
                        objects = matchCats.find();
                    } catch (ParseException e) {
                        if(cfl != null)
                            cfl.onCannotConnectToParse();
                        e.printStackTrace();
                    }
                }
            });
            client.start();
            //wait for background thread to finish
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int z = 0; z < objects.size(); z++) {
                List<String> provs = new ArrayList<>();
                cats = new ArrayList<>();
                if(objects.get(z).has("providers")) {
                    provs = objects.get(z).getList("providers");
                }
                if(objects.get(z).has("categories")) {
                    cats = objects.get(z).getList("categories");
                }
                channels.add(new Channel(objects.get(z).getString("channel_name"), objects.get(z).getString("channel_id"), objects.get(z).getString("cover_thumbnail"), objects.get(z).getString("cover_title"),objects.get(z).getString("description"),objects.get(z).getInt("size"),provs, cats
                ));

                Collections.shuffle(channels);
            }


            if(cfl != null)
                cfl.onConnectionSuccessful();
        } else {
            if(cfl != null)
                cfl.onConnectionFailed();
        }

        if(channels.size() < 6){return channels;}else{return channels.subList(0, 6);}
    }





    public List<Video> getHomeTabVideos(final connectionFailedListener cfl){

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        List<Video> videos = new ArrayList<>();
        List<String> provider_ids = new ArrayList<>();

        List<Channel> mychannels = databaseHandler.getAllLocalChannels(new connectionFailedListener() {
            @Override
            public void onConnectionFailed() {

            }

            @Override
            public void onConnectionSuccessful() {

            }

            @Override
            public void onCannotConnectToParse() {

            }
        });

        for(int i = 0; i < mychannels.size(); i ++) //Attempt to remove duplicate providers
        {
            for(int z = 0; z < mychannels.get(i).getProviders().size(); z ++)
            {
                if(!provider_ids.contains(mychannels.get(i).getProviders().get(z))){
                    provider_ids.add(mychannels.get(i).getProviders().get(z));
                }
            }
        }

        final ParseQuery<ParseObject> matchId = ParseQuery.getQuery("Video");
        matchId.whereContainedIn("provider_id", provider_ids);
        matchId.setLimit(500);

        if (haveNetworkConnection()) {   //If we can get a network connection
            Thread client = new Thread(new Runnable() {
                public void run() {
                    try {
                        myobjects = matchId.find();
                    } catch (ParseException e) {
                        cfl.onCannotConnectToParse();
                        e.printStackTrace();
                    }
                }
            });
            client.start();
            //wait for background thread to finish
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int z = 0; z < myobjects.size(); z++) {
                videos.add(new Video(myobjects.get(z).getString("provider_thumbnail"), myobjects.get(z).getString("provider_name"), myobjects.get(z).getString("provider_id"), "Veed", "Null", myobjects.get(z).getString("video_id"), myobjects.get(z).getString("date_published"), myobjects.get(z).getString("video_title"), myobjects.get(z).getString("video_description"), myobjects.get(z).getString("video_thumbnail"), myobjects.get(z).getString("video_type")));
            }
            if(cfl != null)
                cfl.onConnectionSuccessful();
        } else {
            if(cfl != null)
                cfl.onConnectionFailed();
        }

        Collections.shuffle(videos);

        for(int i = 0; i < videos.size(); i++) //Attempt to match video to a channel
        {
            for(int z = 0; z < mychannels.size(); z++)
            {
                    if(mychannels.get(z).getProviders().contains(videos.get(i).provider_id)){
                        videos.get(i).setChannel_name(mychannels.get(z).channel_name);
                        videos.get(i).setChannel_id(mychannels.get(z).channel_id);
                        break;
                    }
            }
        }

        //********************************************
        //Make sure the no duplicate VIDEOS!!!!!!!!!!!!!
        //***********************************************


/*
        List<Video> hometabvideos = new ArrayList<>();
        List<Channel> channels = new DatabaseHandler(context).getAllLocalChannels(new DatabaseHandler.connectionFailedListener() {
            @Override
            public void onConnectionFailed(){}

            @Override
            public void onConnectionSuccessful(){}

            @Override
            public void onCannotConnectToParse(){}
        });


        Collections.sort(onevideos, new VideoComparator());
        Collections.sort(twovideos, new VideoComparator());
        Collections.sort(threevideos, new VideoComparator());


        for(int i = 0; i < 15; i++)
        {
            if(i < onevideos.size())
                hometabvideos.add(onevideos.get(i));
            if(i < twovideos.size())
                hometabvideos.add(twovideos.get(i));
            if(i < threevideos.size())
                hometabvideos.add(threevideos.get(i));
        }

        */
        videos = databaseHandler.cropBlockedVideos(videos);
        if(videos.size() < 20){return videos;}else{return videos.subList(0, 19);}
    }








    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}
