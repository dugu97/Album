package com.example.album.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 独步清风 on 2017/3/5.
 */

public class ImageFolder {

    /**
     * 第一张图片的路径
     */
    private String firstImagePath;

    /**
     * 文件夹的名称
     */
    private String bucket_Name;


    /**
     * 文件夹的id
     */
    private int bucket_id;

    /**
     * 图片的数量
     */
    private String photo_Num;

    /**
     * 该文件夹下所有图片路径的集合
     */
    private List<File> imageList = new ArrayList<>();


    public int getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(int bucket_id) {
        this.bucket_id = bucket_id;
    }

    public List<File> getImageList() {
        return imageList;
    }

    // 添加一个imagePath到imageList里
    File file;
    public void buildImageList(String imagePath){
        file =  new File(imagePath);
        imageList.add(file);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = imageList.get(0).toString();
    }

    public void setBucket_Name(String bucket_Name) {
        this.bucket_Name = bucket_Name;
    }

    public String getBucket_Name() {
        return bucket_Name;
    }

    public String getPhoto_Num() {
        int num = imageList.size();
        String photo_Num = Integer.toString(num);
        return photo_Num;
    }


}
