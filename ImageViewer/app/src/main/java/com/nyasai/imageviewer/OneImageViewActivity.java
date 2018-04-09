package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;

/**
 * 画像一枚単位の描画用アクティビティ
 */
public class OneImageViewActivity extends AppCompatActivity {

  // イメージビュー
  private PhotoView mImageView;
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
  private final int SWAIP_X_SPEED_ABS_TH = 1000;
  // スワイプイベント閾値（移動量）
  private final int SWAIP_X_MOVE_TH = 300;
  // タイトルバー状態
  private boolean mIsTitleBar = true;


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

    // タイトル設定
    String[] splitStr = filePath.split("/",0);
    setTitle(splitStr[splitStr.length - 1]);
    // タイトルバー非表示
    changeTitleBarState(false);

    // レイアウト設定
    setContentView(R.layout.activity_one_image_view);
    // レイアウト取得
    mLayout = (ConstraintLayout)findViewById(R.id.one_image_view);
    mImageView = (PhotoView) findViewById(R.id.one_image_view_item);

    // ビューに画像描画
    setImage(filePath);

    // タップイベント動作セットアップ
    setupGestureDetector();
  }
  
  /**
   * メニュー設定
   * @param menu
   * @return
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.item_menu,menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  /**
   * メニューアイテム選択処理
   * @param item
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId())
    {
      case R.id.item_menu_remove: // ファイル削除
        this.deleteFile();
        break;
      default:
        break;
    }
    
    return super.onOptionsItemSelected(item);
  }

  // ファイル削除
  private void deleteFile()
  {
    // 削除確認
    new AlertDialog.Builder(this)
        .setMessage(R.string.file_remove_check)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            String deleteFilePath =mFilePathList.get(mNowPosition);
            // ファイルパスリストから削除するファイルを除き，画像を前のファイルに更新
            mFilePathList.remove(mNowPosition);
            // ファイル削除
            FileManager.deleteFile(deleteFilePath);
            // ポジション修正
            if(mFilePathList.size() > 0) {
              // 複数ファイルが存在する時に先頭画像を削除する場合は，次に先頭になるファイルを選択
              if(mNowPosition !=0)
                mNowPosition--;
              // 削除ファイルの前又は後ろのファイルを表示
              setImage(mFilePathList.get(mNowPosition));
              setTitleName(mFilePathList.get(mNowPosition));
            }
            else
            {
              // フォルダ内の画像が0になればフォルダビューに戻る
              finish();
            }
          }
        })
        .setNegativeButton("Cancel",null)
        .show();
  }
  

  /**
   * 表示用画像設定
   * @param filePath
   */
  private void setImage(String filePath)
  {
    BitmapFactory.Options preOptions = Common.getBitMapSize(filePath);
    Bitmap bitmap;
    if((preOptions.outWidth*preOptions.outHeight) > (1920*1080)) {
      bitmap = Common.getBitmap(filePath,mContext);
    }
    else
    {
      bitmap = BitmapFactory.decodeFile(filePath);
    }
    // 画像を設定しビューに追加
    mImageView.setImageBitmap(bitmap);
    mLayout.invalidate();
    mImageView.invalidate();
  }

  /**
   * 画面タッチイベント発生
   */
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    // 先にスワイプ判定を行い，次のビューに渡す
    mGestureDetector.onTouchEvent(ev);
    return super.dispatchTouchEvent(ev);
  }

  /**
   * タイトルバー表示/非表示切り替え
   */
  private void changeTitleBarState(){
    mIsTitleBar = (mIsTitleBar == false) ? true : false;
    if(mIsTitleBar == true)
    {
      getSupportActionBar().show();
    }
    else
    {
      getSupportActionBar().hide();
    }
  }
  private void changeTitleBarState(boolean isShow){
    mIsTitleBar = isShow;
    if(mIsTitleBar == true)
    {
      getSupportActionBar().show();
    }
    else
    {
      getSupportActionBar().hide();
    }
  }


  /**
   * タップイベント関連設定
   */
  private void setupGestureDetector()
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
            return false;
          }

          /**
           * ダブルタップ（押す，移動，離す）
           * @param e
           * @return
           */
          @Override
          public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
          }

          /**
           * シングルタップ時
           * @param e
           * @return
           */
          @Override
          public boolean onSingleTapConfirmed(MotionEvent e) {
            changeTitleBarState();
            return false;
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
                if(mNowPosition < (mFilePathList.size()-1)) {
                  setImage(mFilePathList.get(++mNowPosition));
                  setTitleName(mFilePathList.get(mNowPosition));
                }
              }
            }
            else if(befor.getX() < after.getX() && (after.getX()-befor.getX()) > SWAIP_X_MOVE_TH)
            {
              if(Math.abs(velocityX) > SWAIP_X_SPEED_ABS_TH)
              {
                // 左→右へのスワイプ
                Log.d("SWAIP","右へスワイプ");
                // 前の画像へ
                if(mNowPosition > 0) {
                  setImage(mFilePathList.get(--mNowPosition));
                  setTitleName(mFilePathList.get(mNowPosition));
                }
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
  
  /**
   * タイトル設定
   * @param filePath
   */
  private void setTitleName(String filePath)
  {
    // タイトル設定
    String[] splitStr = filePath.split("/",0);
    setTitle(splitStr[splitStr.length - 1]);
  }

}
