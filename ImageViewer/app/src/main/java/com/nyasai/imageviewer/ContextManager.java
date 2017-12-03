package com.nyasai.imageviewer;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;

/**
 * 自アプリケーションコンテキスト管理クラス
 */
public class ContextManager {
  /**
   * 自インスタンス(シングルトン)
   */
  private static ContextManager instance = null;

  /**
   *
   */
  private static Context mAppContext;

  /**
   * アプリ起動時
   * コンテキストの取得
   * @param context
   */
  public static void onCreateApplication(Context context)
  {
    instance = new ContextManager(context);
  }

  /**
   * コンストラクタ
   * @param context
   */
  private ContextManager(Context context) {
    this.mAppContext = context;
  }


  /**
   * インスタンス取得
   * @return
   */
  public static ContextManager getInstance() {
    return instance;
  }

  public static Context GetContext(){
    return mAppContext.getApplicationContext();
  }



}
