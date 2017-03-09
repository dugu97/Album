package com.example.album.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterViewFlipper;

import com.example.album.Adapter.AdapterViewFlipperAdapter;
import com.example.album.R;

import java.io.File;
import java.util.List;

public class AdapterViewFlipperActivity extends Activity implements View.OnTouchListener {

    private List<File> folderImages;
    private int onClickImagePosition;
    private AdapterViewFlipperAdapter myAdapter;
    AdapterViewFlipper adapterViewFlipper;
    GestureDetector myGestureDetector;

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (adapterViewFlipper.isFlipping()) {
                adapterViewFlipper.stopFlipping();
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
            }
            return super.onDoubleTap(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        folderImages = (List<File>) intent.getSerializableExtra("folderImages");
        myAdapter = new AdapterViewFlipperAdapter(this, folderImages, onClickImagePosition);
    }

    private void showPreviousImage() {
        adapterViewFlipper.showPrevious();
    }

    private void showNextImage() {
        adapterViewFlipper.showNext();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        myGestureDetector.onTouchEvent(event);
        return true;
    }
}
