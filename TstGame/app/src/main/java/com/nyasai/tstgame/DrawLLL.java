package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;

public class DrawLLL extends AbstractGameObject{
  private Paint p;


  /**
   * コンストラクタ
   * @param clsCntext
   * @param iWdth
   * @param iHght
   */
  public DrawLLL(Context clsCntext, int iWdth, int iHght) {
    super(clsCntext, R.drawable.lll, iWdth, iHght);
    m_iX = Integer.MIN_VALUE;
    m_iY = Integer.MIN_VALUE;
  }

  @Override
  public void Draw(Canvas clsCanvas, int iX, int iY) {
    super.Draw(clsCanvas, iX, iY);
    m_iX = iX;
    m_iY = iY;
  }

  public float GetPosX(){
    return this.m_iX;
  }
  public float GetPosY(){
    return this.m_iY;
  }




}
