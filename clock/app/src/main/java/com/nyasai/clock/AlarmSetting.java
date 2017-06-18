package com.nyasai.clock;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AlarmSetting extends Activity {

  // 時スピナー設定データ
  private int[] iSpnrHour=new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
  // 分スピナー設定データ
  private int[] iSpnrMinute= new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
      31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};

  // 時計設定スピナー
  // 時スピナー
  private Spinner m_SpinnerHour;
  private Spinner m_SpinnerMinite;

  public AlarmSetting(){
  }


  // アラーム設定画面初期化
  public void InitAlartmSetting(){
    m_SpinnerHour = (Spinner)findViewById(R.id.spnr_hour);
    m_SpinnerMinite = (Spinner)findViewById(R.id.spnr_hour);
    // 時設定
    ArrayAdapter<String> csAdapterHour = new ArrayAdapter<String>(this,R.layout.alarm_setting);
    for (int val : iSpnrHour ) {
      csAdapterHour.add(Integer.toString(val));
    }
    m_SpinnerHour.setAdapter(csAdapterHour);

    // 分設定
    ArrayAdapter<String> csAdapterMinute = new ArrayAdapter<String>(this,R.layout.alarm_setting);
    for (int val : iSpnrMinute ) {
      csAdapterMinute.add(Integer.toString(val));
    }
    m_SpinnerMinite.setAdapter(csAdapterMinute);
  }
}
