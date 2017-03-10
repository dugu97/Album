package com.example.album.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.album.Adapter.ListViewAdapter;
import com.example.album.R;
import com.example.album.Util.ImageDataUtil;
import com.example.album.bean.ListViewItem;

public class ListView_MainActivity extends Activity implements AdapterView.OnItemClickListener{

    ListView listView;
    ListViewAdapter listViewAdapter;
    ListViewItem[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_layout);
        listView = (ListView) findViewById(R.id.list_view);
        initListViewAdapter();
        if (items != null) {
            listViewAdapter = new ListViewAdapter(this, R.layout.listview_item_layout, items);
        }
        listView.setAdapter(listViewAdapter);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(AnimationUtils.loadAnimation(this,R.anim.zoom_in));
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_RANDOM);
        listView.setLayoutAnimation(layoutAnimationController);
        listView.startLayoutAnimation();
        listView.setOnItemClickListener(this);
    }

    public void initListViewAdapter() {
        ImageDataUtil imageDataUtil = new ImageDataUtil(this);
        items = imageDataUtil.getListViewAdapterData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListViewItem listViewItem = items[position];
        String firstImagePath = listViewItem.getFirstImagePath();
        Intent intent = new Intent(this,GridViewActivity.class);
        intent.putExtra("firstImagePath",firstImagePath);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
    }
}
