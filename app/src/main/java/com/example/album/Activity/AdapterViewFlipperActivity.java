package com.example.album.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterViewFlipper;
import android.widget.Toast;

import com.example.album.Adapter.AdapterViewFlipperAdapter;
import com.example.album.R;
import com.example.album.Service.MyMusicService;

import java.io.File;
import java.util.List;

public class AdapterViewFlipperActivity extends Activity implements View.OnTouchListener{

    private List<File> folderImages;

    private int onClickImagePosition;
    private int presentImagePosition;

    private AdapterViewFlipperAdapter myAdapter;
    private AdapterViewFlipper adapterViewFlipper;

    private GestureDetector myGestureDetector;

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (adapterViewFlipper.isFlipping()) {
                adapterViewFlipper.stopFlipping();
                stopMusic();
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 50) {
                showNextImage();
            } else if (e2.getX() - e1.getX() > 50) {
                showPreviousImage();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!adapterViewFlipper.isFlipping()) {
                adapterViewFlipper.startFlipping();
                startMusic();
            }
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Toast.makeText(AdapterViewFlipperActivity.this,folderImages.get(presentImagePosition).getName(),Toast.LENGTH_LONG).show();
            super.onLongPress(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imageshow);
        adapterViewFlipper = (AdapterViewFlipper) findViewById(R.id.AdapterViewFlipper);
        initAdapterViewFlipperAdapter();
        adapterViewFlipper.setAdapter(myAdapter);
        adapterViewFlipper.setDisplayedChild(onClickImagePosition);
        adapterViewFlipper.setOnTouchListener(this);
        myGestureDetector = new GestureDetector(this, new MyGestureListener());
    }

    public void initAdapterViewFlipperAdapter() {
        Intent intent = getIntent();
        onClickImagePosition = intent.getIntExtra("onClickImagePosition", 0);
        presentImagePosition = onClickImagePosition;
        folderImages = (List<File>) intent.getSerializableExtra("folderImages");
        myAdapter = new AdapterViewFlipperAdapter(this, folderImages, onClickImagePosition);
    }

    private void showPreviousImage() {
        adapterViewFlipper.showPrevious();
        presentImagePosition --;
        if (presentImagePosition == -1){
            presentImagePosition = folderImages.size() - 1;
        }
    }

    private void showNextImage() {
        adapterViewFlipper.showNext();
        presentImagePosition ++;
        if (presentImagePosition ==  folderImages.size()){
            presentImagePosition = 0;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        myGestureDetector.onTouchEvent(event);
        return true;
    }

    private void startMusic(){
        Intent intent = new Intent();
        intent.setClass(AdapterViewFlipperActivity.this, MyMusicService.class);
        startService(intent);
    }

    private void stopMusic(){
        Intent intent = new Intent();
        intent.setClass(AdapterViewFlipperActivity.this, MyMusicService.class);
        stopService(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopMusic();
    }
}
