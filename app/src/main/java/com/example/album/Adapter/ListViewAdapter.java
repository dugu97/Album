package com.example.album.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.album.R;
import com.example.album.bean.ListViewItem;

import java.io.File;

/**
 * Created by 独步清风 on 2017/3/5.
 */

public class ListViewAdapter extends ArrayAdapter<ListViewItem>{

    private int resourceId;

    public ListViewAdapter(Context context,int textViewResourceId, ListViewItem[] objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItem imageFolder = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_first);
            viewHolder.bucket_Name = (TextView) view.findViewById(R.id.textView_bucketName);
            viewHolder.photo_Num = (TextView) view.findViewById(R.id.textView_PhotoNum);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        File file = new File(imageFolder.getFirstImagePath());
        Glide.with(getContext())
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(viewHolder.imageView);
        viewHolder.bucket_Name.setText(imageFolder.getBucket_Name());
        viewHolder.photo_Num.setText(imageFolder.getPhoto_Num()+ "张照片");
        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView bucket_Name;
        TextView photo_Num;
    }
}
