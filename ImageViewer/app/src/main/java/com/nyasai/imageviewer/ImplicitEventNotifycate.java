package com.nyasai.imageviewer;

/**
 * イベント通知用クラス
 */
public class ImplicitEventNotifycate {
  // イベントリスナ
  private ImplicitIntentEventListener mEventListener;


  /**
   * 暗黙的インテントリターンイベント
   */
  public void sendImplicitIntentEvent(Object object)
  {
    mEventListener.returnIntentEvent(object);
  }



  /**
   * リスナーセット
   * @param listener
   */
  public void setImplicitIntentListener(ImplicitIntentEventListener listener)
  {
    mEventListener = listener;
  }


}
