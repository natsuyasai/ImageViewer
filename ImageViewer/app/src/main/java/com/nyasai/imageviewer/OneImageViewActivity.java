package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

/**
 * 画像一枚単位の描画用アクティビティ
 */
public class OneImageViewActivity extends AppCompatActivity {

  // イメージビュー
  private MyImageView mImageView;
  // 現表示ファイルと同一フォルダのファイルパス一覧
  private ArrayList<String> mFilePathList;
  // 現表示ファイル
  private int mNowPosition;
  // 画面タップイベント
  private GestureDetector mGestureDetector;
  // レイアウト
  private ConstraintLayout mLayout;
  // コンテキスト
  private Context mContext;
  // スワイプイベント閾値（速度）
  private final int SWAIP_X_SPEED_ABS_TH = 2500;
  // スワイプイベント閾値（移動量）
  private final int SWAIP_X_MOVE_TH = 100;


  @SuppressLint("ResourceType")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // サブアクティビティからの情報取得
    Intent intent = getIntent();
    String filePath = intent.getStringExtra(Constants.FILE_PATH);
    mNowPosition = intent.getIntExtra(Constants.FILE_POSITION,0);
    mFilePathList = intent.getStringArrayListExtra(Constants.FILE_PATH_LIST);

    // コンテキスト設定
    mContext = getApplicationContext();

//    // タイトル設定
//    String[] splitStr = filePath.split("/",0);
//    setTitle(splitStr[splitStr.length - 1]);
//    // ナビゲーションバー非表示
//    if (Build.VERSION.SDK_INT >= 19) {
//      Window window = getWindow();
//      View view = window.getDecorView();
//      view.setSystemUiVisibility(
//          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
//              View.SYSTEM_UI_FLAG_FULLSCREEN |
//              View.SYSTEM_UI_FLAG_FULLSCREEN);
//    }

    // レイアウト設定
    setContentView(R.layout.activity_one_image_view);
    // レイアウト取得
    mLayout = (ConstraintLayout)findViewById(R.id.one_image_view);
    mImageView = (MyImageView)findViewById(R.id.one_image_view_item);

    // ビューに画像描画
    SetImage(filePath);

    // タップイベント動作セットアップ
    SetupGestureDetector();
  }

  private void SetImage(String filePath)
  {
    // 画像を設定しビューに追加
    mImageView.SetImage(filePath);
    mLayout.invalidate();
    mImageView.invalidate();
  }

  /**
   * タッチイベント
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    mGestureDetector.onTouchEvent(event);
    return true;
  }

  /**
   * タップイベント関連設定
   */
  private void SetupGestureDetector()
  {
    mGestureDetector = new GestureDetector(this,
        new GestureDetector.SimpleOnGestureListener() {
          /**
           * 画面に指をおろした時
           * @param e
           * @return
           */
          @Override
          public boolean onDown(MotionEvent e) {
            return false;
          }

          /**
           * ダブルタップ時
           * @param e
           * @return
           */
          @Override
          public boolean onDoubleTap(MotionEvent e) {
            // 画像を画面まで拡大
            Log.d("onDoubleTap","ダブルタップ");
            Picasso.with(getApplicationContext())
                .load(new File(mFilePathList.get(mNowPosition)))
                .fit()
                .into(mImageView);
            return super.onDoubleTap(e);
          }

          /**
           * ダブルタップ（押す，移動，離す）
           * @param e
           * @return
           */
          @Override
          public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
          }

          /**
           * シングルタップ時
           * @param e
           * @return
           */
          @Override
          public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
          }

          /**
           * 画面をおした時
           * @param e
           */
          @Override
          public void onShowPress(MotionEvent e) {

          }

          /**
           * シングル・ダブルタップ時
           * @param e
           * @return
           */
          @Override
          public boolean onSingleTapUp(MotionEvent e) {
            return false;
          }

          /**
           * スクロール操作時
           * @param e1
           * @param e2
           * @param distanceX
           * @param distanceY
           * @return
           */
          @Override
          public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
          }

          /**
           * 長押し時
           * @param e
           */
          @Override
          public void onLongPress(MotionEvent e) {

          }

          /**
           * 画面を指で弾いた時
           * @param befor
           * @param after
           * @param velocityX
           * @param velocityY
           * @return
           */
          @Override
          public boolean onFling(MotionEvent befor, MotionEvent after, float velocityX, float velocityY) {
//            String dbgStr = String.valueOf(befor.getX()) +", " + String.valueOf(befor.getY());
//            String dbgStr2 = String.valueOf(after.getX())  +", " +  String.valueOf(after.getY());
//            String dbgStr3 = String.valueOf(velocityX);
//            String dbgStr4 = String.valueOf(velocityY);
//            Log.d("onFling", dbgStr);
//            Log.d("onFling", dbgStr2);
//            Log.d("onFling", dbgStr3);
//            Log.d("onFling", dbgStr4);

            // スワイプ判定
            if(befor.getX() > after.getX() && (befor.getX()-after.getX()) > SWAIP_X_MOVE_TH)
            {
              if(Math.abs(velocityX) > SWAIP_X_SPEED_ABS_TH)
              {
                // 右→左へのスワイプ
                Log.d("SWAIP","左へスワイプ");
                // 次の画像へ
                if(mNowPosition < (mFilePathList.size()-1))
                  SetImage(mFilePathList.get(++mNowPosition));
              }
            }
            else if(befor.getX() < after.getX() && (after.getX()-befor.getX()) > SWAIP_X_MOVE_TH)
            {
              if(Math.abs(velocityX) > SWAIP_X_SPEED_ABS_TH)
              {
                // 左→右へのスワイプ
                Log.d("SWAIP","右へスワイプ");
                // 前の画像へ
                if(mNowPosition > 0)
                  SetImage(mFilePathList.get(--mNowPosition));
              }
            }
            else
            {
              // 何もしない
            }
            return false;
          }
        });
  }

}
