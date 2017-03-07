package com.example.album.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.album.R;

import java.io.File;
import java.util.List;

/**
 * Created by 独步清风 on 2017/2/22.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<File> list;

    public GridViewAdapter(Context context, List<File> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load((File)getItem(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(viewHolder.imageView);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        public ViewHolder(View convertView){
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(350,350);
            imageView.setLayoutParams(params);
        }
    }

}
