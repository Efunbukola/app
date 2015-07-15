package Fragments;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.example.saboorsalaam.veed10.HomeActivity;
import com.example.saboorsalaam.veed10.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;


import java.util.ArrayList;
import java.util.List;


import Adapters.ChannelDragSortRecyclerAdapter;
import DatabaseHandler.DatabaseHandler;
import MyUltimateRecyclerView.MyUltimateRecyclerView;
import ObservableScrollView.BaseFragment;
import ObservableScrollView.ObservableRecyclerView;
import ObservableScrollView.ObservableScrollViewCallbacks;
import ObservableScrollView.ScrollUtils;
import ParseClasses.Channel;
import ParseClasses.connectionFailedListener;



/**
 * Created by Saboor Salaam on 4/27/2015.
 */
public class MyChannelsFragment extends BaseFragment {

    private static final String TAG = "MGF";
    public static final int TYPE_CHANNEL = 0;
    private static final int TYPE_FOOTER = 1;
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";


    int width, height;
    Display display;

    DatabaseHandler databaseHandler;
    GridViewLoader gridViewLoader;
    List<Channel> channels = new ArrayList<>();
    ChannelDragSortRecyclerAdapter myChannelsDragSortAdapter;

    ProgressBar progressBar;
    LinearLayout error_container;
    TextView try_agian_button;
    HomeActivity parentActivity;
    RecyclerViewHeader header;

    MyUltimateRecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentActivity = (HomeActivity) getActivity();

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_my_channels, container, false);

        display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        error_container = (LinearLayout) rootView.findViewById(R.id.error_container);
        try_agian_button = (TextView) rootView.findViewById(R.id.try_again_button);
        recyclerView = (MyUltimateRecyclerView) rootView.findViewById(R.id.scroll);
        databaseHandler = new DatabaseHandler(getActivity());
        header = (RecyclerViewHeader) rootView.findViewById(R.id.header);


        gridViewLoader = new GridViewLoader();
        gridViewLoader.execute();


        try_agian_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //error_container.setVisibility(View.GONE);
                new GridViewLoader().execute();
            }
        });

        parentActivity.getFloatingActionButton().attachToRecyclerView(recyclerView.getmRecyclerView());

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

            channels = databaseHandler.getAllLocalChannels(new connectionFailedListener() {
                @Override
                public void onConnectionFailed() {
                    error_code = NO_NETWORK_CONNECTION;
                    Log.d("Network Error", "Cannot connect");
                    cancel(true);
                }

                @Override
                public void onConnectionSuccessful() {
                }

                @Override
                public void onCannotConnectToParse() {
                    //Toast.makeText(getApplicationContext(), R.string.no_connection_to_parse, Toast.LENGTH_LONG).show();
                    error_code = CANNOT_CONNECT_TO_PARSE;
                    Log.d("Network Error", "Cannot connect");
                    cancel(true);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            myChannelsDragSortAdapter = new ChannelDragSortRecyclerAdapter(recyclerView.getmRecyclerView(), channels, getActivity(), width/2);
            recyclerView.setAdapter(myChannelsDragSortAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            header.attachTo(recyclerView.getmRecyclerView(), true);
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

};

