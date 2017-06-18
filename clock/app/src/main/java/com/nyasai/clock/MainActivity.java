package com.nyasai.clock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

  // 時計管理クラス
  Clock m_clsClock;
  Alarm m_clsAlarm;
  // アラーム設定クラス
  AlarmSetting m_clsAlarmSetting;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      // 時計オブジェクト生成
      m_clsClock = new Clock(this);
      // 時計起動
      m_clsClock.TimerStart((TextView)findViewById(R.id.tvClock));

      // アラーム設定オブジェクト設定
      m_clsAlarmSetting = new AlarmSetting();
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.onClickFloatingSetteingButton();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.action_settings) {
        return true;
      }

      return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
      m_clsClock.onDestroy();
      super.onDestroy();
  }

  private void onClickFloatingSetteingButton(){
    FloatingActionButton clsAlarmSetBtn = (FloatingActionButton)findViewById(R.id.fab_setting);
    clsAlarmSetBtn.setOnClickListener(new View.OnClickListener(){
      public void onClick(View view){
        Intent clsIntent = new Intent(getApplicationContext(),AlarmSetting.class);
        startActivity(clsIntent);
      }
    });
  }


}
