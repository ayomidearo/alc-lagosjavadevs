package me.ayomidearo.lagosjavadevs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.ayomidearo.lagosjavadevs.R;
import me.ayomidearo.lagosjavadevs.activity.DevProfileActivity;
import me.ayomidearo.lagosjavadevs.model.Devs;

/**
 * Created by aro on 3/10/17.
 */

public class DevListAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<Devs> devs;
    private static LayoutInflater inflater=null;

    public DevListAdapter(Context context, ArrayList<Devs> devs){
        this.context = context;
        this.devs = devs;

    }

    @Override
    public int getCount() {
        return devs.size();
    }

    @Override
    public Object getItem(int position) {
        return devs.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.dev_list_item, viewGroup, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Devs currentItem = (Devs) getItem(i);


        viewHolder.username.setText(currentItem.getUsername());

        Picasso.with(context).load(currentItem.getProfileImage())
                .noFade()
                .error(R.drawable.ic_empty_profile)
                .placeholder(R.drawable.ic_empty_profile)
                .into(viewHolder.circleImageView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent iii = new Intent(context, DevProfileActivity.class);
                iii.putExtra("USERNAME", currentItem.getUsername());
                iii.putExtra("PROFILEIMAGE", currentItem.getProfileImage());
                iii.putExtra("PROFILEURL", currentItem.getProfileUrl());
                iii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(iii);

            }
        });

        // returns the view for the current row
        return convertView;

    }

    private class ViewHolder {
        TextView username;
        CircleImageView circleImageView;

        public ViewHolder(View view) {
            username = (TextView)view.findViewById(R.id.profile_username);
            circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);
        }
    }
}
