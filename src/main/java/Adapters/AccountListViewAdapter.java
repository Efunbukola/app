package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saboorsalaam.veed10.R;

import java.util.Random;

import UtilityClasses.ValueHolder;

/**
 * Created by Saboor Salaam on 7/4/2015.
 */
public class AccountListViewAdapter extends BaseAdapter {
    Context context;
    Random rand = new Random();
    public AccountListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return ValueHolder.ACCOUNT_LIST.length;
    }

    public String getTitle(int position) {
        return ValueHolder.ACCOUNT_LIST[position];
    }

    @Override
    public Object getItem(int position) {
        return ValueHolder.ACCOUNT_LIST[position];
    }

    public int getIconFromItem(int position){
        return ValueHolder.ACCOUNT_LIST_ICONS[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.account_list_item, parent, false);

        TextView title = (TextView) rowView.findViewById(R.id.title);
        ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
        TextView badge = (TextView) rowView.findViewById(R.id.badge);

        title.setText(getTitle(position));
        icon.setImageResource(getIconFromItem(position));
        badge.setText((rand.nextInt((50 - 5) + 1) + 5) + "");
        return rowView;
    }
}
