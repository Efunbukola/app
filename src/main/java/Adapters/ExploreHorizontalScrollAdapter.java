package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saboorsalaam.veed10.R;

import java.util.ArrayList;
import java.util.List;

import ParseClasses.Channel;


public class ExploreHorizontalScrollAdapter extends BaseAdapter {

    private List<Channel> channels = new ArrayList<>();
    private Context context;
    private int counter = 0;

    public ExploreHorizontalScrollAdapter(List<Channel> channels, Context context) { this.channels = channels; this.context = context;};

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public Channel getItem(int position) {
        return channels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.explore_list_item2_horizontal_item, null);
        }

        String title = (String) getItem(position).getChannel_name();
        final TextView title_textview = (TextView) convertView.findViewById(R.id.title);
        final RelativeLayout back = (RelativeLayout) convertView.findViewById(R.id.back_rl);
        title_textview.setText(title);

        switch (counter) {
            case 0:
                back.setBackgroundColor(Color.parseColor("#B24CA9FF"));
                counter++;
                break;
            case 1:
                back.setBackgroundColor(Color.parseColor("#B275C887"));
                counter++;
                break;
            case 2:
                back.setBackgroundColor(Color.parseColor("#B2A6A6A6"));
                counter = 0;
                break;
            default:
                back.setBackgroundColor(Color.parseColor("#B24CA9FF"));
                counter = 0;
                break;
        }

            return convertView;
        }
    }


