package com.example.album.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.album.R;

import java.io.File;
import java.util.List;

/**
 * Created by 独步清风 on 2017/3/8.
 */

public class AdapterViewFlipperAdapter extends BaseAdapter {

    private Context context;
    private List<File> imagesData;
    private int firstDisplayImage;
    private Boolean is = true;

    public AdapterViewFlipperAdapter(Context context, List<File> imagesData, int firstDisplayImage){
        this.context = context;
        this.imagesData = imagesData;
        this.firstDisplayImage = firstDisplayImage;
    }

    @Override
    public int getCount() {
        return imagesData.size();
    }

    @Override
    public Object getItem(int position) {
        return imagesData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null){
            if (is) {
                position = firstDisplayImage;
                is = false;
            }
            convertView = LayoutInflater.from(context).inflate(R.layout.showimages_layout,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).toString().endsWith(".gif")){
            Glide.with(context)
                    .load((File) getItem(position)).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .animate(R.anim.rotate_out)
                    .into(viewHolder.imageView);
        }else {
            Glide.with(context)
                    .load((File) getItem(position))
                    .centerCrop().animate(R.anim.showimage_zoom_in)
                    .into(viewHolder.imageView);
        }
        String s = position + "";
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
        private ViewHolder(View convertView){
            imageView = (ImageView) convertView.findViewById(R.id.showImageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

}
