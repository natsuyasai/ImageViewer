package com.nyasai.imageviewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * プログレスダイアログ表示用クラス
 */
public class AsyncProgressDialog extends AsyncTask<Void,Void,String>{

  private Activity mActivity;
  private ProgressDialog mProgressDialog;

  /**
   * コンストラクタ
   * @param activity
   */
  public AsyncProgressDialog(Activity activity)
  {
    mActivity = activity;
  }

  /**
   * Runs on the UI thread before {@link #doInBackground}.
   *
   * @see #onPostExecute
   * @see #doInBackground
   */
  @Override
  protected void onPreExecute() {
    super.onPreExecute();

    // プログレスダイアログの生成
    mProgressDialog = new ProgressDialog(mActivity);
    // 設定
    mProgressDialog.setMessage("Loading...");
    // 表示
    mProgressDialog.show();

  }

  /**
   * Override this method to perform a computation on a background thread. The
   * specified parameters are the parameters passed to {@link #execute}
   * by the caller of this task.
   * <p>
   * This method can call {@link #publishProgress} to publish updates
   * on the UI thread.
   *
   * @param voids The parameters of the task.
   * @return A result, defined by the subclass of this task.
   * @see #onPreExecute()
   * @see #onPostExecute
   * @see #publishProgress
   */
  @Override
  protected String doInBackground(Void... voids) {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * <p>Runs on the UI thread after {@link #doInBackground}. The
   * specified result is the value returned by {@link #doInBackground}.</p>
   * <p>
   * <p>This method won't be invoked if the task was cancelled.</p>
   *
   * @param s The result of the operation computed by {@link #doInBackground}.
   * @see #onPreExecute
   * @see #doInBackground
   * @see #onCancelled(Object)
   */
  @Override
  protected void onPostExecute(String s) {
    super.onPostExecute(s);
    // ダイアログ表示中なら閉じる
    CroseDialog();
  }

  /**
   * <p>Applications should preferably override {@link #onCancelled(Object)}.
   * This method is invoked by the default implementation of
   * {@link #onCancelled(Object)}.</p>
   * <p>
   * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
   * {@link #doInBackground(Object[])} has finished.</p>
   *
   * @see #onCancelled(Object)
   * @see #cancel(boolean)
   * @see #isCancelled()
   */
  @Override
  protected void onCancelled() {
    super.onCancelled();
    CroseDialog();
  }

  public void CroseDialog()
  {
    // ダイアログ表示中なら閉じる
    if(mProgressDialog != null && mProgressDialog.isShowing())
    {
      mProgressDialog.dismiss();
    }
  }


}
