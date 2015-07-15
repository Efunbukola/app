package Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saboorsalaam.veed10.HomeActivity;
import com.example.saboorsalaam.veed10.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapters.ExploreViewPagerAdapter;
import Adapters.HomeListViewAdapter;
import Adapters.HomeRecyclerViewAdapter;
import DatabaseHandler.DatabaseHandler;
import MyUltimateRecyclerView.MyUltimateRecyclerView;
import ObservableScrollView.BaseFragment;
import ObservableScrollView.ObservableRecyclerView;
import ObservableScrollView.ObservableScrollViewCallbacks;
import ObservableScrollView.ScrollUtils;
import ParseClasses.Channel;
import ParseClasses.ParseDBCommunicator;
import ParseClasses.Video;
import ParseClasses.connectionFailedListener;
import RecyclerViewDivider.HorizontalDividerItemDecoration;
import UtilityClasses.SnappingListView;
import UtilityClasses.VerticalViewPager;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;

/**
 * Created by Saboor Salaam on 4/27/2015.
 */
public class HomeFragment extends BaseFragment {

    DatabaseHandler databaseHandler;
    ExploreViewPagerAdapter exploreViewPagerAdapter;
    VerticalViewPager verticalViewPager;
    ProgressBar progressBar;
    GridViewLoader gridViewLoader;
    LinearLayout error_container;
    TextView try_agian_button;
    List<Channel> channels;
    ParseDBCommunicator parseDBCommunicator;
    Context context;
    SnappingListView snappingListView;
    HomeListViewAdapter homeListViewAdapter;
    MyUltimateRecyclerView recyclerView;
    HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    ScaleInAnimationAdapter slideInBottomAnimationAdapter;
    AlphaInAnimationAdapter alphaInAnimationAdapter;

    PointF start = new PointF();
    PointF mid = new PointF();
    int width, height, current_tab;
    Display display;

    HomeActivity parentActivity;

    public FragmentManager getFm() {
        return fm;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    FragmentManager fm;

    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_home, container, false);

        display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        parseDBCommunicator = new ParseDBCommunicator(getActivity());
        databaseHandler = new DatabaseHandler(getActivity());
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        error_container = (LinearLayout) rootView.findViewById(R.id.error_container);
        try_agian_button = (TextView) rootView.findViewById(R.id.try_again_button);
        channels = new ArrayList<>();
        gridViewLoader = new GridViewLoader();
        databaseHandler = new DatabaseHandler(getActivity());
        recyclerView = (MyUltimateRecyclerView) rootView.findViewById(R.id.scroll);

        //databaseHandler.addChannel("music");
        //databaseHandler.addChannel("gaming");
        //databaseHandler.addChannel("abstract");
        //databaseHandler.addChannel("technology");
        //databaseHandler.addChannel("news");
        //databaseHandler.addChannel("sports");
        //databaseHandler.addChannel("comedy");
        //databaseHandler.addChannel("summer_picks");
        //databaseHandler.addChannel("best_of_veed");
        //databaseHandler.deleteChannel("fathers_day");


        gridViewLoader.execute();


        parentActivity = (HomeActivity) getActivity();

        recyclerView.setHasFixedSize(false);

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified offset after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(recyclerView, new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollVerticallyToPosition(initialPosition);
                    }
                });
            }
            recyclerView.getmRecyclerView().setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return rootView;
    }


    public class GridViewLoader extends AsyncTask<Void, Void, Void> {

        final static int NO_NETWORK_CONNECTION = 0;
        final static int CANNOT_CONNECT_TO_PARSE = 1;
        int error_code = 0;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            error_container.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<Video> videos = parseDBCommunicator.getHomeTabVideos(new connectionFailedListener() {
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

            List<Channel> channels = new ParseDBCommunicator(context).suggestChannels(new connectionFailedListener() {
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

              homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity(), videos,channels, LayoutInflater.from(getActivity()).inflate(R.layout.padding, null), width, height);
            alphaInAnimationAdapter = new AlphaInAnimationAdapter(homeRecyclerViewAdapter);
            alphaInAnimationAdapter.setDuration(300);
            alphaInAnimationAdapter.setInterpolator(new DecelerateInterpolator());
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {

            //ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                    //.addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.cover, 50f, 10f));
                    //.addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.tutorial_img_phone, -0.65f,
                            //PARALLAX_EFFECT_DEFAULT));

            recyclerView.setAdapter(alphaInAnimationAdapter);
            //recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.enableDefaultSwipeRefresh(true);
            recyclerView.mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    parentActivity.hideToolbar();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setRefreshing(false);
                            //recyclerView.mSwipeRefreshLayout.b
                        }
                    }, 3000);
                }
            });
            recyclerView.enableLoadmore();
            recyclerView.setOnLoadMoreListener(new MyUltimateRecyclerView.OnLoadMoreListener() {
                @Override
                public void loadMore(int i, int i1) {

                }
            });
/*
            recyclerView.addItemDecoration(
                    new HorizontalDividerItemDecoration.Builder(context)
                            .sizeResId(R.dimen.grey_list_divider)
                            .color(Color.parseColor("#FFECECEC"))
                            .margin(R.dimen.divider)
                            .build());
                            */

            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            error_container.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            Log.d("Network Error", "Post Cancelled");
            error_container.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            if (error_code == NO_NETWORK_CONNECTION) {
                Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_SHORT).show();
            } else if (error_code == CANNOT_CONNECT_TO_PARSE) {
                Toast.makeText(getActivity(), R.string.no_connection_to_parse, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
