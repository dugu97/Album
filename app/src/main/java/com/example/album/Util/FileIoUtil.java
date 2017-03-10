package com.example.album.Util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.album.R;

import java.io.File;
import java.util.List;

/**
 * Created by 独步清风 on 2017/3/10.
 */

public class FileIoUtil {

    private Context context;
    private List<File> selectedImagesFileSet;

    public FileIoUtil(Context context, List<File> selectedImagesFileSet) {
        this.context = context;
        this.selectedImagesFileSet = selectedImagesFileSet;
    }

    private Boolean deleteFiles() {
        return true;
    }

    private Boolean copyFiles() {
        return true;
    }

    private Boolean moveFiles() {
        return true;
    }

    private Boolean pasteFiles(){
        return true;
    }

    private Boolean renameFiles() {
        getRenameDataFromDialog();
        return true;
    }

    public void chooseOperaterFromDialog() {

        final String[] operateSets = new String[]{"重命名", "复制", "粘贴", "删除"};
        Dialog dialog = new AlertDialog.Builder(context)
                .setItems(operateSets, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (operateSets[which].equals("重命名")) {
                            renameFiles();
                        }
                        if (operateSets[which].equals("复制")){
                            copyFiles();
                        }
                        if (operateSets[which].equals("粘贴")){
                            pasteFiles();
                        }
                        if (operateSets[which].equals("删除")) {
                            deleteFiles();
                        }
                    }
                }).create();
        dialog.show();
    }

    private void getRenameDataFromDialog(){
        final View renameDataView = LayoutInflater.from(context).inflate(R.layout.table_dialog_data,null);
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
