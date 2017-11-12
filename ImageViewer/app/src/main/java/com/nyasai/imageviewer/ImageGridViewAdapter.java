package com.nyasai.imageviewer;

import android.app.Activity;
import android.content.Context;
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
  private ArrayList<GridViewItem> mItemList;
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
  public ImageGridViewAdapter(Context context, ArrayList<GridViewItem> data, int resource) {
    this.mContext = context;
    this.mItemList = data;
    this.mResource = resource;
    // xmlビューの登録
    mLayoutInflater = LayoutInflater.from(context);
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

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    Activity activity = (Activity)mContext;
    GridViewItem item = (GridViewItem) getItem(i);
    if(view == null)
    {
      view = activity.getLayoutInflater().inflate(mResource,null);
    }
    //((ImageView) view.findViewById(R.id.gv_image)).setImageURI(item.);
    //((TextView) view.findViewById(R.id.gv_text)).setImageURI(item.);
    return view;
  }
}
