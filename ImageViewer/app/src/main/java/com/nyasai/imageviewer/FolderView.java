package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * 1フォルダ単位の画像一覧描画用ビュー
 */
public class FolderView extends AppCompatActivity implements Callback {

  // ファイルマネージャ
  private FileManager mfileManager;
  // グリッドビュー操作用
  private GridViewClickListner mGVClickListner;
  // 現表示中フォルダパス
  private String mFolderPath;
  // メニュー
  private Menu mMenu;
  // グリッドビュー
  private GridView mGridView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_folder_view);

    // メインアクティビティからの情報取得
    Intent intent = getIntent();
    mFolderPath = intent.getStringExtra(Constants.FOLDER_PATH);

    // グリッドビュー取得
    mGridView = findViewById(R.id.gridView_sub);

    // グリッドビュークリックスナ登録
    mGVClickListner = new GridViewClickListner(this,(GridView)findViewById(R.id.gridView_sub));
    ((GridView) findViewById(R.id.gridView_sub)).setOnItemClickListener(mGVClickListner);

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
    //
    if(mIsSelect == true)
    {
      doSelectMenu(mMenu);
    }
  }

  //region メニュー関連
  /**
   * メニュー設定
   * @param menu
   * @return
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.folder_menu,menu);
    mMenu = menu;
    return super.onCreateOptionsMenu(menu);
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
      case R.id.folder_menu_select:     // 選択
        mGVClickListner.setCheckable(true); // 選択状態に設定
        break;
      case R.id.folder_menu_move:       // 移動
        mGVClickListner.setCheckable(false);  // 非選択状態に設定
        break;
      case R.id.folder_menu_remove:     // 削除
        // TODO:確認ダイアログ表示
        // 削除確認
        // ファイル削除
        FileManager.deleteFile(getCheckFileList());
        // 画面リフレッシュ
        Common.refreshActivity(this);
        mGVClickListner.setCheckable(false);  // 非選択状態に設定
        break;
      case R.id.folder_select_chancel:  // 選択キャンセル
        mGVClickListner.setCheckable(false);  // 非選択状態に設定
        break;
      default:
        mGVClickListner.setCheckable(false);  // 非選択状態に設定
        break;
    }
    doSelectMenu(mMenu);
    return super.onOptionsItemSelected(item);
  }

  /**
   * 選択メニュー選択時処理
   */
  private boolean mIsSelect = false; // メニュー「選択」項目選択状態
  private void doSelectMenu(Menu menu)
  {
    // 選択メニュー未選択
    if(mIsSelect == false)
    {
      // 選択中メニューに置き換え
      changeMenuVisibles(menu,false);
      mIsSelect = true;
      // チェックボックス表示
      setGridViewChekBox(View.VISIBLE,false);
    }
    else // 選択メニュー選択中
    {
      // 選択中メニューを非表示
      changeMenuVisibles(menu,true);
      mIsSelect = false;
      // チェックボックス非表示
      setGridViewChekBox(View.INVISIBLE,false);
    }
  }

  /**
   * 選択メニュー切替
   * @param menu
   * @param selectVisible
   */
  private void changeMenuVisibles(Menu menu, boolean selectVisible)
  {
    MenuItem menuSelect = menu.findItem(R.id.folder_menu_select);
    MenuItem menuRemove = menu.findItem(R.id.folder_menu_remove);
    MenuItem menuMove = menu.findItem(R.id.folder_menu_move);
    MenuItem menuChancel = menu.findItem(R.id.folder_select_chancel);
    menuSelect.setVisible(selectVisible);
    menuRemove.setVisible(!selectVisible);
    menuMove.setVisible(!selectVisible);
    menuChancel.setVisible(!selectVisible);
  }

  /**
   * チェックボックス表示/非表示切り替え
   * @param visible
   * @param checked
   */
  private void setGridViewChekBox(int visible, boolean checked)
  {
    for(int i=0; i<mGridView.getChildCount(); i++) // グリッドビューの要素数
    {
      ViewGroup gvChild = (ViewGroup)mGridView.getChildAt(i);
      for(int j=0; j<gvChild.getChildCount(); j++) // グリッドビューの1要素内のアイテム探索
      {
        if(gvChild.getChildAt(j) instanceof CheckBox)
        {
          gvChild.getChildAt(j).setVisibility(visible);
          ((CheckBox) gvChild.getChildAt(j)).setChecked(false);
        }
      }
    }
  }

  /**
   * チェックしたファイル一覧を取得
   */
  public ArrayList<String> getCheckFileList()
  {
    ArrayList<Integer> checkOnListIdx = new ArrayList<Integer>();
    // チェックボックスON項目のアイテムインデックス取得
    for(int i=0; i<mGridView.getChildCount(); i++) // グリッドビューの要素数
    {
      ViewGroup gvChild = (ViewGroup)mGridView.getChildAt(i);
      for(int j=0; j<gvChild.getChildCount(); j++) // グリッドビューの1要素内のアイテム探索
      {
        if(gvChild.getChildAt(j) instanceof CheckBox)
        {
          // チェックボックスがONとなっているアイテムのインデックスを保持
          if(((CheckBox) gvChild.getChildAt(j)).isChecked() == true)
          {
            checkOnListIdx.add(i);
          }
        }
      }
    }
    // チェックボックスONとなっているファイル名を取得
    ArrayList<String> deleteFileList = new ArrayList<String>();
    for(Integer idx : checkOnListIdx)
    {
      ViewGroup gvChild = (ViewGroup)mGridView.getChildAt(idx);
      for(int j=0; j<gvChild.getChildCount(); j++) // グリッドビューの1要素内のアイテム探索
      {
        // ファイルパス取得
        if(gvChild.getChildAt(j) instanceof EditText)
        {
          deleteFileList.add(String.valueOf(((EditText) gvChild.getChildAt(j)).getText()));
          Log.d("DeleteFilePath",String.valueOf(((EditText) gvChild.getChildAt(j)).getText()));
        }
      }
    }
    return deleteFileList;
  }
  //endregion

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
    mGVClickListner.setFilePathList(filePath);

    // グリッドビューに設定
    ImageGridViewAdapter adapter = new ImageGridViewAdapter(ContextManager.getContext(),
        gridViewItems,
        R.layout.grid_item_image);
    mGridView.setAdapter(adapter);
  }

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
    // チェック状態
    private boolean mIsCheckable;

    /**
     * コンストラクタ
     */
    public GridViewClickListner(Activity activity , GridView gridView)
    {
      mActivity = activity;
      mGridView = gridView;
      mIsCheckable = false;
    }

    /**
     * グリッドビューのファイルパスリスト設定
     */
    public void setFilePathList(ArrayList<String> list)
    {
      mFilePathList = list;
    }

    /**
     * 選択状態の変更
     * @param checkable
     */
    public void setCheckable(boolean checkable)
    {
      mIsCheckable = checkable;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      if(view != null)
      {
        // 非チェック状態
        if(mIsCheckable == false)
        {
          // 指定座標のアイテム取得
          ImageGridViewAdapter.GridViewItem item = (ImageGridViewAdapter.GridViewItem) parent.getItemAtPosition(position);
          createOneImageView(position, item.imagePath);
        }
        else // 選択状態ならば，チェックボックスの状態を変更する
        {
            // グリッドビューの子要素取得
            // TODO:落ちる
/*            ViewGroup gvChild = (ViewGroup) parent.getChildAt(position);
            if(gvChild != null)
            {
              for (int j = 0; j < gvChild.getChildCount(); j++) // グリッドビューの1要素内のアイテム探索
              {
                if (gvChild.getChildAt(j) instanceof CheckBox) {
                  if (((CheckBox) gvChild.getChildAt(j)).isChecked() == true)
                    ((CheckBox) gvChild.getChildAt(j)).setChecked(false);
                  else
                    ((CheckBox) gvChild.getChildAt(j)).setChecked(true);
                }
              }
            }*/
        }
      }
    }

    /**
     * 画像描画用ビュー新規作成(サブアクティビティ生成)
     */
    private void createOneImageView(int position, String filePath)
    {
      // 1ファイルビュー遷移
      if(mFilePathList != null) {
        Intent intent = new Intent(ContextManager.getContext(), OneImageViewActivity.class);
        intent.putExtra(Constants.FILE_PATH, filePath);
        intent.putExtra(Constants.FILE_POSITION,position);
        intent.putStringArrayListExtra(Constants.FILE_PATH_LIST, mFilePathList);
        ContextManager.getContext().startActivity(intent);
      }
    }
  }


}
