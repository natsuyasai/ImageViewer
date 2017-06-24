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
  DrawLLL clsDrawLLL;

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
    clsDrawLLL = new DrawLLL();
    getHolder().addCallback(
        new SurfaceHolder.Callback() {
          @Override
          public void surfaceCreated(SurfaceHolder surfaceHolder) {
            clsDrawLLL.DoDrawLLL(surfaceHolder);
          }

          @Override
          public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

          }

          @Override
          public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

          }
        }
    );

  }




}
