package com.nyasai.imageviewer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  /***
   * 権限
   */
  private final static int REQUEST_PERMISSION = 1002;

  private FileManager mfileManager;
  private Context mContext;
  private ImageGridViewAdapter mImageGridViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // コンテキスト設定
    ContextManager.onCreateApplication(getApplicationContext());
    mContext = ContextManager.GetContext();
    // android6.0以上の場合は権限許可チェック
    if(Build.VERSION.SDK_INT >= 23)
      CheckPermission();
    else {
      // ファイル一覧取得
      StartMain();
    }
  }

  /**
   * メイン画面動作統括部
   */
  private void StartMain()
  {
    // 初期設定
    mfileManager = new FileManager();
    // 端末内の画像ファイル一覧取得
    mfileManager.GetAllFileList();

    // 画像ファイル設定
    SetupFile();
  }

  /***
   * ファイル検索開始
   */
  private void SetupFile()
  {
    // アダプタに登録
    ImageGridViewAdapter adapter = new ImageGridViewAdapter(this,
            mfileManager.GetFilePathForRepresentative(mfileManager.GetImageFileUri()),
            R.layout.grid_item_image);
    GridView gridView = (GridView)findViewById(R.id.gridView);
    gridView.setAdapter(adapter);
  }


  //region ファイルアクセス権限確認，取得系
  /**
   * ファイルアクセス権限確認
   */
  private void CheckPermission()
  {
    // 許可済
    if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    {
      StartMain();
    }
    else // 拒否時
    {
      RequestLocationPermission();
    }
  }

  /**
   * 許可を求める
   */
  private void RequestLocationPermission()
  {
    if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
    {
      ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION);
    }
    else
    {
      Toast toast = Toast.makeText(this, "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
      toast.show();
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, REQUEST_PERMISSION);
    }
  }


  /**
   * 結果取得(コールバック)
   * @param requestCode
   * @param permissions
   * @param grantResults
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == REQUEST_PERMISSION) {
      // 使用が許可された
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        StartMain();
        return;

      } else {
        // それでも拒否された時の対応
        Toast toast = Toast.makeText(this, "ファイルへのアクセスが許可されていません", Toast.LENGTH_SHORT);
        toast.show();
      }
    }
  }
  //endregion


}
