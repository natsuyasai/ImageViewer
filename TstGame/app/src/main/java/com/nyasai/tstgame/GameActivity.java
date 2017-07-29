package com.nyasai.tstgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

/**
 * ゲームメインアクテイビティ用クラス
 */
public class GameActivity extends AppCompatActivity {

  GameMainSView m_gSView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game_main);
    ContextManager.onCreateApplication(getApplicationContext());
    m_gSView = new GameMainSView(this);
    setContentView(m_gSView);
  }


  // デバッグ用
  public boolean onTouchEvent(MotionEvent event)
  {
    //X軸の取得
    float pointX = event.getX();

    //Y軸の取得
    float pointY = event.getY();

    //取得した内容をログに表示
    Log.d("TouchEvent", "X:" + pointX + ",Y:" + pointY);

    return true;
  }
}
