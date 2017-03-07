package com.example.album.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.album.Adapter.ListViewAdapter;
import com.example.album.R;
import com.example.album.Util.ImageUtil;
import com.example.album.bean.ImageFolder;
import com.example.album.bean.ListViewItem;

import java.util.List;

public class ListView_MainActivity extends Activity {

    List<ImageFolder> folders;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ListViewItem[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        listView = (ListView) findViewById(R.id.list_view);
        initListViewAdapter();
        if (items != null) {
            listViewAdapter = new ListViewAdapter(ListView_MainActivity.this, R.layout.listview_item_layout, items);
        }
        listView.setAdapter(listViewAdapter);
    }

    public void initListViewAdapter() {
        ImageUtil imageUtil = new ImageUtil(ListView_MainActivity.this);
        items = imageUtil.getListViewAdapterData();
    }
}
