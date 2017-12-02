package com.nyasai.imageviewer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.File;
import java.lang.reflect.Parameter;
import java.security.Policy;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import static com.nyasai.imageviewer.Constants.MODE_DEF;

/**
 * 1フォルダ単位の画像一覧描画用ビュー
 */
public class FolderView extends AppCompatActivity implements Callback{

  // タスク用パラメータ
  public class FolderViewAsyncParams{
    public String path;  // フォルダパス
    public Object obj; // クラスオブジェクト
  }

  private FileManager mfileManager;
  private GridViewOperation mGVOeration;
  private String mFolderPath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_folder_view);

    // メインアクティビティからの情報取得
    Intent intent = getIntent();
    mFolderPath = intent.getStringExtra(Constants.FOLDER_PATH);

    // グリッドビュークリックスナ登録
    mGVOeration = new GridViewOperation((GridView)findViewById(R.id.gridView_sub),intent.getIntExtra(Constants.MODE,0));
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

    // グリッドビューに設定
    ImageGridViewAdapter adapter = new ImageGridViewAdapter(ContextManager.GetContext(),
        gridViewItems,
        R.layout.grid_item_image);
    GridView gridView = (GridView)findViewById(R.id.gridView_sub);
    gridView.setAdapter(adapter);
  }
}
