package com.example.album.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.album.R;

import java.io.File;
import java.util.List;

/**
 * Created by 独步清风 on 2017/3/10.
 */

public class FileIoUtil{

    private Context context;
    private List<File> selectedImagesFileSet;

    public FileIoUtil(Context context, List<File> selectedImagesFileSet) {
        this.context = context;
        this.selectedImagesFileSet = selectedImagesFileSet;
    }

    //删除后要发送广播通知系统
    public Boolean deleteFiles() {

        String folderImages;
        ImageDataUtil imageDataUtil = new ImageDataUtil(context);

        for (int i = 0; i < selectedImagesFileSet.size(); i++) {

            File selectImage = selectedImagesFileSet.get(i);

            if (selectImage.exists()) {
                selectImage.delete();
                Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(selectImage));
                context.sendBroadcast(media);
            }

            folderImages = imageDataUtil.isExistInDatabase(selectImage);

            if (folderImages != null && folderImages.equals(selectImage.toString())) {
                i = i - 1;
            }
        }
        return true;
    }

    public Boolean copyToThisFiles(){
//        FileInputStream fosfrom = new FileInputStream(fromFile);
//        FileOutputStream fosto = new FileOutputStream(toFile);
//
//        byte[] bt = new byte[1024];
//        int c;
//        while((c=fosfrom.read(bt)) > 0){
//            fosto.write(bt,0,c);
//        }
//        //关闭输入、输出流
//        fosfrom.close();
//        try {
//            fosto.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return true;
    }

    public Boolean moveFiles() {
        return true;
    }

    public Boolean copyToOtherFiles() {
        return true;
    }

    public Boolean renameFiles() {
        getRenameDataFromDialog();
        return true;
    }

//    public void refreshView(){
//        Intent intent = new Intent(context,GridViewActivity.class);
//        context.startActivity(intent);
//    }


    private void getRenameDataFromDialog() {
        final View renameDataView = LayoutInflater.from(context).inflate(R.layout.table_dialog_data, null);
        Dialog dialog = new AlertDialog.Builder(context)
                .setView(renameDataView)
                .setTitle("输入重命名所需数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText newName = (EditText) renameDataView.findViewById(R.id.NewNameData);
                        EditText startNumber = (EditText) renameDataView.findViewById(R.id.startNumber);
                        EditText numDigits = (EditText) renameDataView.findViewById(R.id.numberDigits);


                    }
                })
                .create();
        dialog.show();

    }

}
