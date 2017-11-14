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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 画像表示グリッドビュー用アダプタ
 */
public class ImageGridViewAdapter extends BaseAdapter {

  // コンテキスト
  private Context mContext;
  // ビュー登録
  private LayoutInflater mLayoutInflater;
  // 表示データ
  private ArrayList<String> mItemList;
  // リソース
  private int mResource = 0;
  /**
   * グリッドビュー表示用
   */
  public static class GridViewItem {
    public ImageView mImageView;
    public TextView mTextView;

  }

  /**
   * コンストラクタ
   * @param context
   */
  public ImageGridViewAdapter(Context context, ArrayList<String> data, int resource) {
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
    String imageFilePath = mItemList.get(i);

    // ビューが空の場合
    if(convertView == null)
    {
      convertView = mLayoutInflater.from(mContext).inflate(R.layout.grid_item_image,null);
    }

    ImageView imageView = (ImageView) convertView.findViewById(R.id.gv_image);
    TextView textView = (TextView) convertView.findViewById(R.id.gv_text);

    // 画像を設定
    if(imageView != null)
    {
      /// メモリ削減対策
      // ビットマップ設定
      BitmapFactory.Options bmpOption = new BitmapFactory.Options();
      // ARGBそれぞれ0~127階調の色を使用
      bmpOption.inPreferredConfig = Bitmap.Config.ARGB_4444;
      // 画像を1/20に
      bmpOption.inSampleSize = 20;
      // 不要オブジェクトのメモリ解放
      bmpOption.inPurgeable = true;
      // 現在の表示メトリクスの取得
      DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
      // ビットマップのサイズを現在の表示メトリクスに合わせる
      bmpOption.inDensity = displayMetrics.densityDpi;
      // ビットマップオブジェクトの生成
      Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath,bmpOption);
      imageView.setImageBitmap(bitmap);
      textView.setText(imageFilePath);
    }

    return convertView;
  }
}
