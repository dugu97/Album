package com.example.album.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.example.album.util.ImageDataUtil;
import com.example.album.bean.GridViewItem;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by 独步清风 on 2017/2/22.
 */

public class GridViewAdapter extends BaseAdapter {

    private Map<Integer, Boolean> mSelectMap = null;
    private Context context = null;
    private List<File> images = null;

    public GridViewAdapter(Context context, List<File> images,Map<Integer, Boolean> mSelectMap) {
        this.context = context;
        this.images = images;
        this.mSelectMap = mSelectMap;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridViewItem item;
        if (convertView == null) {
            item = new GridViewItem(context);
            ImageDataUtil imageDataUtil = new ImageDataUtil(context);
            item.setLayoutParams(new AbsListView.LayoutParams(imageDataUtil.getDisplay(),imageDataUtil.getDisplay()));
        } else {
            item = (GridViewItem) convertView;
        }

        item.setImageView(images.get(position));
        item.setChecked(mSelectMap.get(position) == null ? false : mSelectMap.get(position));
        return item;
    }
}
