package com.nyasai.clock;

import android.os.Debug;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Clock {

    // 周期処理用タイマ
    private Timer m_Timer;
    // UIスレッド動作用ハンドラ
    protected Handler m_Handler;

    // 現時刻保持
    private  long m_lDate;
    // 現在時刻（HH:mm:ss）
    private DateFormat m_DatFrmt;

    // アラーム確認用
    private Alarm m_Alarm;

    // メインアクティビティインスタンス
    MainActivity m_mainact;

    // コンストラクタ
    Clock(MainActivity obj){
        m_Timer = null;
        m_Handler = new Handler();
        m_Alarm = new Alarm(this,obj);
        m_DatFrmt = new SimpleDateFormat("HH:mm:ss");
        m_mainact = obj;
    }


    // アクセッサ
    public void SetDate(long lTime){
        m_lDate = lTime;
    }

    public long GetDate(){
        return m_lDate;
    }


    protected String GetNowDateTime(){
        SetDate(System.currentTimeMillis());
        Log.d("",String.valueOf(GetDate()));
        return m_DatFrmt.format(GetDate());
    }


    // タイマー
    public void TimerStart(final TextView clsTVClock) {
        if(m_Timer == null) {
            m_Timer = new Timer(true);
            m_Timer.schedule(new TimerTask() {
                // 時計更新
                @Override
                public void run() {
                    m_Handler.post(new Runnable() {
                        @Override
                        public void run() {
                            SetTvClock(clsTVClock, GetNowDateTime());
                        }
                    });
                    // アラームチェック
                    if(m_Alarm.CheckTim()){
                        m_Alarm.OnAlarm();
                    }
                }
            }, 1000, 1000);
        }
    }

    // テキスト設定
    private void SetTvClock(TextView clsTVClock,String str){
        clsTVClock.setText(str);
    }

    public void onDestroy(){
        m_Timer.cancel();
        m_Timer = null;
    }
}
