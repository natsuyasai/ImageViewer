package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.*;
import android.view.WindowManager;

import static com.nyasai.imageviewer.Common.GetBitMapOption;

/**
 * 画像一枚単位の描画用アクティビティ
 */
public class OneImageViewActivity extends AppCompatActivity {

  private ImageView mImageView;
  @SuppressLint("ResourceType")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // サブアクティビティからの情報取得
    Intent intent = getIntent();
    String filePath = intent.getStringExtra(Constants.FILE_PATH);
    // ビューに画像描画
    mImageView = new ImageView(this,filePath);
    setContentView(mImageView);
  }

}
