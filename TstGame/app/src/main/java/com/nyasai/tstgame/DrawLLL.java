package com.nyasai.tstgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;

public class DrawLLL {
  private Paint p;
  private final float fMltShtX = 2.5f;
  private final float fMltShtY =5.5f;
  private final float fMltEndX =100;
  private final float fMltEndY =200;

  public DrawLLL() {
    p = new Paint();
  }


  // LLL描画
  public void DoDrawLLL(SurfaceHolder surfaceHolder){
    // キャンバスロック（描画非更新）
    Canvas canvas = surfaceHolder.lockCanvas();
    // 描画色設定
    canvas.drawColor(Color.GRAY);
    p.setColor(Color.GREEN);
    RectF rect = new RectF(canvas.getWidth() /fMltShtX,canvas.getHeight()/fMltShtY,canvas.getWidth()/fMltShtX+fMltEndX,canvas.getHeight()/fMltShtY+fMltEndY);
    canvas.drawOval(rect,p);
    RectF rect2 = new RectF(canvas.getWidth()/fMltShtX,canvas.getHeight()/fMltShtY+100,canvas.getWidth()/fMltShtX+fMltEndX+fMltEndX,canvas.getHeight()/fMltShtY+fMltEndY);
    canvas.drawOval(rect2,p);

    Log.d("DEBUG_W", String.valueOf(canvas.getWidth()));
    Log.d("DEBUG_H", String.valueOf(canvas.getHeight()));
    // キャンバス非更新解除
    surfaceHolder.unlockCanvasAndPost(canvas);
  }
}
