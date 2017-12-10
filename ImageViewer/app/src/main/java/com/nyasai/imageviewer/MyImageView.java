package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import static com.nyasai.imageviewer.Common.GetBitMapOption;

/**
 * 画像描画用ビュー
 */
@SuppressLint("AppCompatCustomView")
public class MyImageView extends ImageView
{
  // コンテキスト
  private Context mContext;
  // 現表示中ビットマップ
  private Bitmap mBitmap;

  /**
   * コンストラクタ
   * @param context
   */
  public MyImageView(Context context) {
    super(context);
    SetInit(context);
  }

  /**
   * コンストラクタ
   * @param context
   * @param filePath
   */
  public MyImageView(Context context, String filePath) {
    super(context);
    SetInit(context);
    SetImage(filePath);
  }

  /**
   * コンストラクタ
   * @param context
   * @param attrs
   */
  public MyImageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    SetInit(context);
  }

  /**
   * コンストラクタ
   * @param context
   * @param attrs
   * @param defStyleAttr
   */
  public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    SetInit(context);
  }

  /**
   * 描画
   *
   * @param canvas the canvas on which the background will be drawn
   */
  @SuppressLint("ResourceAsColor")
  @Override
  protected void onDraw(Canvas canvas) {
    if(mBitmap != null)
    {
      Paint paint = new Paint();
      // 画面中央に表示するよう座標を変換
      int left=0,top=0;
      if(mBitmap.getWidth() > mBitmap.getHeight())
      {
        top = WindowSizeManager.GetContentHeight()/2 - mBitmap.getHeight()/2;
      }
      else
      {
        left = WindowSizeManager.GetContentWidth()/2 - mBitmap.getWidth()/2;
      }
      int navigationsize = WindowSizeManager.GetHeight()-WindowSizeManager.GetContentAllHeight();
      if(navigationsize == 0)
        navigationsize = 70;
      canvas.drawBitmap(mBitmap,left,top+navigationsize,paint);
      this.setBackgroundColor(R.color.DarkGray);
    }
    super.onDraw(canvas);
  }

  /**
   * 初期設定
   */
  private void SetInit(Context context)
  {
    mContext = context;
    setWillNotDraw(false);
  }

  /**
   * レイアウトが生成時
   */
  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if(super.getWidth() == 0)
    {
      // View未形成
      return;
    }
  }

  /**
   * 画像描画
   * @param imagePath
   */
  public void SetImage(String imagePath)
  {
    if(mBitmap != null)
      mBitmap.recycle();
    // 画像サイズ取得
    BitmapFactory.Options preOptions = Common.GetBitMapSize(imagePath);
    /// メモリ削減対策
    int imageCompSize = (preOptions.outWidth * 4)/ WindowSizeManager.GetHeight(); // 画面縮小サイズ計算
    //int imageCompSize = -1;
    // ビットマップ設定
    BitmapFactory.Options bmpOption = GetBitMapOption(mContext,imageCompSize);
    mBitmap = BitmapFactory.decodeFile(imagePath,bmpOption);

    // 画像リサイズ
    // note. outWidth:outHeight = GetWidth:GetHeight
    int width,height;
    if(preOptions.outWidth >= preOptions.outHeight)
    {
      width = WindowSizeManager.GetContentWidth();
      int a = (preOptions.outHeight* WindowSizeManager.GetContentWidth())/preOptions.outWidth;
      height = a;
    }
    else
    {
      height = WindowSizeManager.GetContentHeight();
      int a = (preOptions.outWidth* WindowSizeManager.GetContentHeight())/preOptions.outHeight;
      width = a;
    }
    mBitmap = Bitmap.createScaledBitmap(mBitmap,width,height,true);
  }
}
