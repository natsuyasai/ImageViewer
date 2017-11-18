package com.nyasai.imageviewer;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;

/**
 * 画面サイズ管理クラス
 */
public class WindowManager {

  // 自インスタンス
  private static WindowManager instance = null;

  // 画面サイズ
  public static int width;
  public static int height;

  /**
   * 画面サイズ設定(画面起動時に設定)
   * @param context
   */
  public static void onCreateApplication(Context context)
  {
    instance = new WindowManager();
    android.view.WindowManager windowManager = (android.view.WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    Display display = windowManager.getDefaultDisplay();
    Point point = new Point();
    display.getSize(point);
    width = point.x;
    height = point.y;
  }

  /**
   * 画面サイズ取得
   * @return
   */
  public static int GetWidth()
  {
    return width;
  }
  public static int GetHeight()
  {
    return height;
  }
}
