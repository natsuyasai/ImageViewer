package com.nyasai.imageviewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

/**
 * 1フォルダ単位の画像一覧描画用ビュー
 */
public class FolderView extends AppCompatActivity implements Callback ,ImplicitIntentEventListener {

  private FileManager mfileManager;
  private GridViewOperation mGVOeration;
  private String mFolderPath;
  private ImplicitEventNotifycate mIntentNotifycate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_folder_view);

    // メインアクティビティからの情報取得
    Intent intent = getIntent();
    mFolderPath = intent.getStringExtra(Constants.FOLDER_PATH);

    // イベント通知取得設定
    mIntentNotifycate = new ImplicitEventNotifycate();
    mIntentNotifycate.SetImplicitIntentListener(this);


    // グリッドビュークリックスナ登録
    mGVOeration = new GridViewOperation(this,mIntentNotifycate,(GridView)findViewById(R.id.gridView_sub),intent.getIntExtra(Constants.MODE,0));
    ((GridView) findViewById(R.id.gridView_sub)).setOnItemClickListener(mGVOeration);

    // タイトルバー変更
    String[] splitStr = mFolderPath.split("/",0);
    setTitle(splitStr[splitStr.length - 1]);

    mfileManager = new FileManager();

    // 画像描画
    (new Thread(runnable)).start();
  }

  /**
   * ファイル検索，アダプタセット用
   */
  private Runnable runnable = new Runnable() {
    @Override
    public void run() {
      FolderView.this.SetupFile(mFolderPath);
    }
  };

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
    // ファイル一覧表示
    SetupFile(mFolderPath);
  }

  /**
   * グリッドビューにファイル一覧を設定
   * @param folderPath
   */
  private void SetupFile(String folderPath)
  {
    // 指定フォルダの画像ファイルパスを取得
    ArrayList<String> filePath =  mfileManager.GetAllFile(folderPath);

    // グリッドビュー表示用アイテムリスト生成
    ArrayList<ImageGridViewAdapter.GridViewItem> gridViewItems = new ArrayList<ImageGridViewAdapter.GridViewItem>();
    for(int item = 0; item<filePath.size(); item++)
    {
      ImageGridViewAdapter.GridViewItem gridViewItem = new ImageGridViewAdapter.GridViewItem();
      gridViewItem.imagePath = filePath.get(item);
      gridViewItem.folderPath = gridViewItem.imagePath;
      gridViewItems.add(gridViewItem);
    }

    // ファイルパスリストを設定
    mGVOeration.SetFilePathList(filePath);

    // グリッドビューに設定
    ImageGridViewAdapter adapter = new ImageGridViewAdapter(ContextManager.GetContext(),
        gridViewItems,
        R.layout.grid_item_image);
    GridView gridView = (GridView)findViewById(R.id.gridView_sub);
    gridView.setAdapter(adapter);
  }

  /**
   * インテントリターン設定
   *
   * @param object
   */
  @Override
  public void ReturnIntentEvent(Object object) {
    Intent reslt = new Intent();
    reslt.putExtra("SubSctivity",(String)object);
    setResult(Activity.RESULT_OK,reslt);
    finish();
  }
}
