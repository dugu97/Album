package com.example.album.Util;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

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

public class ImageDataUtil {

    private Context context;

    public ImageDataUtil(Context context) {
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

//        系统相册图片和其他目录下的所有图片，并按照时间倒叙排列
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                                MediaStore.Images.Thumbnails.DATA}, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC ");
        if (cursor != null) {
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
        }

        ListViewItem[] listViewItems = new ListViewItem[items.size()];
        for (int i = 0; i < listViewItems.length; i++) {
            listViewItems[i] = items.get(i);
        }
        Arrays.sort(listViewItems);
        return listViewItems;
    }

    private int getAbsoluteImageNum(File parentFile_Item) {

        String[] parentFile_ItemNum = parentFile_Item.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return (filename.endsWith(".jpg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".jpeg")
                        || filename.endsWith(".gif")
                        || filename.endsWith(".bmp"));
            }
        });
        return parentFile_ItemNum.length;

    }

    //获取相册分类合集
    public List<File> getGridViewFolderData(File firstImagePath) {

        List<File> folderImages = new ArrayList<>();

        //selection: 指定查询条件
        String selection = MediaStore.Images.Thumbnails.DATA + " like ?";
        //设定查询目录
        File file = firstImagePath.getParentFile();
        String parentFile_Path = file.toString() + File.separator;
        //定义selectionArgs：
        String[] selectionArgs = {parentFile_Path + "%"};

//        系统相册图片和其他目录下的所有图片，并按照时间倒叙排列
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                File image = new File(imagePath);
                folderImages.add(image);
            }
            cursor.close();
        }

        return folderImages;
    }

    public int getDisplay() {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int result = (dm.widthPixels - 18) / 3;
        return result;
    }

}
