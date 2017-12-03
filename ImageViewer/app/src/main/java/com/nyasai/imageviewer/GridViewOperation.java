package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import static com.nyasai.imageviewer.Constants.REQ_CODE_SUB_ACT;

/**
 * グリッドビュー操作クラス
 *
 * note.
 * クリックイベント操作用にOnItemClickListenerインタフェースを実装
 */
public class GridViewOperation implements AdapterView.OnItemClickListener, View.OnTouchListener{

  private GridView mGridView;
  private int mMode; // 動作モード(0:通常起動,1:暗黙インテントによる起動)
  private ImplicitEventNotifycate mNotifycate;
  private Activity mActivity;

  /**
   * コンストラクタ
   */
  public GridViewOperation(Activity activity, ImplicitEventNotifycate notifycate , GridView gridView, int mode)
  {
    mActivity = activity;
    mNotifycate = notifycate;
    mGridView = gridView;
    mMode = mode;
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
        case R.id.gridViewHome: // メインアクティビティからの呼び出し
          CreateSubFolderView(item.folderPath);
          break;
        case R.id.gridView_sub: //フォルダ単位のサブアクティビティからの呼び出し
          if(mMode == Constants.MODE_DEF)
            CreateOneImageView(item.imagePath);
          else
          {
            // メインアクティビティにメッセージ送信
            if(mNotifycate != null)
              mNotifycate.SendImplicitIntentEvent(item.imagePath);
          }
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
    intent.putExtra(Constants.FOLDER_PATH, subFordlerPath);
    intent.putExtra(Constants.MODE, mMode);
    //ContextManager.GetContext().startActivity(intent);
    mActivity.startActivityForResult(intent,REQ_CODE_SUB_ACT);
  }

  private void CreateOneImageView(String filePath)
  {
    // 1ファイルビュー遷移
    Intent intent = new Intent(ContextManager.GetContext(),OneImageViewActivity.class);
    intent.putExtra(Constants.FILE_PATH,filePath);
    ContextManager.GetContext().startActivity(intent);
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
  @SuppressLint("ResourceAsColor")
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
}
