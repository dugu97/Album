package com.example.album.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.album.Adapter.GridViewAdapter;
import com.example.album.R;
import com.example.album.Util.ImageDataUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewActivity extends Activity implements AdapterView.OnItemClickListener,AbsListView.MultiChoiceModeListener{

    private GridViewAdapter myAdapter;
    private List<File> folderImages;
    private GridView gridView;
    private TextView mActionText;
    private Map<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    public void initGridViewAdapter() {
        Intent intent = getIntent();
        String firstImagePath = intent.getStringExtra("firstImagePath");
        File file = new File(firstImagePath);
        ImageDataUtil imageDataUtil = new ImageDataUtil(this);
        folderImages = imageDataUtil.getGridViewFolderData(file);
        myAdapter = new GridViewAdapter(this,folderImages,mSelectMap);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AdapterViewFlipperActivity.class);
        intent.putExtra("onClickImagePosition", position);
        intent.putExtra("folderImages", (Serializable) folderImages);
        Toast.makeText(this,mSelectMap.toString(),Toast.LENGTH_LONG).show();
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_out, R.anim.showimage_zoom_in);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//         当每个项状态改变的时候的操作
        mActionText.setText(formatString(gridView.getCheckedItemCount()));
        mSelectMap.put(position,checked);
        mode.invalidate();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//         先清除上次得到的mSelectMap
        mSelectMap.clear();
//         得到布局文件的View
        View menuView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout,null);
        mActionText = (TextView) menuView.findViewById(R.id.action_text);
        mActionText.setText(formatString(gridView.getCheckedItemCount()));
//         设置动作条的视图
        mode.setCustomView(menuView);
//         得到菜单
        getMenuInflater().inflate(R.menu.action_menu,menu);
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

        switch (item.getItemId()){

            case R.id.menu_selected :
                for (int i = 0; i < gridView.getCount(); i++){
                    gridView.setItemChecked(i,true);
                    mSelectMap.put(i,true);
                }
                break;

            case R.id.menu_unselected :
                for (int i = 0; i < gridView.getCount(); i++){
                    gridView.setItemChecked(i,false);
                }
                mSelectMap.clear();
                break;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
            myAdapter.notifyDataSetChanged();
    }

    private String formatString(int count) {
        return String.format("选中%s个", count);
    }
}
