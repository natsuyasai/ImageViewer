package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameMainSView extends SurfaceView {

  private Paint p;
  private DrawLLL clsDrawLLL;
  private float fX;
  private float fY;

  public GameMainSView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    Init();
  }

  public GameMainSView(Context context) {
    super(context);
    Init();
  }

  private void Init(){
    p = new Paint();
    fX = 0.0f;
    fY = 0.0f;
    clsDrawLLL = new DrawLLL();
    getHolder().addCallback(
        new SurfaceHolder.Callback() {
          @Override
          public void surfaceCreated(SurfaceHolder surfaceHolder) {
            clsDrawLLL.DoLLL(surfaceHolder);
          }

          @Override
          public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            clsDrawLLL.DoLLL(surfaceHolder);
            //clsDrawLLL.InjectBullet(surfaceHolder,fX+=1,fY+=1);
          }

          @Override
          public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

          }
        }
    );

  }




}
