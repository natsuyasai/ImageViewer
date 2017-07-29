package com.nyasai.tstgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * ゲーム描画用SurfaceViewクラス
 */
public class GameMainSView extends SurfaceView implements SurfaceHolder.Callback{

  private GameThread m_clsGThread;

  /**
   * コンストラクタ
   * @param context
   * @param attrs
   * @param defStyleAttr
   */
  public GameMainSView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  /**
   * コンストラクタ
   * @param context
   */
  public GameMainSView(Context context) {
    super(context);
    SurfaceHolder clsHolder = getHolder();
    clsHolder.addCallback(this);
    /*
    スレッド用インスタンス生成
     */
    m_clsGThread = new GameThread(clsHolder,context,new Handler(){
      @Override
      public void handleMessage(Message msg) {
        super.handleMessage(msg);
      }
    });
    // ウィンドウサイズ設定
    m_clsGThread.SetWindowSize(getWidth(),getHeight());
  }

  /**
   * コンストラクタ
   * @param context
   * @param attrs
   */
  public GameMainSView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  /**
   * サーフェス生成時
   * @param surfaceHolder
   */
  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {
    // 描画用スレッド開始
    m_clsGThread.start();
  }

  /**
   * サーフェス変更時
   * @param surfaceHolder
   * @param i
   * @param i1
   * @param i2
   */
  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

  }

  /**
   * サーフェス削除時
   * @param surfaceHolder
   */
  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    m_clsGThread.SetThredSts(false);
    // 削除
    m_clsGThread = null;
  }


  /**
   * SurfaceView用スレッドクラス
   */
  public class GameThread extends Thread {

    /**
     * サーフェスホルダ
     */
    SurfaceHolder clsSdcHolder;
    /**
     * スレッド実行ループ状態
     */
    private boolean blShouldContinue = true;
    /**
     * LLL描画クラス
     */
    private DrawLLL m_clsDrawLLL;
    /**
     * 自機描画クラス
     */
    private DrawOwn m_clsDrawOwn;
    /**
     * 敵弾クラス
     */
    private DrawBullet[] m_clsDrawBllt;
    /**
     * LLL生成リスト
     */
    private List<DrawLLL> m_lstLLL;


    /**
     * ウィンドウサイズ
     */
    private int m_iWidth;
    private int m_iHeight;

    private int m_iGameState = GameState.STS_INIT;


    /**********************************/
    /** 定数                          */
    /*********************************/
    // LLLデフォルトサイズ
    static final int LLL_SIZE = 250;
    // LLL初期サイズXオフセット
    private final float LLL_DEF_X = 2.5f;
    // LLL初期サイズYオフセット
    private final float LLL_DEF_Y =5.5f;

    // 自機デフォルトサイズ
    static final int OWN_SIZE = 150;
    // 自機初期サイズXオフセット
    private final float OWN_DEF_X = 2.5f;
    // 自機初期サイズYオフセット
    private final float OWN_DEF_Y =1.5f;

    // 敵弾デフォルトサイズ
    static final int BLT_SIZE = 150;
    // 敵弾初期サイズXオフセット
    private final int BLT_DEF_X = 300;
    // 敵弾初期サイズYオフセット
    private final int BLT_DEF_Y = 300;
    // 弾数
    private final int BLT_NUM = 10;

    private int i=0;


    /**
     * コンストラクタ
     * @param surfaceHolder
     * @param context
     * @param handler
     */
    public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler){
      this.clsSdcHolder = surfaceHolder;
      m_clsDrawLLL = new DrawLLL(ContextManager.GetContext(),LLL_SIZE,LLL_SIZE);
      m_clsDrawOwn = new DrawOwn(context,OWN_SIZE,OWN_SIZE);
      m_clsDrawBllt = new DrawBullet[10];

    }

    /**
     * スレッド実行
     */
    @Override
    public void run() {
      while(blShouldContinue){
        Canvas clsCanvas = clsSdcHolder.lockCanvas();
        this.Draw(clsCanvas);
        clsSdcHolder.unlockCanvasAndPost(clsCanvas);
      }
    }

    /**
     * スレッド動作設定
     * @param blSts
     */
    public void SetThredSts(boolean blSts){
      blShouldContinue = blSts;
    }


    /**
     * 描画
     * @param clsCanvas
     */
    public void Draw(Canvas clsCanvas){
      clsCanvas.drawColor(Color.GRAY);
      m_iWidth = clsCanvas.getWidth();
      m_iHeight = clsCanvas.getHeight();
      switch (m_iGameState){
        case GameState.STS_INIT: // ゲーム初期化
          m_iGameState = GameState.STS_GAME_MAIN01; // 状態遷移
          break;
        case GameState.STS_GAME_MAIN01: // ゲームメイン01
          m_clsDrawLLL.Draw(clsCanvas,(int)(clsCanvas.getWidth() / LLL_DEF_X+i),(int)(clsCanvas.getHeight()/ LLL_DEF_Y+i)); // 敵機表示
          i++;
          break;
        default:
          break;
      }


    }


    /**
     * ウィンドウサイズSetter
     * @param iWdth
     * @param iHght
     */
    public void SetWindowSize(int iWdth, int iHght){
      this.m_iHeight = iHght;
      this.m_iHeight = iHght;
    }


  }

}
