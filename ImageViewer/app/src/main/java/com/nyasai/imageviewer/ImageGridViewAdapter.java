package com.nyasai.imageviewer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
   * グリッドビューの1コマずつの要素参照を保持
   */
  static class ViewHolder
  {
    ImageView imageView;
    TextView textView;
    CheckBox checkBox;
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
    String[] splitStr = imageFolderPath.split("/",0); // ファイル名リスト

    ViewHolder holder;
    // ビューが空の場合
    if(convertView == null)
    {
      // 現在のコマの参照を保持
      holder = new ViewHolder();
      convertView = mLayoutInflater.from(mContext).inflate(R.layout.grid_item_image,null);
      holder.imageView = (ImageView) convertView.findViewById(R.id.gv_image);
      holder.textView = (TextView) convertView.findViewById(R.id.gv_text);
      holder.checkBox = (CheckBox)convertView.findViewById(R.id.gv_checkbox);
      convertView.setTag(holder);
      // テキストビュー設定
      holder.textView.setText(splitStr[splitStr.length - 1]);
    }
    else
    {
      // 保持されていればそれを使用
      holder = (ViewHolder)convertView.getTag();
    }

    // ビューサイズ設定
    setGridViewSize(holder.imageView);

    // 画像を設定
    if(holder.imageView != null)
    {
      // 画像設定
      Picasso.Builder builder = new Picasso.Builder(mContext);
      // エラー発生時，エラー内容を表示させる
      builder.listener(new Picasso.Listener() {
        @Override
        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
          Log.d("getView",e.toString());
        }
      });
      // リサイズ値取得
      BitmapFactory.Options preOptions = Common.getBitMapSize(imageFilePath);
      int resizeVal = Common.getResizeValue(preOptions);
      if(resizeVal != 0) {
        // 画像の読み込み，設定
        builder.build()
            .load(new File(imageFilePath))
            .resize(preOptions.outWidth / resizeVal, preOptions.outHeight / resizeVal)
            .into(holder.imageView, new Callback() {
              @Override
              public void onSuccess() {
              }
              @Override
              public void onError() {
                Log.d("getView", "Error");
              }
            });
      }
      // 非同期読み込み
//      AsyncTask<GetViewTaskStartParam,Void,GetViewTaskResulttParam> task = new AsyncTask<GetViewTaskStartParam, Void, GetViewTaskResulttParam>() {
//        private ImageView mImageView;
//        private String tag;
//
//
//        @Override
//        protected GetViewTaskResulttParam doInBackground(GetViewTaskStartParam... getViewTaslStartParams) {
//          GetViewTaskResulttParam reslt = new GetViewTaskResulttParam();
//          String imageFilePath = getViewTaslStartParams[0].imageFilePath;
//
//          // 設定先のビュー指定
//          mImageView = getViewTaslStartParams[0].viewHolder.imageView;
//          tag = mImageView.getTag().toString();
//
//          // 画像サイズ取得
//          BitmapFactory.Options preOptions = Common.getBitMapSize(imageFilePath);
//          /// メモリ削減対策
//          int imageCompSize;
//          // 画面縮小サイズ計算
//          if(preOptions.outWidth >= preOptions.outHeight)
//            imageCompSize = (preOptions.outWidth * 4)/ WindowSizeManager.getHeight();
//          else
//            imageCompSize = (preOptions.outHeight * 4)/ WindowSizeManager.getWidth();
//          // ビットマップ設定
//          BitmapFactory.Options bmpOption = getBitMapOption(mContext,imageCompSize);
//          // ビットマップオブジェクトの生成
//          reslt.bitmap = BitmapFactory.decodeFile(imageFilePath,bmpOption);
//
//          reslt.holder = getViewTaslStartParams[0].viewHolder;
//          return reslt;
//        }
//
//        /**
//         *
//         */
//        @Override
//        protected void onPostExecute(GetViewTaskResulttParam getViewTaskResulttParam) {
//          super.onPostExecute(getViewTaskResulttParam);
//          if(getViewTaskResulttParam != null)
//          {
//            // イメージの設定
//            if(tag.equals(mImageView.getTag()))
//            {
//              mImageView.setImageBitmap(getViewTaskResulttParam.bitmap);
//              mImageView.setVisibility(View.VISIBLE);
//            }
//          }
//        }
//      };
//      // パラメータ設定
//      GetViewTaskStartParam param = new GetViewTaskStartParam();
//      param.imageFilePath = imageFilePath;
//      param.viewHolder = new ViewHolder();
//      param.viewHolder.imageView = imageView;
//      task.execute(param);
    }
    return convertView;
  }

  /**
   * グリッドビューアイテムのサイズ設定
   * @param imageView
   */
  private void setGridViewSize(ImageView imageView)
  {
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
    params.width = WindowSizeManager.getWidth() / 2;
    params.height = (WindowSizeManager.getHeight() / 4) - 10;
    imageView.setLayoutParams(params);
  }

}
