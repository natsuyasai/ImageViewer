package com.nyasai.imageviewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.nyasai.imageviewer.Common.GetBitMapOption;

/**
 * 画像表示グリッドビュー用アダプタ
 */
public class ImageGridViewAdapter extends BaseAdapter {

  // コンテキスト
  private Context mContext;
  // ビュー登録
  private LayoutInflater mLayoutInflater;
  // 表示データ
  private ArrayList<GridViewItem> mItemList;
  // リソース
  private int mResource = 0;
  /**
   * グリッドビュー表示用
   */
  public static class GridViewItem {
    public String imagePath;
    public String folderPath;

  }

  /**
   * コンストラクタ
   * @param context
   */
  public ImageGridViewAdapter(Context context, ArrayList<GridViewItem> data, int resource) {
    super();
    this.mContext = context;
    this.mItemList = data;
    this.mResource = resource;
    // サービスの取得（メインのライフサイクルから外れる）
    mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  /**
   * データ数取得
   */
  @Override
  public int getCount() {
    return mItemList.size();
  }

  /**
   * 指定された項目を取得
   * @param i
   * @return
   */
  @Override
  public Object getItem(int i) {
    return mItemList.get(i);
  }

  /**
   * 指定された項目識別用ID値取得
   * @param i
   * @return
   */
  @Override
  public long getItemId(int i) {
    return 0;
  }

  /**
   * グリッドビューにデータ表示(setAdapter実行にて呼び出される)
   * @param i
   * @param convertView
   * @param parent
   * @return
   */
  @Override
  public View getView(int i, View convertView, ViewGroup parent) {
    // ファイルパス取得
    GridViewItem gridViewItem = mItemList.get(i);
    String imageFilePath = gridViewItem.imagePath;
    String imageFolderPath = gridViewItem.folderPath;

    // ビューが空の場合
    if(convertView == null)
    {
      convertView = mLayoutInflater.from(mContext).inflate(R.layout.grid_item_image,null);
    }

    ImageView imageView = (ImageView) convertView.findViewById(R.id.gv_image);
    TextView textView = (TextView) convertView.findViewById(R.id.gv_text);

    // ビューサイズ設定
    SetGridViewSize(imageView);

    // 画像を設定
    if(imageView != null)
    {
      // 画像サイズ取得
      BitmapFactory.Options preOptions = Common.GetBitMapSize(imageFilePath);

      /// メモリ削減対策
      int imageCompSize = (preOptions.outWidth * 4)/WindowManager.GetHeight(); // 画面縮小サイズ計算
      // ビットマップ設定
      BitmapFactory.Options bmpOption = GetBitMapOption(mContext,imageCompSize);


      // ビットマップオブジェクトの生成
      Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath,bmpOption);
      imageView.setImageBitmap(bitmap);
      // フォルダ名設定
      String[] folderStr = imageFolderPath.split("/",0);
      textView.setText(folderStr[folderStr.length - 1]);
    }

    return convertView;
  }

  /**
   * グリッドビューアイテムのサイズ設定
   * @param imageView
   */
  private void SetGridViewSize(ImageView imageView)
  {
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
    params.width = WindowManager.GetHeight() / 2;
    params.height = (WindowManager.GetHeight() / 4) - 10;
    imageView.setLayoutParams(params);
  }
}
