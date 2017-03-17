package com.example.album.bean;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.album.R;
import com.example.album.util.ImageDataUtil;

import java.io.File;

import static com.example.album.R.id.imageView;

/**
 * Created by 独步清风 on 2017/3/10.
 */

public class GridViewItem extends RelativeLayout implements Checkable{

    private Context mContext;
    private boolean mChecked;//判断该选项是否被选上的标志量
    private ImageView mImageView = null;
    private ImageView mSelectView = null;
    private LayoutParams params;

    public GridViewItem(Context context) {
        this(context, null, 0);
        ImageDataUtil imageDataUtil = new ImageDataUtil(context);
        this.params = new LayoutParams(imageDataUtil.getDisplay()/4,imageDataUtil.getDisplay()/4);
    }

    public GridViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }



    public GridViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.gridview_item_layout, this);
        mImageView = (ImageView) findViewById(imageView);
        mSelectView = (ImageView) findViewById(R.id.select);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundDrawable(checked ? getResources().getDrawable(R.drawable.background) : null);
        mSelectView.setLayoutParams(params);
        mSelectView.setVisibility(checked ? View.VISIBLE : View.GONE);//选上了则显示小勾图片
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setImageView(File imagePath) {

        if (mImageView != null) {
            if (imagePath.toString().endsWith(".gif")){
                Glide.with(getContext())
                        .load(imagePath).asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .animate(R.anim.showimage_zoom_in)
                        .into(mImageView);
            }
            Glide.with(getContext())
                    .load(imagePath)
                    .centerCrop().animate(R.anim.rotate_out)
                    .into(mImageView);
        }
    }
}
