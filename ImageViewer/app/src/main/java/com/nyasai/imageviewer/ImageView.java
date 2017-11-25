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
import android.support.v7.app.WindowDecorActionBar;
import android.util.AttributeSet;
import android.view.View;

import static com.nyasai.imageviewer.Common.GetBitMapOption;

/**
 * 画像描画用ビュー
 */
public class ImageView extends View
{
  private Context mContext;
  private Bitmap mBitmap;

  /**
   * コンストラクタ
   * @param context
   */
  public ImageView(Context context) {
    super(context);
    SetInit();
  }

  /**
   * コンストラクタ
   * @param context
   * @param filePath
   */
  public ImageView(Context context,String filePath) {
    super(context);
    SetInit();
    mContext = context;
    SetImage(filePath);
  }

  /**
   * コンストラクタ
   * @param context
   * @param attrs
   */
  public ImageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    SetInit();
  }

  /**
   * コンストラクタ
   * @param context
   * @param attrs
   * @param defStyleAttr
   */
  public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    SetInit();
  }


  /**
   * コンストラクタ
   * @param context
   * @param attrs
   * @param defStyleAttr
   * @param defStyleRes
   */
  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    SetInit();
  }

  /**
   * Implement this to do your drawing.
   *
   * @param canvas the canvas on which the background will be drawn
   */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if(mBitmap != null)
    {
      Paint paint = new Paint();
      int left=0,top=0;
      if(mBitmap.getWidth() > mBitmap.getHeight())
      {
        top = WindowManager.GetContentHeight()/2 - mBitmap.getHeight()/2;
      }
      else
      {
        left =WindowManager.GetContentWidth()/2 - mBitmap.getWidth()/2;
      }
      canvas.drawBitmap(mBitmap,left,top,paint);
    }
  }

  /**
   * 初期設定
   */
  private void SetInit()
  {
  }

  /**
   * 画像描画
   * @param imagePath
   */
  private void SetImage(String imagePath)
  {
    // 画像サイズ取得
    BitmapFactory.Options preOptions = Common.GetBitMapSize(imagePath);
    /// メモリ削減対策
    int imageCompSize = (preOptions.outWidth * 4)/WindowManager.GetHeight(); // 画面縮小サイズ計算
    //int imageCompSize = -1;
    // ビットマップ設定
    BitmapFactory.Options bmpOption = GetBitMapOption(mContext,imageCompSize);
    mBitmap = BitmapFactory.decodeFile(imagePath,bmpOption);

    // 画像リサイズ
    // note. outWidth:outHeight = GetWidth:GetHeight
    int width,height;
    if(preOptions.outWidth >= preOptions.outHeight)
    {
      width = WindowManager.GetContentWidth();
      int a = (preOptions.outHeight*WindowManager.GetContentWidth())/preOptions.outWidth;
      height = a;
    }
    else
    {
      height = WindowManager.GetContentHeight();
      int a = (preOptions.outWidth*WindowManager.GetContentHeight())/preOptions.outHeight;
      width = a;
    }
    mBitmap = Bitmap.createScaledBitmap(mBitmap,width,height,true);
  }
}
