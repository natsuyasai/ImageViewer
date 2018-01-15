package com.nyasai.imageviewer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.nyasai.imageviewer.Constants.MODE_DEF;
import static com.nyasai.imageviewer.Constants.MODE_IMPLICIT_INTENT;

public class MainActivity extends AppCompatActivity {

  /***
   * 権限
   */
  private final static int REQUEST_PERMISSION = 1002;

  // ファイルマネージャ
  private FileManager mfileManager;
  // コンテキスト
  private Context mContext;
  // グリッドビュー操作
  private GridViewOperation mGVOeration;
  // モード
  private int mNowMode;

  @Override
  /**
   * 画面起動時
   */
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // タイトルバー変更
    setTitle(R.string.app_home);
    // コンテキスト設定
    ContextManager.onCreateApplication(getApplicationContext());
    mContext = ContextManager.getContext();

    // 画面サイズ保持
    WindowSizeManager.onCreateApplication(mContext);
    WindowSizeManager.setViewAllWindowsSize(getWindowManager().getDefaultDisplay());

    // インテント受取
    Intent intent = getIntent();
    // グリッドビュークリックスナ登録
    if(intent.getAction().equals("android.intent.action.MAIN")) {
      mGVOeration = new GridViewOperation(this, null, (GridView) findViewById(R.id.gridViewHome), MODE_DEF);
      mNowMode = MODE_DEF;
    }
    else {
      mGVOeration = new GridViewOperation(this,null,(GridView) findViewById(R.id.gridViewHome), MODE_IMPLICIT_INTENT);
      mNowMode = MODE_IMPLICIT_INTENT;
    }
    ((GridView) findViewById(R.id.gridViewHome)).setOnItemClickListener(mGVOeration);

    // android6.0以上の場合は権限許可チェック
    if(Build.VERSION.SDK_INT >= 23)
      checkPermission();
    else {
      // ファイル一覧取得
      startMain();
    }
  }

  /**
   * ビュー生成後
   * @param hasFocus
   */
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    // ウィンドウサイズを保持
    WindowSizeManager.setViewWindowsSize(findViewById(R.id.main_activity));
  }

  /**
   * メニュー設定
   * @param menu
   * @return
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.home_menu,menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * サブアクティビティからの戻り時
   *
   * @param requestCode
   * @param resultCode
   * @param data
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data == null) {
      // フォルダ一覧表示
      setupFile();
    }
    else
    {
      String path = data.getStringExtra("SubSctivity");
      Uri uri = Uri.fromFile(new File(path));
      //Intent reslt = new Intent(Intent.ACTION_PICK_ACTIVITY, Uri.parse(uri.toString()));
      Intent reslt = new Intent();
      //reslt.setType("image/*");
      reslt.putExtra(MediaStore.EXTRA_OUTPUT,uri);
      setResult(Activity.RESULT_OK,reslt);
      finish();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    // フォルダ一覧再表示
    setupFile();
  }

  /**
   * メイン画面動作統括部
   */
  private void startMain()
  {
    // 初期設定
    mfileManager = new FileManager();

    // 端末内の画像ファイル一覧を取得し，グリッドビューに設定
    // 取得
    MainActivity.this.mfileManager.getAllFileList();
    // 設定
    MainActivity.this.setupFile();
  }

  /***
   * ファイル検索開始
   */
  private ArrayList<ImageGridViewAdapter.GridViewItem> mGridViewData;
  private void setupFile()
  {
    // フォルダごとに一番はじめに見つかったファイルパスを取得
    if(mGridViewData == null)
      mGridViewData = mfileManager.getFilePathForRepresentative(mfileManager.getImageFileUri());
    // アダプタに登録
    ImageGridViewAdapter adapter = new ImageGridViewAdapter(this,
            mGridViewData,
            R.layout.grid_item_image);
    GridView gridView = (GridView)findViewById(R.id.gridViewHome);
    gridView.setAdapter(adapter);
  }


  //region ファイルアクセス権限確認，取得系
  /**
   * ファイルアクセス権限確認
   */
  private void checkPermission()
  {
    // 許可済
    if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    {
      if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        startMain();
      }
    }
    else // 拒否時
    {
      requestLocationPermission();
    }
  }

  /**
   * 許可を求める
   */
  private void requestLocationPermission()
  {
    if((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))&&
        (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)))
    {
      ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
    }
    else
    {
      Toast toast = Toast.makeText(this, "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
      toast.show();
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
      if((ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
          ||(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
      {
        finish();
      }
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
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        startMain();
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
