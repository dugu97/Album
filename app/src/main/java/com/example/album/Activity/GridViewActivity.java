package com.example.album.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.album.Adapter.GridViewAdapter;
import com.example.album.R;
import com.example.album.Util.FileIoUtil;
import com.example.album.Util.ImageDataUtil;
import com.example.album.bean.ListViewItem;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewActivity extends Activity implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener, DialogInterface.OnClickListener {

    private FileIoUtil fileIoUtil;
    private GridViewAdapter myAdapter;
    private List<File> folderImages;
    private List<File> selectedImagesFileSet;
    private GridView gridView;
    private TextView mActionText;
    private File firstImagePath;
    private String firstImagePathString;
    private ImageDataUtil imageDataUtil = new ImageDataUtil(this);
    private Map<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();

    private String OtherAlbumPath;

    private final static int DELETE_RESULT_OK = 1;
    private final static int COPY_TO_THIS_FILE = 2;
    private final static int COPY_TO_OTHER_FILE = 3;

    public static GridViewActivity gridViewActivityRefresh = null;  //用于在FileIoUtil类里finish掉当前activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gridview);
        initGridViewAdapter();
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(myAdapter);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        gridView.setLayoutAnimation(layoutAnimationController);
        gridView.startLayoutAnimation();
        gridView.setOnItemClickListener(this);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(this);
        gridViewActivityRefresh = this;
    }

    public void initGridViewAdapter() {
        Intent intent = getIntent();
        firstImagePathString = intent.getStringExtra("firstImagePath");
        firstImagePath = new File(firstImagePathString);
        imageDataUtil = new ImageDataUtil(this);
        folderImages = imageDataUtil.getGridViewFolderData(firstImagePath);
        myAdapter = new GridViewAdapter(this, folderImages, mSelectMap);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AdapterViewFlipperActivity.class);
        intent.putExtra("onClickImagePosition", position);
        intent.putExtra("folderImages", (Serializable) folderImages);
        intent.putExtra("firstImagePath", firstImagePathString);
        Toast.makeText(this, mSelectMap.toString(), Toast.LENGTH_LONG).show();
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//         当每个项状态改变的时候的操作
        mActionText.setText(formatString(gridView.getCheckedItemCount()));
        mSelectMap.put(position, checked);
        mode.invalidate();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//         先清除上次得到的mSelectMap
        mSelectMap.clear();
//         得到布局文件的View
        View menuView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        mActionText = (TextView) menuView.findViewById(R.id.action_text);
        mActionText.setText(formatString(gridView.getCheckedItemCount()));
//         设置动作条的视图
        mode.setCustomView(menuView);
