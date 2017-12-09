package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * 画像一枚単位の描画用アクティビティ
 */
public class OneImageViewActivity extends AppCompatActivity {

  private MyImageView mImageView;
  ArrayList<String> mFilePathList;


  @SuppressLint("ResourceType")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // サブアクティビティからの情報取得
    Intent intent = getIntent();
    String filePath = intent.getStringExtra(Constants.FILE_PATH);
    mFilePathList = intent.getStringArrayListExtra(Constants.FILE_PATH_LIST);

    /*
    // タイトル設定
    String[] splitStr = filePath.split("/",0);
    setTitle(splitStr[splitStr.length - 1]);
    // ナビゲーションバー非表示
    if (Build.VERSION.SDK_INT >= 19) {
      Window window = getWindow();
      View view = window.getDecorView();
      view.setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
              View.SYSTEM_UI_FLAG_FULLSCREEN |
              View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    */

    setContentView(R.layout.activity_one_image_view);

    // ビューに画像描画
    mImageView = new MyImageView(this,filePath);
    ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.one_image_view);
    layout.addView(mImageView);
  }

}
