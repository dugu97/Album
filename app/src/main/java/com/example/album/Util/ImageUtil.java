package com.example.album.Util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.album.bean.ListViewItem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 独步清风 on 2017/3/5.
 */

public class ImageUtil {

    Context context;
    ListViewItem[] listViewItems;
//    List<ImageFolder> folders = new ArrayList<>();


    public ImageUtil(Context context) {
        this.context = context;
    }

    public ListViewItem[] getListViewAdapterData() {

        Set<String> bucketSet = new HashSet<>();  //用于目录名过滤
        Set<File> absolutePath = new HashSet<>();
        List<ListViewItem> items = new ArrayList<>();
        String firstImagePath;
        String bucket_Name;
        File parentFile_Item;
        int parentFile_ImageNum;

        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Thumbnails.DATA}, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC ");

        while (cursor.moveToNext()) {
            bucket_Name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
            firstImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            parentFile_Item = new File(firstImagePath).getParentFile();
            if (!bucketSet.contains(bucket_Name) || !absolutePath.contains(parentFile_Item)) {

                firstImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                parentFile_Item = new File(firstImagePath).getParentFile();
                parentFile_ImageNum = getAbsoluteImageNum(parentFile_Item);
                ListViewItem listViewItem = new ListViewItem(firstImagePath, bucket_Name, parentFile_ImageNum + "");
                items.add(listViewItem);

                absolutePath.add(new File(firstImagePath).getParentFile());
                bucketSet.add(bucket_Name);
            }
        }
        cursor.close();
        listViewItems = new ListViewItem[items.size()];
        for (int i = 0; i < listViewItems.length; i++) {
            listViewItems[i] = items.get(i);
        }
        Arrays.sort(listViewItems);
        return listViewItems;
    }

    public int getAbsoluteImageNum(File parentFile_Item) {

        String[] parentFile_ItemNum = parentFile_Item.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        });
        return parentFile_ItemNum.length;

    }
//
//    //获取相册分类合集
//    public List<ImageFolder> getImageUrlSet() {
//        Cursor cursor = context.getContentResolver()
//                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
//                        MediaStore.Images.Thumbnails.DATA,
//                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
//                        MediaStore.Images.ImageColumns.BUCKET_ID,
//                }, null, null, null);
//        while (cursor.moveToNext()) {
//            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
//            String bucket_Name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME));
//            int bucket_Id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID));
//            //将图片路径分类存储
//            saveData(imagePath,bucket_Name,bucket_Id);
//        }
//        cursor.close();
//        return folders;
//    }
//
//    public void saveData(String imagePath,String bucket_Name,int bucket_Id){
//        if (folders.isEmpty()){
//            buildImageFolder(imagePath,bucket_Name,bucket_Id);
//        }else{
//            for (int i = 0; i < folders.size(); i++) {
//                if (folders.get(i).getBucket_id()!=bucket_Id){
//                    buildImageFolder(imagePath,bucket_Name,bucket_Id);
//                }else {
//                    folders.get(i).buildImageList(imagePath);
//                    return;
//                }
//            }
//        }
//    }
//
//    public void buildImageFolder(String imagePath,String bucket_Name,int bucket_Id){
//        ImageFolder imageFolder = new ImageFolder();
//        imageFolder.setBucket_id(bucket_Id);
//        imageFolder.setBucket_Name(bucket_Name);
//        imageFolder.buildImageList(imagePath);
//        folders.add(imageFolder);
//    }

}
