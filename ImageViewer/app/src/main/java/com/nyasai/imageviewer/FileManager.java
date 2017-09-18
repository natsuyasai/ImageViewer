package com.nyasai.imageviewer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ファイル取得用クラス
 * 端末内のイメージファイルを取得，管理用クラス
 */
public class FileManager {

  /**
   * コンストラクタ
   */
  public FileManager()
  {

  }

  /**
   * 画像ファイル一覧取得
   * @return
   */
  public Cursor GetImageFileUri()
  {

    ContentResolver contentResolver = ContextManager.GetContext().getContentResolver();

    // 選択列(射影)
    String[] projection = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};

    Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,null,null,null);

    if(cursor == null)
      return null;

    // デバッグ出力
    if(Constants.DEBUG_MODE)
    {
      cursor.moveToFirst();
      int pathIndex = cursor.getColumnIndex(
          MediaStore.Images.Media.DATA);
      int nameIndex = cursor.getColumnIndex(
          MediaStore.Images.Media.DISPLAY_NAME);
      while(!cursor.isAfterLast()){
        Log.d("TEST","画像パス = " + cursor.getString(pathIndex));
        Log.d("TEST","画像名 = " + cursor.getString(nameIndex));
        cursor.moveToNext();
      }
    }

    cursor.moveToFirst();
    return cursor;
  }

  /**
   * フォルダパス一覧取得
   * @param cursor
   * @return
   */
  public ArrayList<String> GetFolderPaths(Cursor cursor)
  {
    // フォルダパスリスト
    ArrayList<String > folderPathList = new ArrayList<String>();

    cursor.moveToFirst();
    int pathIndex = cursor.getColumnIndex(
        MediaStore.Images.Media.DATA);
    int nameIndex = cursor.getColumnIndex(
        MediaStore.Images.Media.DISPLAY_NAME);
    String folderPath = "";
    String regex = "";
    while(!cursor.isAfterLast()){
      // パスからファイル名を削除
      folderPath = cursor.getString(pathIndex);
      regex = "/"+cursor.getString(nameIndex);
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(folderPath);
      folderPath = matcher.replaceFirst("");
      // リストに追加
      folderPathList.add(folderPath);
      cursor.moveToNext();
    }

    // 重複削除
    Set<String> set = new HashSet<>(folderPathList);
    ArrayList<String > folderPathListReturn = new ArrayList<>(set);

    return folderPathListReturn;
  }

}
