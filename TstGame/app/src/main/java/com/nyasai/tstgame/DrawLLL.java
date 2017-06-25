package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class DrawLLL extends AbstractGameObject{
  private Paint p;
//  private final float fMltShtX = 2.5f;
//  private final float fMltShtY =5.5f;
//  private final float fMltEndX =100;
//  private final float fMltEndY =200;


  /**
   * コンストラクタ
   * @param clsCntext
   * @param iWdth
   * @param iHght
   */
  public DrawLLL(Context clsCntext, int iWdth, int iHght) {
    super(clsCntext, R.drawable.lll, iWdth, iHght);
  }


  // LLL描画
  public void SetLLL(SurfaceHolder surfaceHolder){
//    // キャンバスロック（描画非更新）
//    Canvas canvas = surfaceHolder.lockCanvas();
//    // 描画色設定
//    canvas.drawColor(Color.GRAY);
//    p.setColor(Color.GREEN);
//    RectF rect = new RectF(canvas.getWidth() /fMltShtX,canvas.getHeight()/fMltShtY,canvas.getWidth()/fMltShtX+fMltEndX,canvas.getHeight()/fMltShtY+fMltEndY);
//    canvas.drawOval(rect,p);
//    RectF rect2 = new RectF(canvas.getWidth()/fMltShtX,canvas.getHeight()/fMltShtY+100,canvas.getWidth()/fMltShtX+fMltEndX+fMltEndX,canvas.getHeight()/fMltShtY+fMltEndY);
//    canvas.drawOval(rect2,p);
//
//    Log.d("DEBUG_W", String.valueOf(canvas.getWidth()));
//    Log.d("DEBUG_H", String.valueOf(canvas.getHeight()));
//    // キャンバス非更新解除
//    surfaceHolder.unlockCanvasAndPost(canvas);
  }


}
