package com.nyasai.imageviewer;

import android.content.Context;

/**
 * 自アプリケーションコンテキスト管理クラス
 */
public class ContextManager {
  /**
   * 自インスタンス(シングルトン)
   */
  private static ContextManager instance = null;

  // アプリケーションコンテキスト
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

  public static Context getContext(){
    return mAppContext.getApplicationContext();
  }



}
