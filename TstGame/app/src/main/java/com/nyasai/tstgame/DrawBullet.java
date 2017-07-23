package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;

public class DrawBullet extends AbstractGameObject {

  private int m_iDefX;
  private int m_iDefY;
  private float m_fVelocity = 10;
  private boolean blInitFlg;
  Handler m_clsHndl;

  /// 定数
  final static int DELAY_TIME = 1000;


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
    m_clsHndl = new Handler();
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

  public void BulletTimerStart(final Canvas canvas)
  {
    m_clsHndl.postDelayed(new Runnable() {
      @Override
      public void run() {
        MoveSin(canvas);
        m_clsHndl.postDelayed(this,DELAY_TIME);
      }
    },DELAY_TIME);
  }

  public void BulletTimerStop()
  {
    m_clsHndl.removeCallbacksAndMessages(null);
  }
}
