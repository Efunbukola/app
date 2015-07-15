package Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.saboorsalaam.veed10.InsideChannelActivity;
import com.example.saboorsalaam.veed10.R;



import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import DragSortAdapter.DragSortAdapter;
import DragSortAdapter.NoForegroundShadowBuilder;
import DragSortAdapter.DragSortShadowBuilder;


import ParseClasses.Channel;

/**
 * Created by Saboor Salaam on 7/10/2015.
 */
public class ChannelDragSortRecyclerAdapter extends DragSortAdapter<ChannelDragSortRecyclerAdapter.MainViewHolder> {

    private List<ChannelwID> channels = new ArrayList<>();
    private Context context;
    private static RelativeLayout.LayoutParams ImageViewLayoutParams;
    private List<RelativeLayout.LayoutParams> layout_params = new ArrayList<>();
    private final static int types_of_dimen = 3;
    private int count = 0;

    public ChannelDragSortRecyclerAdapter(RecyclerView recyclerView, List<Channel> channels, Context context,@NonNull int item_height) {
        super(recyclerView);
        this.context = context;
        for(int i = 0; i < channels.size();i++){
            this.channels.add(new ChannelwID(channels.get(i),i));
        }
        layout_params.add(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item_height/2));
        layout_params.add(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item_height/3));

        ImageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item_height);
    }


    @Override public long getItemId(int position) {
        return channels.get(position).id;
    }

    @Override public int getItemCount() {
        return channels.size();
    }

    @Override public int getPositionForId(long id) {
        return getIndexOfId(channels, id);
    }

    @Override public boolean move(int fromPosition, int toPosition) {
        channels.add(toPosition, channels.remove(fromPosition));
        return true;
    }



    @Override public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dragsort_recyclerview_cell_layout, parent, false);
        MainViewHolder holder = new MainViewHolder(this, view, context);
        view.setOnClickListener(holder);
        view.setOnLongClickListener(holder);
        return holder;
    }

    @Override public void onBindViewHolder(final MainViewHolder holder, final int position) {
        int itemId = channels.get(position).id;
/*
        if(position%2==0) {
            holder.container.setLayoutParams(layout_params.get(0));
        }else{
            holder.container.setLayoutParams(layout_params.get(1));
        }
        */

        String cover_thumb = channels.get(position).channel.getCover_thumb();
        String cover_title = channels.get(position).channel.getCover_title();
        String channel_title = channels.get(position).channel.getChannel_name();

        Picasso.with(context).load(cover_thumb).centerCrop().fit().into(holder.cover, new Callback() {
            @Override
            public void onSuccess() {
                holder.cover.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

        holder.cover_title.setText(cover_title);
        holder.title.setText(channel_title);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InsideChannelActivity.class);
                intent.putExtra("channel_id", channels.get(position).channel.getChannel_id());
                intent.putExtra("channel_name", channels.get(position).channel.getChannel_name());
                context.startActivity(intent);
            }
        });

        // NOTE: check for getDraggingId() match to set an "invisible space" while dragging
        holder.container.setVisibility(getDraggingId() == itemId ? View.INVISIBLE : View.VISIBLE);
        holder.container.postInvalidate();


    }

    static class MainViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {


        RelativeLayout cover_holder;
        ImageView cover;
        TextView title;
        TextView cover_title;
        ProgressBar progressBar;
        ViewGroup container;
        View header;
        Context context;

        public MainViewHolder(DragSortAdapter adapter, View itemView, Context context) {
            super(adapter, itemView);

            this.context = context;
            container = (ViewGroup) itemView.findViewById(R.id.container);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
            cover_title = (TextView) itemView.findViewById(R.id.cover_title);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progbar);
            cover_holder = (RelativeLayout) itemView.findViewById(R.id.cover_holder);
            header = itemView.findViewById(R.id.header);
            cover.setLayoutParams(ImageViewLayoutParams);
        }

        @Override
        public void onClick(@NonNull View v) {

        }

        @Override
        public boolean onLongClick(@NonNull View v) {
            startDrag();
            return true;
        }

        @Override public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
            return new DragSortShadowBuilder(itemView, touchPoint);
        }
    }


    public class ChannelwID
    {
        public ChannelwID(Channel channel, int id) {
            this.channel = channel;
            this.id = id;
        }

        Channel channel;
        int id;
    }

    int getIndexOfId(List<ChannelwID> items, long id)
    {
        for(int i = 0; i < items.size();i++)
        {
            if(items.get(i).id == (int) id)
            {
                return i;
            }
        }

        return 0;
    }

}