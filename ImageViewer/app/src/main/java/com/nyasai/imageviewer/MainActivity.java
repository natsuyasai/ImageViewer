package com.nyasai.imageviewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  /***
   *
   */
  private final static int REQUEST_PERMISSION = 1002;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // android6.0以上の場合は権限許可チェック
    if(Build.VERSION.SDK_INT >= 23)
      CheckPermission();
    else
      SetupSearch();
  }

  /***
   * ファイル検索開始
   */
  private void SetupSearch()
  {

  }

  /**
   * ファイルアクセス権限確認
   */
  private void CheckPermission()
  {
    // 許可済
    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    {
      SetupSearch();
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
        SetupSearch();
        return;

      } else {
        // それでも拒否された時の対応
        Toast toast = Toast.makeText(this, "ファイルへのアクセスが許可されていません", Toast.LENGTH_SHORT);
        toast.show();
      }
    }
  }
}
