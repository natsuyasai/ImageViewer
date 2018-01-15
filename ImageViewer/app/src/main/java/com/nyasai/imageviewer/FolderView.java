package com.nyasai.imageviewer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

/**
 * 1フォルダ単位の画像一覧描画用ビュー
 */
public class FolderView extends AppCompatActivity implements Callback ,ImplicitIntentEventListener {

  // ファイルマネージャ
  private FileManager mfileManager;
  // グリッドビュー操作用
  private GridViewOperation mGVOeration;
  // 現表示中フォルダパス
  private String mFolderPath;
  // イベント通知用
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
    mIntentNotifycate.setImplicitIntentListener(this);


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
   * メニュー設定
   * @param menu
   * @return
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.folder_menu,menu);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * ファイル検索，アダプタセット用
   */
  private Runnable runnable = new Runnable() {
    @Override
    public void run() {
      FolderView.this.setupFile(mFolderPath);
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
  }


  /**
   * Dispatch onResume() to fragments.  Note that for better inter-operation
   * with older versions of the platform, at the point of this call the
   * fragments attached to the activity are <em>not</em> resumed.  This means
   * that in some cases the previous state may still be saved, not allowing
   * fragment transactions that modify the state.  To correctly interact
   * with fragments in their proper state, you should instead override
   * {@link #onResumeFragments()}.
   */
  @Override
  protected void onResume() {
    super.onResume();
    // ファイル一覧再表示
    setupFile(mFolderPath);
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
        case R.id.folder_menu_remove:
          break;
        default:
          break;
      }

      return super.onOptionsItemSelected(item);
    }

    /**
   * グリッドビューにファイル一覧を設定
   * @param folderPath
   */
  private void setupFile(String folderPath)
  {
    // 指定フォルダの画像ファイルパスを取得
    ArrayList<String> filePath =  mfileManager.getAllFile(folderPath);

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
    mGVOeration.setFilePathList(filePath);

    // グリッドビューに設定
    ImageGridViewAdapter adapter = new ImageGridViewAdapter(ContextManager.getContext(),
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
  public void returnIntentEvent(Object object) {
    Intent reslt = new Intent();
    reslt.putExtra("SubSctivity",(String)object);
    setResult(Activity.RESULT_OK,reslt);
    finish();
  }
}
