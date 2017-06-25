package com.nyasai.tstgame;

import android.content.Context;

/**
 * 自機描画クラス
 */
public class DrawOwn extends AbstractGameObject {

  /**
   * コンストラクタ
   *
   * @param clsCntext
   * @param iWdth
   * @param iHght
   */
  public DrawOwn(Context clsCntext, int iWdth, int iHght) {
    super(clsCntext, R.drawable.reddit, iWdth, iHght);
  }
}
