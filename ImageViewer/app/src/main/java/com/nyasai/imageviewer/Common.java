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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
        if(imageCompSize != -1)
            bmpOption.inSampleSize = imageCompSize;
        // 不要オブジェクトのメモリ解放
        bmpOption.inPurgeable = true;
        // 現在の表示メトリクスの取得
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // ビットマップのサイズを現在の表示メトリクスに合わせる
        bmpOption.inDensity = displayMetrics.densityDpi;

        return bmpOption;
    }

    /**
     * 画像リサイズ値取得
     * @param preOptions
     * @return
     */
    public static int GetResizeValue(BitmapFactory.Options preOptions)
    {
        /// メモリ削減対策
        int imageCompSize;
        // 画面縮小サイズ計算
        if(preOptions.outWidth >= preOptions.outHeight)
            imageCompSize = (preOptions.outWidth * 4)/ WindowSizeManager.GetHeight();
        else
            imageCompSize = (preOptions.outHeight * 4)/ WindowSizeManager.GetWidth();

        return imageCompSize;
    }


    /**
     * 画像サイズ取得
     * @param imageFilePath
     * @return
     */
    public static BitmapFactory.Options GetBitMapSize(String imageFilePath)
    {
        // 画像サイズ一時取得
        BitmapFactory.Options preOptions = new BitmapFactory.Options();
        preOptions.inJustDecodeBounds = true; // Bitmapをロードしない
        InputStream stream = null;
        Bitmap preBitmap = null;
        try {
            stream = new FileInputStream(imageFilePath);
            preBitmap = BitmapFactory.decodeStream(stream,null,preOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return preOptions;
    }

    public static Bitmap GetBitmap(String imagePath,Context context)
    {
        Bitmap bitmap;
        // 画像サイズ取得
        BitmapFactory.Options preOptions = Common.GetBitMapSize(imagePath);
        /// メモリ削減対策
        int imageCompSize = (preOptions.outWidth * 1)/ WindowSizeManager.GetHeight(); // 画面縮小サイズ計算
        // ビットマップ設定
        BitmapFactory.Options bmpOption = GetBitMapOption(context,imageCompSize);
        bitmap = BitmapFactory.decodeFile(imagePath,bmpOption);

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
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
        return bitmap;
    }

    /**
     * 最大公約数(ユークリッド互除法)
     * @return
     */
    public static int CalcEuclideanAlgo(int a, int b)
    {
        int ans = 0;
        int tmp;
        int tmp_a=0,tmp_b=0;
        if(a>=b && b!=0)
        {
            tmp = a%b;
            tmp_a = b;
            tmp_b= tmp;
        }
        else if(b>=a && a!=0)
        {
            tmp = b%a;
            tmp_a = a;
            tmp_b= tmp;
        }
        if(tmp_b != 0)
            ans = CalcEuclideanAlgo(tmp_a,tmp_b);
        else
            ans = tmp_a;
        return ans;
    }
}