//         得到菜单
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.getItem(0).setEnabled(gridView.getCheckedItemCount() != gridView.getCount());
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//        当点击全选的时候,全选 点击全不选的时候全不选

        switch (item.getItemId()) {

            case R.id.menu_selected:
                for (int i = 0; i < gridView.getCount(); i++) {
                    gridView.setItemChecked(i, true);
                    mSelectMap.put(i, true);
                }
                break;

            case R.id.menu_unselected:
                for (int i = 0; i <= gridView.getCount(); i++) {
                    gridView.setItemChecked(i, false);
                    mSelectMap.remove(i);
                }
                break;
        }
        return true;
    }

    //多选确定后回调此函数
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Collection<Boolean> values = mSelectMap.values();
        if (!values.contains(true)) {
            return;
        }
        getSelectedImagesFileSet();
        if (mSelectMap.containsKey(0)) {
            fileIoUtil = new FileIoUtil(this, selectedImagesFileSet);
        }else{
            fileIoUtil = new FileIoUtil(this, selectedImagesFileSet);
        }
        chooseOperateFromDialog();
    }

    public void chooseOperateFromDialog() {

        String[] operateSets = new String[]{"重命名", "复制到当前相册", "复制到其它相册", "删除"};
        Dialog dialog = new AlertDialog.Builder(this)
                .setItems(operateSets, this).create();
        dialog.show();
    }

    private void chooseAlbumFromDialog() {

        ImageDataUtil imageDataUtil = new ImageDataUtil(this);
        ListViewItem[] items = imageDataUtil.getListViewAdapterData();

        String[] operateSets = new String[items.length];
        final String[] operateSetImagePath = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            operateSets[i] = items[i].getBucket_Name() + " (存有" + items[i].getPhoto_Num() + "张照片)";
            operateSetImagePath[i] = items[i].getFirstImagePath();
        }

        Dialog dialog = new AlertDialog.Builder(this)
                .setItems(operateSets, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OtherAlbumPath = operateSetImagePath[which];  //初始化otherAlbum的路径
                        FileOperateInUI(COPY_TO_OTHER_FILE);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String[] operateSets = new String[]{"重命名", "复制到当前相册", "复制到其它相册", "删除"};
        if (operateSets[which].equals("重命名")) {
            fileIoUtil.renameFiles();
            myAdapter.notifyDataSetChanged();
        }
        if (operateSets[which].equals("复制到其它相册")) {
            chooseAlbumFromDialog();
        }
        if (operateSets[which].equals("复制到当前相册")) {
            FileOperateInUI(COPY_TO_THIS_FILE);
        }
        if (operateSets[which].equals("删除")) {
            FileOperateInUI(DELETE_RESULT_OK);
        }

    }

    private void FileOperateInUI(final int flag) {

        final ProgressDialog progressDialog = showProgressDialog();
        progressDialog.show();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DELETE_RESULT_OK) {
                    progressDialog.dismiss();
                    reFleshGridViewActivity();
                } else if (flag == COPY_TO_OTHER_FILE) {
                    progressDialog.dismiss();
                    goToOtherAlbum(OtherAlbumPath);
                } else {
                    if (msg.what == COPY_TO_THIS_FILE) {
                        progressDialog.dismiss();
                        reFleshGridViewActivity();
                    }
                }
            }
        };

        if (flag == COPY_TO_THIS_FILE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (fileIoUtil.copyToThisFiles()) {
                        handler.sendEmptyMessage(COPY_TO_THIS_FILE);
                    }
                }
            }).start();
        } else if (flag == COPY_TO_OTHER_FILE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(OtherAlbumPath);
                    String copyToOtherAlbumPath = file.getParent() + File.separator;
                    if (fileIoUtil.copyToOtherFiles(copyToOtherAlbumPath)) {
                        handler.sendEmptyMessage(COPY_TO_OTHER_FILE);
                    }
                }
            }).start();
        } else {
            if (flag == DELETE_RESULT_OK) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (fileIoUtil.deleteFiles()) {
                            handler.sendEmptyMessage(DELETE_RESULT_OK);
                        }
                    }
                }).start();
            }
        }

    }

    private void goToOtherAlbum(String firstImagePath) {
        Intent intent = new Intent(this, GridViewActivity.class);
        intent.putExtra("firstImagePath", firstImagePath);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
        this.finish();
    }

    public ProgressDialog showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("正在执行操作 ");
        return progressDialog;
    }

    public void reFleshGridViewActivity() {
        Intent intent = new Intent(GridViewActivity.this, GridViewActivity.class);
        intent.putExtra("firstImagePath", firstImagePathString);
        startActivity(intent);
        finish();
    }

    public void getSelectedImagesFileSet() {
        selectedImagesFileSet = new ArrayList<>();
        for (int i = 0; i < folderImages.size(); i++) {
            if (mSelectMap.containsKey(i) && mSelectMap.get(i)) {
                selectedImagesFileSet.add(folderImages.get(i));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ListView_MainActivity.class);
        startActivity(intent);
    }

    private String formatString(int count) {
        return String.format("选中%s个", count);
    }

}
