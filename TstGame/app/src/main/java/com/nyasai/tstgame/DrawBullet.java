package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Canvas;

public class DrawBullet extends AbstractGameObject {

  private int m_iDefX;
  private int m_iDefY;
  private float m_fVelocity = 10;
  private boolean blInitFlg;


  /**
   * コンストラクタ
   *
   * @param clsCntext
   * @param iWdth
   * @param iHght
   */
  public DrawBullet(Context clsCntext, int iWdth, int iHght) {
    super(clsCntext, R.drawable.bullet, iWdth, iHght);
    blInitFlg = true;
  }


  /**
   * 初期位置設定
   * @param iX
   * @param iY
   */
  public void SetIniPos(int iX,int iY){
    if(blInitFlg == true)
    {
      m_iDefX = iX;
      m_iDefY = iY;
      this.m_iX = m_iDefX;
      this.m_iY = m_iDefY;
      blInitFlg = false;
    }
    else{
      // 何もしない
    }
  }

  /**
   * 座標移動
   * @param canvas
   */
  public void DrawLoop(Canvas canvas){
    super.Draw(canvas,m_iDefX,m_iY);
    m_iY+=m_fVelocity;
  }

  public void MoveSin(Canvas canvas){
    super.Draw(canvas,m_iX,m_iY);
    m_iX += (int) Math.tan(m_iX) + m_fVelocity;
    m_iY += (int) Math.sin(m_iY) + m_fVelocity;
  }

  public void SetVelocity(float v){
    m_fVelocity = v;
  }
}
