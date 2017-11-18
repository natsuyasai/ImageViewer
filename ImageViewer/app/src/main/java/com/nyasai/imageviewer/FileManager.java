package com.nyasai.imageviewer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Paint;
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
 * TODO
 * 特定フォルダを指定すれば，そのフォルダ配下のみ管理できるようにする
 * メンバに管理しているフォルダのパスを保持する
 * 全体管理ならばパス管理用メンバには無効値(null以外)を設定する
 */

/**
 * ファイル取得用クラス
 * 端末内のイメージファイルを取得，管理用クラス
 */
public class FileManager {

  // 画像ファイル一覧(URI)
  Cursor mCursor;
  // フォルダパス一覧
  ArrayList<String> mfolderPathList;
  // 画像ファイル一覧
  ArrayList<String> mfilePathList;

  /**
   * コンストラクタ
   */
  public FileManager()
  {
  }

  /**
   * 初期化処理
   * ここでファイル一覧とフォルダパスとフォルダ一覧を取得する
   */
  public void GetAllFileList()
  {
    // 画像ファイル一覧取得
    LoadImageFileUri();
    // 上記で取得した画像ファイル一覧(URI)からフォルダリストを取得
    CreateFolderPathList(mCursor);
  }

  /**
   * 画像ファイル一覧情報取得
   * @return
   */
  public Cursor GetImageFileUri()
  {
    return mCursor;
  }
  public ArrayList<String> GetImageFilePath()
  {
    return mfilePathList;
  }

  /**
   * フォルダパスリスト取得
   * @return
   */
  public ArrayList<String> GetFolderPaths()
  {
    return mfolderPathList;
  }

  /**
   * フォルダパス一覧取得
   * @param cursor
   * @return
   */
  private void CreateFolderPathList(Cursor cursor)
  {
    mfolderPathList = GetFolderPaths(cursor);
  }

  /**
   * フォルダパスリスト取得
   * 引数で渡された情報に対して処理を行う
   * @param cursor
   * @return
   */
  public ArrayList<String> GetFolderPaths(Cursor cursor)
  {
    // フォルダパスリスト
    ArrayList<String > folderPathList = new ArrayList<String>();
    // URI情報の先頭に移動
    cursor.moveToFirst();
    int pathIndex = cursor.getColumnIndex(
        MediaStore.Images.Media.DATA);
    int nameIndex = cursor.getColumnIndex(
        MediaStore.Images.Media.DISPLAY_NAME);
    String filePath = "";
    String folderPath = "";
    String regex = "";
    Pattern pattern;
    Matcher matcher;
    while(!cursor.isAfterLast()){
      // ファイルパス取得
      filePath = cursor.getString(pathIndex);
      // ファイル名取得
      regex = "/"+cursor.getString(nameIndex);
      // パスからファイル名を削除
      pattern = Pattern.compile(regex);
      matcher = pattern.matcher(filePath);
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

  /**
   * 画像ファイル一覧読み込み
   * @return
   */
  private void LoadImageFileUri()
  {

    ContentResolver contentResolver = ContextManager.GetContext().getContentResolver();

    // 選択列(射影)
    String[] projection = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};

    Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,null,null,null);

    if(cursor != null)
    {
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

      mCursor = cursor;
      SetFilePaths(cursor);
    }
  }

  /**
   * ファイルパス一覧設定(文字列)
   * @param cursor
   */
  public void SetFilePaths(Cursor cursor)
  {
    // フォルダパスリスト
    ArrayList<String > filePathList = new ArrayList<String>();
    // URI情報の先頭に移動
    cursor.moveToFirst();
    int pathIndex = cursor.getColumnIndex(
        MediaStore.Images.Media.DATA);
    String folderPath = "";
    while(!cursor.isAfterLast()){
      folderPath = cursor.getString(pathIndex);
      // リストに追加
      filePathList.add(folderPath);
      cursor.moveToNext();
    }
    // 保持
    mfilePathList = filePathList;
  }

  /**
   * 代表画像ファイルパス取得
   * @param cursor
   * @return
   */
  public ArrayList<ImageGridViewAdapter.GridViewItem> GetFilePathForRepresentative(Cursor cursor)
  {
    ArrayList<ImageGridViewAdapter.GridViewItem> gridViewItems = new ArrayList<ImageGridViewAdapter.GridViewItem>();
    HashMap<String,Integer> folderPathList = new HashMap<String,Integer>();

    // URI情報の先頭に移動
    cursor.moveToFirst();
    int pathIndex = cursor.getColumnIndex(
            MediaStore.Images.Media.DATA);
    int nameIndex = cursor.getColumnIndex(
            MediaStore.Images.Media.DISPLAY_NAME);
    String filePath = "";
    String folderPath = "";
    // ファイルパス削除用
    String regex = "";
    Pattern pattern;
    Matcher matcher;
    // ファイル確認
    while(!cursor.isAfterLast()){
      // ファイルパス取得
      filePath = cursor.getString(pathIndex);
      // ファイル名
      regex = "/"+cursor.getString(nameIndex);
      // パスからファイル名を削除
      pattern = Pattern.compile(regex);
      matcher = pattern.matcher(filePath);
      folderPath = matcher.replaceFirst("");
      // まだ取得していないフォルダパスのファイルなら取得
      if(folderPathList.containsKey(folderPath) != true)
      {
        // マップにフォルダパス追加
        folderPathList.put(folderPath,1);
        // リストに追加
        ImageGridViewAdapter.GridViewItem item = new ImageGridViewAdapter.GridViewItem();
        item.imagePath = filePath;
        item.folderPath = folderPath;
        gridViewItems.add(item);
      }
      cursor.moveToNext();
    }

    return gridViewItems;
  }


}
