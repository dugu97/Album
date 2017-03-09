package com.example.album.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class GridViewAdapter extends ArrayAdapter<File> {

    private int resourceId;

    public GridViewAdapter(Context context, int resource, List<File> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        File imagePath = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(350,350);
            viewHolder.imageView.setLayoutParams(params);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(getContext())
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(viewHolder.imageView);
//        viewHolder.bucket_Name.setText(imageFolder.getBucket_Name());
//        viewHolder.photo_Num.setText(imageFolder.getPhoto_Num()+ "张照片");
        return view;
    }

    class ViewHolder {
        ImageView imageView;
    }

}
