package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * ゲームオブジェクト用抽象クラス
 */
public abstract class AbstractGameObject {
  /** 描画オブジェクト */
  protected Drawable m_clsDrawableImg;
  /** 横幅 */
  protected int m_iWdth;
  /** 縦幅 */
  protected int m_iHght;
  /** X座標 */
  protected int m_iX;
  /** Y座標 */
  protected int m_iY;

  /**
   * コンストラクタ
   */
  public AbstractGameObject(Context clsCntext, int iRscID, int iWdth, int iHght) {
    m_clsDrawableImg = clsCntext.getResources().getDrawable(iRscID);
    this.m_iWdth = iWdth;
    this.m_iHght = iHght;
  }


  /**
   * オブジェクト描画
   * @param clsCanvas
   * @param iX
   * @param iY
   */
  public void Draw(Canvas clsCanvas, int iX,int iY){
    m_clsDrawableImg.setBounds(iX,iY,iX+m_iWdth,iY+m_iHght);
    m_iX = iX;
    m_iY = iY;
    m_clsDrawableImg.draw(clsCanvas);
  }
}
