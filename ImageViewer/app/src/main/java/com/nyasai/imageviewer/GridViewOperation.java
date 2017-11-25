package com.nyasai.imageviewer;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * グリッドビュー操作クラス
 *
 * note.
 * クリックイベント操作用にOnItemClickListenerインタフェースを実装
 */
public class GridViewOperation implements AdapterView.OnItemClickListener{

  private GridView mGridView;

  /**
   * コンストラクタ
   */
  public GridViewOperation(GridView gridView)
  {
    mGridView = gridView;
  }


  /**
   * グリッドビュークリックリスナー
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

    if(view != null)
    {
      // 指定座標のアイテム取得
      ImageGridViewAdapter.GridViewItem item = (ImageGridViewAdapter.GridViewItem) parent.getItemAtPosition(position);
      switch (parent.getId())
      {
        case R.id.gridView: // メインアクティビティからの呼び出し
          CreateSubFolderView(item.folderPath);
          break;
        case R.id.gridView_sub: //フォルダ単位のサブアクティビティからの呼び出し
          CreateOneImageView(item.imagePath);
          break;
        default:
          break;
      }
    }

  }

  /**
   * 画像描画用ビュー新規作成(サブアクティビティ生成)
   * @param subFordlerPath
   */
  private void CreateSubFolderView(String subFordlerPath)
  {
    // フォルダ単位ビュー遷移
    Intent intent = new Intent(ContextManager.GetContext(),FolderView.class);
    intent.putExtra(Constants.FOLDER_PATH,subFordlerPath);
    ContextManager.GetContext().startActivity(intent);
  }

  private void CreateOneImageView(String filePath)
  {
    // 1ファイルビュー遷移
    Intent intent = new Intent(ContextManager.GetContext(),OneImageViewActivity.class);
    intent.putExtra(Constants.FILE_PATH,filePath);
    ContextManager.GetContext().startActivity(intent);
  }
}
