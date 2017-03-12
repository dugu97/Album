package com.example.album.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.album.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by 独步清风 on 2017/3/10.
 */

public class FileIoUtil implements DialogInterface.OnClickListener{

    private Context context;
    private List<File> selectedImagesFileSet;
    private String newNameFromEditText;
    private String startNumberFromEditText;
    private String numDigitsFromEditText;
    private MediaScannerConnection myMediaConnection;

    public FileIoUtil(Context context, List<File> selectedImagesFileSet) {
        this.context = context;
        this.selectedImagesFileSet = selectedImagesFileSet;
    }

    //删除后要发送广播通知系统,或者直接contentProvider里删除
    public Boolean deleteFiles() {
        for (int i = 0; i < selectedImagesFileSet.size(); i++) {

            File selectImage = selectedImagesFileSet.get(i);

            if (selectImage.exists()) {
                selectImage.delete();
                ContentResolver resolver = context.getContentResolver();
                resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{selectImage.toString()});
            }
        }
        return true;
    }

    public Boolean copyToThisFiles() {

        File file1 = selectedImagesFileSet.get(0);
        File file = file1.getParentFile();
        String parentFile_Path = file.toString() + File.separator;
        File file2 = new File(parentFile_Path + "456.jpg"); //特别注意此处的文件名
        for (int i = 0; i < selectedImagesFileSet.size(); i++) {
            copySingleImage(selectedImagesFileSet.get(0),file2,true);
        }
        return true;
    }

    public void copySingleImage(File fromFile, final File toFile, Boolean rewrite){
        if(!fromFile.exists()){
            return;
        }
        if(!fromFile.isFile()){
            return;
        }
        if(!fromFile.canRead()){
            return;
        }
        if(!toFile.getParentFile().exists()){
            toFile.getParentFile().mkdirs();
        }
        if(toFile.exists() && rewrite){
            toFile.delete();
        }
//图片格式
        int index = toFile.toString().lastIndexOf(File.separator);
        String allNameWithFormat = toFile.toString().substring(index + 1);

        try {
            FileInputStream fosFromFile = new FileInputStream(fromFile);
            FileOutputStream fosToFile = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosFromFile.read(bt)) > 0) {
                fosToFile.write(bt, 0, c);
            }
            //关闭输入、输出流
            fosFromFile.close();
            fosToFile.close();

//插入图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(),toFile.getAbsolutePath(), allNameWithFormat, null);

            ContentResolver resolver = context.getContentResolver();

            long dateTaken = System.currentTimeMillis();
            ContentValues values = new ContentValues(7);
            values.put(MediaStore.Images.Media.TITLE, allNameWithFormat);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, allNameWithFormat);
            values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg/gif/jpg/bmp/png");
            values.put(MediaStore.Images.Media.DATA, toFile.toString());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // 最后通知图库更新
            try {
                myMediaConnection = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                        myMediaConnection.scanFile(toFile.toString(),toFile.toString().substring(toFile.toString().lastIndexOf(File.separator) + 1));
                    }
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        myMediaConnection.disconnect();
                    }
                });
                myMediaConnection.connect();
            }catch (Exception e){
                return;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Boolean moveFiles() {
        return true;
    }

    public Boolean copyToOtherFiles() {
        return true;
    }

    public Boolean renameFiles() {
        getRenameDataFromDialog();
//        newNameFromEditText;
//        startNumberFromEditText;
//        numDigitsFromEditText;

        return true;
    }

    private void getRenameDataFromDialog() {

        View renameDataView = LayoutInflater.from(context).inflate(R.layout.table_dialog_data, null);
        Dialog dialog = new AlertDialog.Builder(context)
                .setView(renameDataView)
                .setTitle("输入重命名所需数据")
                .setPositiveButton("确定",this)
                .create();
        dialog.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        View renameDataView = LayoutInflater.from(context).inflate(R.layout.table_dialog_data, null);
        EditText newName = (EditText) renameDataView.findViewById(R.id.NewNameData);
        EditText startNumber = (EditText) renameDataView.findViewById(R.id.startNumber);
        EditText numDigits = (EditText) renameDataView.findViewById(R.id.numberDigits);
        newNameFromEditText = newName.getText().toString();
        startNumberFromEditText = startNumber.getText().toString();
        numDigitsFromEditText = numDigits.getText().toString();
    }
}
