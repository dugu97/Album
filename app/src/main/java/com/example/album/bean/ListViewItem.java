package com.example.album.bean;

/**
 * Created by 独步清风 on 2017/3/5.
 */

public class ListViewItem implements Comparable<ListViewItem> {

    private String bucket_Name;

    private String photo_Num;

    private String firstImagePath;

    public ListViewItem(String firstImagePath, String bucket_Name, String photo_Num) {
        this.firstImagePath = firstImagePath;
        this.bucket_Name = bucket_Name;
        this.photo_Num = photo_Num;
    }

    public String getBucket_Name() {
        return bucket_Name;
    }

    public String getPhoto_Num() {
        return photo_Num;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    //ListViewItem按照图片数目从高到底排序，目录按照字符串从大到小排序
    @Override
    public int compareTo(ListViewItem o) {

        int o1 = Integer.valueOf(this.getPhoto_Num());
        int o2 = Integer.valueOf(o.getPhoto_Num());

        if (o1 > o2) {
            return -1;
        } else if (o1 < o2) {
            return 1;
        } else if (this.getBucket_Name().compareTo(o.getBucket_Name()) > 0) {
            return 1;
        } else if (this.getBucket_Name().compareTo(o.getBucket_Name()) < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
