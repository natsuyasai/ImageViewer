package com.nyasai.imageviewer;

/**
 * Created by yasai on 2017/11/18.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 共通メソッドクラス
 */
public class Common {

    Common()
    {
    }

    /**
     * bitmapオプション設定
     * @param context
     * @return
     */
    public static BitmapFactory.Options GetBitMapOption(Context context, int imageCompSize)
    {
        BitmapFactory.Options bmpOption = new BitmapFactory.Options();
        // ARGBそれぞれ0~127階調の色を使用
        bmpOption.inPreferredConfig = Bitmap.Config.ARGB_4444;
        // 画像を1/20に
        bmpOption.inSampleSize = imageCompSize;
        // 不要オブジェクトのメモリ解放
        bmpOption.inPurgeable = true;
        // 現在の表示メトリクスの取得
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // ビットマップのサイズを現在の表示メトリクスに合わせる
        bmpOption.inDensity = displayMetrics.densityDpi;

        return bmpOption;
    }

}
