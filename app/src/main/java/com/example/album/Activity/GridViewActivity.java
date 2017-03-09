package com.example.album.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.album.Adapter.GridViewAdapter;
import com.example.album.R;
import com.example.album.Util.ImageDataUtil;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class GridViewActivity extends Activity implements AdapterView.OnItemClickListener{

    private GridViewAdapter myAdapter;
    private List<File> folderImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview);
        initGridViewAdapter();
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(myAdapter);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(AnimationUtils.loadAnimation(this,R.anim.zoom_in));
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        gridView.setLayoutAnimation(layoutAnimationController);
        gridView.startLayoutAnimation();
        gridView.setOnItemClickListener(this);
    }

    public void initGridViewAdapter() {
        Intent intent = getIntent();
        String firstImagePath = intent.getStringExtra("firstImagePath");
        File file = new File(firstImagePath);
        ImageDataUtil imageDataUtil = new ImageDataUtil(this);
        folderImages = imageDataUtil.getGridViewFolderData(file);
        myAdapter = new GridViewAdapter(this,R.layout.gridview_item_layout,folderImages);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,AdapterViewFlipperActivity.class);
        intent.putExtra("onClickImagePosition",position);
        intent.putExtra("folderImages",(Serializable) folderImages);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_out,R.anim.showimage_zoom_in);
    }
}
