package com.nyasai.imageviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 画像一枚単位の描画用アクティビティ
 */
public class OneImageViewActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_one_image_view);
  }
}
