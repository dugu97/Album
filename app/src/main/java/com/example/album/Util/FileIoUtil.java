package com.example.album.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.album.Activity.GridViewActivity;
import com.example.album.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 独步清风 on 2017/3/10.
 */

public class FileIoUtil {

    private Context context;
    private List<File> selectedImagesFileSet;
    private String newNameFromEditText;
    private int startNumberFromEditText;
    private int numDigitsFromEditText;

    private String regular = ".*_[\\u4e00-\\u9fa5][\\u4e00-\\u9fa5]\\([\\d]{4}-[\\d]{2}-[\\d]{2}-[\\d]{2}-[\\d]{2}-[\\d]{2}-[\\d]{3}\\)\\..*";
    private Boolean MULTI_SELECT = null;

    private EditText newName;
    private EditText startNumber;
    private EditText numDigits;

    private final Handler handler;
    private ProgressDialog progressDialog;
    private final int RENAME_FILES = 1;
    private final int RENAME_SINGLE_FILE = 2;
    private final int DISMISS_PROGRESS_DIALOG = 3;
    private String firstImagePath;


    public FileIoUtil(final Context context, List<File> selectedImagesFileSet) {

        this.context = context;
        this.selectedImagesFileSet = selectedImagesFileSet;

        if (selectedImagesFileSet.size() > 1) {
            this.MULTI_SELECT = true;
        } else {
            if (selectedImagesFileSet.size() == 1) {
                this.MULTI_SELECT = false;
            }
        }
        progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == RENAME_FILES) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            renameOperate();
                        }
                    }).start();

                } else if (msg.what == RENAME_SINGLE_FILE) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            renameSingleFile();
                        }
                    }).start();
                } else {
                    if (msg.what == DISMISS_PROGRESS_DIALOG) {
                        progressDialog.dismiss();
                        GridViewActivity.gridViewActivityFinish.finish();
                        GridViewActivity.gridViewActivityFinish.reFleshGridViewActivity(firstImagePath);
                        Toast.makeText(context,"请返回进行刷新",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

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

        SimpleDateFormat format = new SimpleDateFormat("副本(yyyy-MM-dd-HH-mm-ss-SSS)");
        String time;
        File fromFile;
        String fileFormat;
        File toFile;
        String parentFile_Path;
        int i;

        for (i = 0; i < selectedImagesFileSet.size(); i++) {
            time = format.format(Calendar.getInstance().getTime());
            fromFile = selectedImagesFileSet.get(i);
            parentFile_Path = fromFile.getParentFile().toString() + File.separator;
            fileFormat = fromFile.toString().substring(fromFile.toString().lastIndexOf("."));

            if (!(fromFile.getName().matches(regular))) {
                toFile = new File(parentFile_Path, fromFile.getName() + "_" + time + fileFormat);
                copySingleImage(fromFile, toFile, false);
                continue;
            }
            if (fromFile.getName().matches(regular)) {
                String oldName = fromFile.getName().substring(0, fromFile.getName().length() - 24 - fileFormat.length());
                toFile = new File(parentFile_Path, oldName + "_" + time + fileFormat);
                copySingleImage(fromFile, toFile, false);
            }
        }
        if (i == selectedImagesFileSet.size())
            return true;
        else
            return false;
    }

    public void copySingleImage(File fromFile, final File toFile, Boolean rewrite) {
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
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


            ContentResolver resolver = context.getContentResolver();

            long dateTaken = System.currentTimeMillis();
            ContentValues values = new ContentValues(7);
            values.put(MediaStore.Images.Media.TITLE, allNameWithFormat);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, allNameWithFormat);
            values.put(MediaStore.Images.Media.DATE_TAKEN, dateTaken);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg/gif/jpg/bmp/png");
            values.put(MediaStore.Images.Media.DATA, toFile.toString());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Uri uri = Uri.fromFile(toFile);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean copyToOtherFiles(String parentFile_Path) {

        File fromFile;
        File toFile;
        parentFile_Path = parentFile_Path + File.separator;

        for (int i = 0; i < selectedImagesFileSet.size(); i++) {

            fromFile = selectedImagesFileSet.get(i);
            toFile = new File(parentFile_Path, fromFile.getName());
            if (!toFile.exists()) {
                copySingleImage(fromFile, toFile, false);
            } else {
                SimpleDateFormat format = new SimpleDateFormat("副本(yyyy-MM-dd-HH-mm-ss-SSS)");
                String time = format.format(Calendar.getInstance().getTime());
                String fileFormat = fromFile.toString().substring(fromFile.toString().lastIndexOf("."));
                if (!(toFile.getName().matches(regular))) {
                    toFile = new File(parentFile_Path, fromFile.getName() + "_" + time + fileFormat);
                } else {
                    if (fromFile.getName().matches(regular)) {
                        String oldName = fromFile.getName().substring(0, fromFile.getName().length() - 24 - fileFormat.length());
                        toFile = new File(parentFile_Path, oldName + "_" + time + fileFormat);
                    }
                }
                copySingleImage(fromFile, toFile, false);
            }
        }
        return true;

    }

    public void renameFiles() {
        getRenameDataFromDialog();
    }

    private void showProgressDialog() {
        progressDialog.setCancelable(false);
        progressDialog.setTitle("正在执行操作 ");
        progressDialog.show();
    }

    private void getRenameDataFromDialog() {

        final View renameDataView = LayoutInflater.from(context).inflate(R.layout.table_dialog_data, null);

        newName = (EditText) renameDataView.findViewById(R.id.NewNameData);
        startNumber = (EditText) renameDataView.findViewById(R.id.startNumber);
        numDigits = (EditText) renameDataView.findViewById(R.id.numberDigits);

        Dialog dialog = new AlertDialog.Builder(context)
                .setView(renameDataView)
                .setTitle("输入重命名所需数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        showProgressDialog();

                        if (MULTI_SELECT) {

                            if (newName.getText().toString().trim().equals("") || startNumber.getText().toString().trim().equals("") || numDigits.getText().toString().trim().equals("")) {
                                Toast.makeText(context, "批量重命名时数据不能为空", Toast.LENGTH_LONG).show();
                                renameFiles();
                                return;
                            }

                            newNameFromEditText = newName.getText().toString();
                            startNumberFromEditText = Integer.valueOf(startNumber.getText().toString());
                            numDigitsFromEditText = Integer.valueOf(numDigits.getText().toString());

                            if (selectedImagesFileSet.size() > (Math.pow(10, numDigitsFromEditText) - startNumberFromEditText)) {
                                Toast.makeText(context, "批量重命名时,编号位数不足", Toast.LENGTH_LONG).show();
                                renameFiles();
                                return;
                            }

                            handler.sendEmptyMessage(RENAME_FILES);

                        } else {
                            if (newName.getText().toString().trim().equals("")) {
                                Toast.makeText(context, "单文件重命名，新名字不可为空", Toast.LENGTH_LONG).show();
                                renameFiles();
                                return;
                            }

                            newName = (EditText) renameDataView.findViewById(R.id.NewNameData);
                            newNameFromEditText = newName.getText().toString();

                            handler.sendEmptyMessage(RENAME_SINGLE_FILE);

                            return;
                        }

                    }
                })
                .create();

        if (!MULTI_SELECT) {
            startNumber.setFocusable(false);
            startNumber.setFocusableInTouchMode(false);
            numDigits.setFocusable(false);
            numDigits.setFocusableInTouchMode(false);
            Toast.makeText(context, "单个重命名只可输入新名字", Toast.LENGTH_LONG).show();
        }

        dialog.show();
    }


    private Boolean renameOperate() {

        int startNum = startNumberFromEditText;
        //重命名操作
        for (int i = 0; i < selectedImagesFileSet.size(); i++) {
            File image = selectedImagesFileSet.get(i);
            String parent_Path = image.getParent() + File.separator;
            String imageFormat = image.toString().substring(image.toString().lastIndexOf("."));
            String numFormat = "";
            for (int j = 0; j < numDigitsFromEditText; j++) {
                numFormat = numFormat + "0";
            }
            DecimalFormat df = new DecimalFormat(numFormat);

            String newName = new String(newNameFromEditText + df.format(startNum) + imageFormat);
            File newFileName = new File(parent_Path + newName);

            if (i == 0){
                firstImagePath = newFileName.toString();
            }

            copySingleImage(image,newFileName,false);

            startNum++;

            Uri uri = Uri.fromFile(newFileName);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            image.delete();
            ContentResolver resolver = context.getContentResolver();
            resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{image.toString()});
        }

        handler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);
        return true;
    }

    public Boolean renameSingleFile() {

        File image = selectedImagesFileSet.get(0);
        String imageFormat = image.toString().substring(image.toString().lastIndexOf("."));
        String parent_Path = image.getParent() + File.separator;
        String newName = new String(newNameFromEditText + imageFormat);
        File newFileName = new File(parent_Path + newName);

        firstImagePath = newFileName.toString();

        copySingleImage(image,newFileName,false);

        //广播通知还有问题
        Uri uri = Uri.fromFile(newFileName);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        image.delete();
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{image.toString()});

        handler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG);

        return true;
    }
}
