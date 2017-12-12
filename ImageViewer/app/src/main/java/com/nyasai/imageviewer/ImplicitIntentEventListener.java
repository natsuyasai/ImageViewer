package com.nyasai.imageviewer;

import java.util.EventListener;

public interface ImplicitIntentEventListener extends EventListener {

  /**
   * インテントリターン設定
   */
  void returnIntentEvent(Object object);
}
