package Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saboorsalaam.veed10.R;

import ParseClasses.Channel;

/**
 * Created by Saboor Salaam on 5/31/2015.
 */
public class ExploreListViewPagerFragement extends Fragment {

    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

   public ExploreListViewPagerFragement newInstance(Channel channel, int counter) {

       ExploreListViewPagerFragement f = new ExploreListViewPagerFragement();

       final String title = channel.getChannel_name();

       Bundle bundle = new Bundle();
       bundle.putString("title", title);
       bundle.putInt("counter", counter);

       f.setArguments(bundle);
       return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.explore_list_item2_horizontal_item, container, false);

        String title = "";
        int counter = 0;
        title = (String) getArguments().getString("title");
        final TextView title_textview = (TextView) rootView.findViewById(R.id.title);
        final RelativeLayout back = (RelativeLayout) rootView.findViewById(R.id.back_rl);
        counter = (int) getArguments().get("counter");

        title_textview.setText(title);


        switch (counter) {
            case 0:
               back.setBackgroundColor(Color.parseColor("#B24CA9FF"));
                break;
            case 1:
                back.setBackgroundColor(Color.parseColor("#B275C887"));
                break;
            case 2:
                back.setBackgroundColor(Color.parseColor("#B2A6A6A6"));
                break;
            default:
                back.setBackgroundColor(Color.parseColor("#B24CA9FF"));
                break;
        }

        return rootView;
    }
}
