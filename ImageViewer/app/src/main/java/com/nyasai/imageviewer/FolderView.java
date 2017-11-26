package com.nyasai.imageviewer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

/**
 * 1フォルダ単位の画像一覧描画用ビュー
 */
public class FolderView extends AppCompatActivity {

  private FileManager mfileManager;
  private GridViewOperation mGVOeration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_folder_view);

    // グリッドビュークリックスナ登録
    mGVOeration = new GridViewOperation((GridView)findViewById(R.id.gridView_sub));
    ((GridView) findViewById(R.id.gridView_sub)).setOnItemClickListener(new GridViewOperation((GridView)findViewById(R.id.gridView_sub)));

    // メインアクティビティからの情報取得
    Intent intent = getIntent();
    String folderPath = intent.getStringExtra(Constants.FOLDER_PATH);

    // タイトルバー変更
    String[] splitStr = folderPath.split("/",0);
    setTitle(splitStr[splitStr.length - 1]);

    // ファイルリスト表示
    SetupFile(folderPath);
  }

  /**
   * グリッドビューにファイル一覧を設定
   * @param folderPath
   */
  private void SetupFile(String folderPath)
  {
    mfileManager = new FileManager();
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
