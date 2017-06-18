package com.nyasai.clock;

import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

public class Alarm {

    // 時計オブジェクト
    private Clock m_clock;
    // アラーム動作フラグ
    boolean m_bAlmFlag;


    Alarm(Clock clock, MainActivity mainact){
        m_clock = clock;
        m_bAlmFlag = true;
    }


    // アラーム時間確認
    //todo. 引数にアラーム設定時間を受け取れるようにする
    public boolean CheckTim(){
        Date AlmDate = new Date(System.currentTimeMillis());
        long lNowTim = m_clock.GetDate();
        long lAlmTim = Util.DateToMSec(AlmDate);
        if(0 < lNowTim && m_bAlmFlag){
            m_bAlmFlag = false;
            return true;
        }
        return false;
    }

    // アラーム発生
    public void OnAlarm(){
        m_clock.m_Handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(m_clock.m_mainact, "テスト", Toast.LENGTH_LONG).show();
            }
        });
    }
}
