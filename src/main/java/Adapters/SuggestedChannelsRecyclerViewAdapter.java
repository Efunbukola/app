package Adapters;

/**
 * Created by Saboor Salaam on 6/11/2015.
 */
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saboorsalaam.veed10.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ParseClasses.Channel;

public class SuggestedChannelsRecyclerViewAdapter extends RecyclerView.Adapter<SuggestedChannelsRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private List<Channel> channels = new ArrayList<>();
    private int counter = 0;
    private int width, height;

    public SuggestedChannelsRecyclerViewAdapter(Context context, List<Channel> channels, int width, int height) {
        this.width = width;
        this.height = height;
        this.context = context;
        this.channels = channels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.explore_list_item2_horizontal_item, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title_textview.setText(channels.get(position).getChannel_name());
        holder.header.setVisibility((position==0) ? View.VISIBLE :View.GONE);
        Picasso.with(context).load(channels.get(position).cover_thumb).centerCrop().fit().into(holder.cover, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title_textview;
        final LinearLayout root;
        final ImageView cover;
        final View header;

        public ViewHolder(View view) {
            super(view);
            header = view.findViewById(R.id.header);
             title_textview = (TextView) view.findViewById(R.id.title);
            root = (LinearLayout) view.findViewById(R.id.root);
            cover = (ImageView) view.findViewById(R.id.cover);
        }

    }
}

