package com.nyasai.imageviewer;

import android.content.Context;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.view.Display;
import android.view.View;

/**
 * 画面サイズ管理クラス
 */
public class WindowManager {

  // 自インスタンス
  private static WindowManager instance = null;

  // 画面サイズ
  public static int width;
  public static int height;

  private static int contentWidth;
  private static int contentHeight;

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
  public static void SetViewWindowsSize(View view)
  {
    contentWidth = view.getWidth();
    contentHeight = view.getHeight();
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

  /**
   * 画面サイズ取得（ビューサイズ）
   * @return
   */
  public static int GetContentWidth()
  {
    return contentWidth;
  }
  public static int GetContentHeight()
  {
    return contentHeight;
  }
}
