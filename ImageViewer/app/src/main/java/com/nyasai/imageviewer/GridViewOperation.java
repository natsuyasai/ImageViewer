package com.nyasai.imageviewer;

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
    ImageGridViewAdapter.GridViewItem item = (ImageGridViewAdapter.GridViewItem) parent.getItemAtPosition(position);
    Log.d("GridViewClick",item.folderPath);

  }
}
