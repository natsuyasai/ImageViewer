package com.nyasai.imageviewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

/**
 * 画面サイズ管理クラス
 */
public class WindowSizeManager {

    // 自インスタンス
    private static WindowSizeManager instance = null;

    // 画面全体
    public static int width;
    public static int height;

    // ナビゲーショバー込み
    private static int contentAllWidth;
    private static int contentAllHeight;
    // ビュー本体のみ
    private static int contentWidth;
    private static int contentHeight;

    /**
     * 画面サイズ設定(画面起動時に設定)
     *
     * @param context コンテキスト
     */
    public static void onCreateApplication(Context context) {
        instance = new WindowSizeManager();
        android.view.WindowManager windowManager = (android.view.WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        width = point.x;
        height = point.y;
    }

    public static void setViewAllWindowsSize(Display display) {
        Point point = new Point();
        display.getSize(point);
        contentAllWidth = point.x;
        contentAllHeight = point.y;
    }

    public static void setViewWindowsSize(View view) {
        contentWidth = view.getWidth();
        contentHeight = view.getHeight();
    }

    /**
     * 画面サイズ取得
     *
     * @return サイズ
     */
    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    /**
     * 画面サイズ取得（タイトルバー抜きビューサイズ）
     *
     * @return サイズ
     */
    public static int getContentAllWidth() {
        return contentAllWidth;
    }

    public static int getContentAllHeight() {
        return contentAllHeight;
    }

    /**
     * 画面サイズ取得（ビューサイズ）
     *
     * @return サイズ
     */
    public static int getContentWidth() {
        return contentWidth;
    }

    public static int getContentHeight() {
        return contentHeight;
    }
}
