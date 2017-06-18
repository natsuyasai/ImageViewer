package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameMainSView extends SurfaceView {
  Paint p;

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

    p.setColor(Color.BLACK);
    getHolder().addCallback(
        new SurfaceHolder.Callback() {
          @Override
          public void surfaceCreated(SurfaceHolder surfaceHolder) {

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
