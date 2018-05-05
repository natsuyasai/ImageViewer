package com.nyasai.imageviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import static com.nyasai.imageviewer.Common.getBitMapOption;

/**
 * 画像描画用ビュー
 */
@SuppressLint("AppCompatCustomView")
public class MyImageView extends ImageView {
    // コンテキスト
    private Context mContext;
    // 現表示中ビットマップ
    private Bitmap mBitmap;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public MyImageView(Context context) {
        super(context);
        setInit(context);
    }

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param filePath フォルダパス
     */
    public MyImageView(Context context, String filePath) {
        super(context);
        setInit(context);
        setImage(filePath);
    }

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param attrs アトリビュート
     */
    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setInit(context);
    }

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param attrs アトリビュート
     * @param defStyleAttr デフォルトスタイル
     */
    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInit(context);
    }

    /**
     * 描画
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            Paint paint = new Paint();
            // 画面中央に表示するよう座標を変換
            int left = 0, top = 0;
            if (mBitmap.getWidth() > mBitmap.getHeight()) {
                top = WindowSizeManager.getContentHeight() / 2 - mBitmap.getHeight() / 2;
            } else {
                left = WindowSizeManager.getContentWidth() / 2 - mBitmap.getWidth() / 2;
            }
            int navigationsize = WindowSizeManager.getHeight() - WindowSizeManager.getContentAllHeight();
            if (navigationsize == 0)
                navigationsize = 70;
            canvas.drawBitmap(mBitmap, left, top + navigationsize, paint);
            this.setBackgroundColor(R.color.DarkGray);
        }
        super.onDraw(canvas);
    }

    /**
     * 初期設定
     */
    private void setInit(Context context) {
        mContext = context;
        setWillNotDraw(false);
    }

    /**
     * レイアウト生成時
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (super.getWidth() == 0) {
            // View未形成
            return;
        }
    }

    /**
     * 画像描画
     *
     * @param imagePath 画像ファイルパス
     */
    public void setImage(String imagePath) {
        if (mBitmap != null)
            mBitmap.recycle();
        // 画像サイズ取得
        BitmapFactory.Options preOptions = Common.getBitMapSize(imagePath);
        /// メモリ削減対策
        int imageCompSize = (preOptions.outWidth * 4) / WindowSizeManager.getHeight(); // 画面縮小サイズ計算
        //int imageCompSize = -1;
        // ビットマップ設定
        BitmapFactory.Options bmpOption = getBitMapOption(mContext, imageCompSize);
        mBitmap = BitmapFactory.decodeFile(imagePath, bmpOption);

        // 画像リサイズ
        // note. outWidth:outHeight = getWidth:getHeight
        int width, height;
        if (preOptions.outWidth >= preOptions.outHeight) {
            width = WindowSizeManager.getContentWidth();
            int a = (preOptions.outHeight * WindowSizeManager.getContentWidth()) / preOptions.outWidth;
            height = a;
        } else {
            height = WindowSizeManager.getContentHeight();
            int a = (preOptions.outWidth * WindowSizeManager.getContentHeight()) / preOptions.outHeight;
            width = a;
        }
        mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
    }
}
