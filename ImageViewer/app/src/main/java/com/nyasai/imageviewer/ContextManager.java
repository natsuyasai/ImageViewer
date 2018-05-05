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
     *
     * @param context コンテキスト
     */
    public static void onCreateApplication(Context context) {
        instance = new ContextManager(context);
    }

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    private ContextManager(Context context) {
        this.mAppContext = context;
    }


    /**
     * インスタンス取得
     *
     * @return thisインスタンス
     */
    public static ContextManager getInstance() {
        return instance;
    }

    /**
     * コンテキスト取得
     *
     * @return コンテキスト
     */
    public static Context getContext() {
        return mAppContext.getApplicationContext();
    }


}
