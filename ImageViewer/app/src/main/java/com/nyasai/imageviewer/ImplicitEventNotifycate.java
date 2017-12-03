package com.nyasai.imageviewer;

import android.app.Activity;

/**
 * イベント通知用クラス
 */
public class ImplicitEventNotifycate {
  private ImplicitIntentEventListener mEventListener;


  /**
   * 暗黙的インテントリターンイベント
   */
  public void SendImplicitIntentEvent(Object object)
  {
    mEventListener.ReturnIntentEvent(object);
  }



  /**
   * リスナーセット
   * @param listener
   */
  public void SetImplicitIntentListener(ImplicitIntentEventListener listener)
  {
    mEventListener = listener;
  }


}
