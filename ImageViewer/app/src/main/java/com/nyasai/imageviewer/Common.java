package com.nyasai.imageviewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 共通メソッドクラス
 */
public class Common {

    Common() {
    }

    /**
     * bitmapオプション設定
     *
     * @param context コンテキスト
     * @return 変換後オプション値
     */
    public static BitmapFactory.Options getBitMapOption(Context context, int imageCompSize) {
        BitmapFactory.Options bmpOption = new BitmapFactory.Options();
        // ARGBそれぞれ0~127階調の色を使用
        bmpOption.inPreferredConfig = Bitmap.Config.ARGB_4444;
        // 画像を1/20に
        if (imageCompSize != -1)
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
     *
     * @param preOptions 画像ファイル情報
     * @return 画像リサイズ値
     */
    public static int getResizeValue(BitmapFactory.Options preOptions) {
        if (preOptions != null) {
            /// メモリ削減対策
            int imageCompSize;
            // 画面縮小サイズ計算
            if (preOptions.outWidth >= preOptions.outHeight)
                imageCompSize = (preOptions.outWidth * 4) / WindowSizeManager.getHeight();
            else
                imageCompSize = (preOptions.outHeight * 4) / WindowSizeManager.getWidth();

            return imageCompSize;
        } else {
            return 0;
        }
    }


    /**
     * 画像サイズ取得
     *
     * @param imageFilePath 取得対象画像ファイルパス
     * @return 画像サイズ
     */
    public static BitmapFactory.Options getBitMapSize(String imageFilePath) {
        // 画像サイズ一時取得
        BitmapFactory.Options preOptions = new BitmapFactory.Options();
        preOptions.inJustDecodeBounds = true; // Bitmapをロードしない
        InputStream stream;
        Bitmap preBitmap = null;
        try {
            stream = new FileInputStream(imageFilePath);
            preBitmap = BitmapFactory.decodeStream(stream, null, preOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return preOptions;
    }

    /**
     * 画像ファイルパスからBitmapイメージを取得
     *
     * @param imagePath 対象のファイルパス
     * @param context   コンテキスト
     * @return Bitmapオブジェクト
     */
    public static Bitmap getBitmap(String imagePath, Context context) {
        Bitmap bitmap;
        // 画像サイズ取得
        BitmapFactory.Options preOptions = Common.getBitMapSize(imagePath);
        /// メモリ削減対策
        int imageCompSize; // 画面縮小サイズ計算
        try {
            imageCompSize = preOptions.outWidth / WindowSizeManager.getHeight();
            // ビットマップ設定
            BitmapFactory.Options bmpOption = getBitMapOption(context, imageCompSize);
            bitmap = BitmapFactory.decodeFile(imagePath, bmpOption);

            // 画像リサイズ
            // note. outWidth:outHeight = getWidth:getHeight
            int width, height;
            if (preOptions.outWidth >= preOptions.outHeight) {
                width = WindowSizeManager.getContentWidth();
                height = (preOptions.outHeight * WindowSizeManager.getContentWidth()) / preOptions.outWidth;
            } else {
                height = WindowSizeManager.getContentHeight();
                width = (preOptions.outWidth * WindowSizeManager.getContentHeight()) / preOptions.outHeight;
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        } catch (NullPointerException e) {
            return null;
        }
        return bitmap;
    }

    /**
     * 最大公約数(ユークリッド互除法)
     *
     * @return 最大公約数
     */
    public static int calcEuclideanAlgo(int a, int b) {
        int ans = 0;
        int tmp;
        int tmp_a = 0, tmp_b = 0;
        if (a >= b && b != 0) {
            tmp = a % b;
            tmp_a = b;
            tmp_b = tmp;
        } else if (b >= a && a != 0) {
            tmp = b % a;
            tmp_a = a;
            tmp_b = tmp;
        }
        if (tmp_b != 0)
            ans = calcEuclideanAlgo(tmp_a, tmp_b);
        else
            ans = tmp_a;
        return ans;
    }

    /**
     * アクティビティの再描画
     *
     * @param activity 再描画対象アクティビティ
     */
    public static void refreshActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }
}
