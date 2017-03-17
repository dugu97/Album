package com.example.album.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.album.adapter.ListViewAdapter;
import com.example.album.R;
import com.example.album.util.FileIoUtil;
import com.example.album.util.ImageDataUtil;
import com.example.album.bean.ListViewItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListView_MainActivity extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private ListViewItem[] items;
    private Uri imageUri;
    private File outputImage;
    private String firstImagePath;

    public static final int TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        listView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listViewAdapter.notifyDataSetChanged();
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
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ListViewItem listViewItem = items[position];
        firstImagePath = listViewItem.getFirstImagePath();
        String parent_Path = firstImagePath.substring(0,firstImagePath.lastIndexOf(File.separator));
        outputImage = new File(parent_Path, "tempImage.jpg");
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,TAKE_PHOTO); // 启动相机程序
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    List<File> image = new ArrayList<>();
                    image.add(outputImage);
                    FileIoUtil fileIoUtil = new FileIoUtil(this,image);
                    if (fileIoUtil.copyPhotoFromCamera()){
                        Toast.makeText(this,"图片存储成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this,GridViewActivity.class);
                        intent.putExtra("firstImagePath",firstImagePath);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
        }
    }
}
