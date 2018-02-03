package com.nyasai.imageviewer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nyasai.imageviewer.Constants.IMAGE_EXTENSIONS;

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
  public void getAllFileList()
  {
    // 画像ファイル一覧取得
    loadImageFileUri();
    // 上記で取得した画像ファイル一覧(URI)からフォルダリストを取得
    createFolderPathList(mCursor);
  }

  /**
   * 画像ファイル一覧情報取得
   * @return
   */
  public Cursor getImageFileUri()
  {
    return mCursor;
  }
  public ArrayList<String> getImageFilePath()
  {
    return mfilePathList;
  }

  /**
   * フォルダパスリスト取得
   * @return
   */
  public ArrayList<String> getFolderPaths()
  {
    return mfolderPathList;
  }

  /**
   * フォルダパス一覧取得
   * @param cursor
   * @return
   */
  private void createFolderPathList(Cursor cursor)
  {
    mfolderPathList = getFolderPaths(cursor);
  }

  /**
   * フォルダパスリスト取得
   * 引数で渡された情報に対して処理を行う
   * @param cursor
   * @return
   */
  public ArrayList<String> getFolderPaths(Cursor cursor)
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
  private void loadImageFileUri()
  {

    ContentResolver contentResolver = ContextManager.getContext().getContentResolver();

    // 選択列(射影)
    String[] projection = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};

    Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,null,null,null);

    if(cursor != null)
    {
      if(Constants.DEBUG_MODE)
      {
        cursor.moveToFirst();
        int pathIndex = cursor.getColumnIndex(
            MediaStore.Images.Media.DATA);
        int nameIndex = cursor.getColumnIndex(
            MediaStore.Images.Media.DISPLAY_NAME);
        while(!cursor.isAfterLast()){
          Log.d("loadImageFileUri","Image Path = " + cursor.getString(pathIndex));
          Log.d("loadImageFileUri","Image Name = " + cursor.getString(nameIndex));
          cursor.moveToNext();
        }
      }
      cursor.moveToFirst();

      mCursor = cursor;
      setFilePaths(cursor);
    }
  }

  /**
   * ファイルパス一覧設定(文字列)
   * @param cursor
   */
  public void setFilePaths(Cursor cursor)
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
   * note.最新順に取得
   * @param cursor
   * @return
   */
  public ArrayList<ImageGridViewAdapter.GridViewItem> getFilePathForRepresentative(Cursor cursor)
  {
    ArrayList<ImageGridViewAdapter.GridViewItem> gridViewItems = new ArrayList<ImageGridViewAdapter.GridViewItem>();
    HashMap<String,Integer> folderPathList = new HashMap<String,Integer>();

    // URI情報の末尾に移動
    cursor.moveToLast();
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
    while(!cursor.isBeforeFirst()){
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
      cursor.moveToPrevious();
    }

    return gridViewItems;
  }

  /**
   * 指定フォルダ内の画像ファイル一覧取得
   * @param folderPath
   * @return
   */
  public ArrayList<String> getAllFile(String folderPath)
  {
    File[] files =new File(folderPath).listFiles();
    ArrayList<String> imageFilePath = new ArrayList<String>();

    // 画像ファイル取得
    for(int i=0; i< files.length; i++)
    {
      // 対象データがファイルの場合
      if(files[i].isFile())
      {
        // 指定拡張子にマッチする場合はファイルパスを取得
        for(int ext=0; ext<IMAGE_EXTENSIONS.length; ext++)
        {
          if( files[i].getName().endsWith(IMAGE_EXTENSIONS[ext].toString()))
          {
            imageFilePath.add(files[i].getPath());
            break;
          }
        }
      }
    }
    return imageFilePath;
  }


  /**
   * 指定ファイルの削除
   * @param filePath
   * @return
   */
  public boolean deleteFile(String filePath)
  {
    File file = new File(filePath);
    return file.delete();
  }
  public boolean deleteFile(File filePath)
  {
    return filePath.delete();
  }

}
