package com.example.album.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.example.album.Adapter.GridViewAdapter;
import com.example.album.R;
import com.example.album.Util.ImageUtil;

import java.io.File;

public class GridViewActivity extends Activity {

    private GridViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview);
        initGridViewAdapter();
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(myAdapter);
    }

    public void initGridViewAdapter() {
        Intent intent = getIntent();
        String firstImagePath = intent.getStringExtra("firstImagePath");
        File file = new File(firstImagePath);
        ImageUtil imageUtil = new ImageUtil(this);
        myAdapter = new GridViewAdapter(this,imageUtil.getGridViewFolderData(file));
    }

}
