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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.nyasai.imageviewer.Constants.MODE_DEF;
import static com.nyasai.imageviewer.Constants.MODE_IMPLICIT_INTENT;
import static com.nyasai.imageviewer.Constants.REQ_CODE_SUB_ACT;

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
  private GridViewClickListner mGVClickListner;
  // モード
  private int mNowMode;
  // 初回起動フラグ
  // note.権限取得待ちのときにonResumeが呼び出される為，権限取得時はonResumeの際になにも実行しないようにフラグで防ぐ
  private boolean mIsFirstExec = true;

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
      mGVClickListner = new GridViewClickListner(this, (GridView) findViewById(R.id.gridViewHome), MODE_DEF);
      mNowMode = MODE_DEF;
    }
    else {
      mGVClickListner = new GridViewClickListner(this,(GridView) findViewById(R.id.gridViewHome), MODE_IMPLICIT_INTENT);
      mNowMode = MODE_IMPLICIT_INTENT;
    }
    ((GridView) findViewById(R.id.gridViewHome)).setOnItemClickListener(mGVClickListner);

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
    if (mIsFirstExec == false) {
      // フォルダ一覧再表示
      setupFile();
    }
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
    mIsFirstExec = false;
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
    // 読み込み許可
    if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    {
      // 書き込み許可
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
    if((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE) == false) ||
        (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == false))
    {
      ActivityCompat.requestPermissions(MainActivity.this,
          new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
    }
    else
    {
      Toast toast = Toast.makeText(this, "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
      toast.show();
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
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
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
      {
        startMain();
        return;

      }
      else
      {
        // それでも拒否された時の対応
        Toast toast = Toast.makeText(this, "ファイルへのアクセスが許可されていません", Toast.LENGTH_SHORT);
        toast.show();
      }
    }
  }
  //endregion

  /**
   * GridViewタップイベント定義用クラス
   */
  private class GridViewClickListner implements AdapterView.OnItemClickListener, View.OnTouchListener
  {
    // 対象グリッドビュー
    private GridView mGridView;
    // 実行中アクティビティ
    private Activity mActivity;
    // ファイルパスリスト
    private ArrayList<String> mFilePathList;
    // 動作モード(0:通常起動,1:暗黙インテントによる起動)
    private int mMode;


    /**
     * コンストラクタ
     */
    public GridViewClickListner(Activity activity, GridView gridView, int mode)
    {
      mActivity = activity;
      mGridView = gridView;
      mMode = mode;
    }

    /**
     * アイテムタップ時
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      // タップイベント判別
      switch (event.getAction())
      {
        case MotionEvent.ACTION_DOWN:
          break;
        case MotionEvent.ACTION_UP:
          break;
        case MotionEvent.ACTION_CANCEL:
          break;
        default:
          break;
      }
      return false;
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      if(view != null) {
        // 指定座標のアイテム取得
        ImageGridViewAdapter.GridViewItem item = (ImageGridViewAdapter.GridViewItem) parent.getItemAtPosition(position);
        createSubFolderView(item.folderPath);
      }
    }

    /**
     * 画像描画用ビュー新規作成(サブアクティビティ生成)
     * @param subFordlerPath
     */
    private void createSubFolderView(String subFordlerPath)
    {
      // フォルダ単位ビュー遷移
      Intent intent = new Intent(ContextManager.getContext(),FolderView.class);
      intent.putExtra(Constants.FOLDER_PATH, subFordlerPath);
      intent.putExtra(Constants.MODE, mMode);
      //ContextManager.getContext().startActivity(intent);
      mActivity.startActivityForResult(intent,REQ_CODE_SUB_ACT);
    }
  }

}
