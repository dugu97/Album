package com.example.album.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.album.Adapter.GridViewAdapter;
import com.example.album.R;
import com.example.album.bean.ImageFolder;

import java.util.List;

public class GridViewActivity extends Activity {

    List<ImageFolder> list;
    GridViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview);
//        initPhoto();
//        getImage();
//        myAdapter = new GridViewAdapter(this,list);
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//        gridView.setAdapter(myAdapter);

    }

//    public void getImage(){
//        Cursor cursor = getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
//        //遍历相册
//        while (cursor.moveToNext()) {
//            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            File file = new File(path);
//            //将图片路径添加到集合
//            list.add(file);
//        }
//        cursor.close();
//    }

}
