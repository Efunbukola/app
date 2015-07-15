package com.example.saboorsalaam.veed10;

/**
 * Author: alex askerov
 * Date: 9/9/13
 * Time: 10:52 PM
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.List;

import DynamicGridView.BaseDynamicGridAdapter;
import ParseClasses.Channel;


public class ChannelDynamicAdapter extends BaseDynamicGridAdapter {

    private static LinearLayout.LayoutParams ImageViewLayoutParams;
    private List<Channel> channels;
    private int[] empty_channel_views = {R.drawable.empty_channel_place_holder_blue, R.drawable.empty_channel_place_holder_green, R.drawable.empty_channel_place_holder_grey};
    private int counter = 0;


    public ChannelDynamicAdapter(Context context, List<?> items, int columnCount, @NonNull int item_height) {
        super(context, items, 2);
        this.ImageViewLayoutParams = new LinearLayout.LayoutParams(item_height, item_height);
        this.channels = channels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChannelViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dragsort_recyclerview_cell_layout, null);
            holder = new ChannelViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChannelViewHolder) convertView.getTag();
        }


        Field a = null;
        try {
            a = getItem(position).getClass().getField("channel_name");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        String title = "error";

        try {
            title = (String) a.get(getItem(position));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Field b = null;
        try {
            b = getItem(position).getClass().getField("cover_title");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        String cover_title = "error";

        try {
            cover_title = (String) b.get(getItem(position));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        Field c = null;
        try {
            c = getItem(position).getClass().getField("cover_thumb");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        String thumb_url = "error";

        try {
            thumb_url = (String) c.get(getItem(position));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        holder.build(title, cover_title, thumb_url, position);

        return convertView;
    }

    public class ChannelViewHolder {

        RelativeLayout cover_holder;
        ImageView cover;
        TextView title;
        TextView cover_title;
        ProgressBar progressBar;
        CardView container;
        View header;

        public ChannelViewHolder(View itemView) {
            container = (CardView) itemView.findViewById(R.id.container);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
            cover_title = (TextView) itemView.findViewById(R.id.cover_title);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progbar);
            cover_holder = (RelativeLayout) itemView.findViewById(R.id.cover_holder);
            header = itemView.findViewById(R.id.header);

            //cover.setLayoutParams(ImageViewLayoutParams);
        }

        void build(String title_info, String cover_title_info , String thumb_url, int position) {

            container.setLayoutParams(ImageViewLayoutParams);

         //header.setVisibility((position < getColumnCount()) ? View.VISIBLE :View.GONE);


        switch (counter) {
                case 0:
                    cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                    counter++;
                    break;
                case 1:
                    cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_green);
                    counter++;
                    break;
                case 2:
                    cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_grey);
                    counter=0;
                    break;
                default:
                    cover_holder.setBackgroundResource(R.drawable.empty_channel_place_holder_blue);
                    counter=1;
                    break;
            }

            Picasso.with(getContext()).load(thumb_url).centerCrop().fit().into(cover, new Callback() {
                @Override
                public void onSuccess() {
                    cover.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });

            title.setText(title_info);
            cover_title.setText(cover_title_info);

        }
    }

}