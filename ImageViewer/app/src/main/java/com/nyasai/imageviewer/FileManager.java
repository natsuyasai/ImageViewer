package com.nyasai.imageviewer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
    createFolderPathList();
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
   * @return
   */
  private void createFolderPathList()
  {
    mfolderPathList = createFolderPaths();
  }

  /**
   * フォルダパスリスト取得
   * @return
   */
  public ArrayList<String> createFolderPaths()
  {
    // フォルダパスリスト
    ArrayList<String > folderPathList = new ArrayList<String>();
    // URI情報の先頭に移動
    mCursor.moveToFirst();
    int pathIndex = mCursor.getColumnIndex(
        MediaStore.Images.Media.DATA);
    int nameIndex = mCursor.getColumnIndex(
        MediaStore.Images.Media.DISPLAY_NAME);
    String filePath = "";
    String folderPath = "";
    String regex = "";
    Pattern pattern;
    Matcher matcher;
    while(!mCursor.isAfterLast()){
      // ファイルパス取得
      filePath = mCursor.getString(pathIndex);
      // ファイル名取得
      regex = "/"+mCursor.getString(nameIndex);
      // パスからファイル名を削除
      pattern = Pattern.compile(regex);
      matcher = pattern.matcher(filePath);
      folderPath = matcher.replaceFirst("");
      // リストに追加
      folderPathList.add(folderPath);
      mCursor.moveToNext();
    }

    // 重複削除
    Set<String> set = new HashSet<>(folderPathList);
    ArrayList<String > folderPathListReturn = new ArrayList<>(set);
    mfolderPathList = folderPathListReturn;
    return mfolderPathList;
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
      setFilePaths();
    }
  }

  /**
   * ファイルパス一覧設定(文字列)
   */
  public void setFilePaths()
  {
    // フォルダパスリスト
    ArrayList<String > filePathList = new ArrayList<String>();
    // URI情報の先頭に移動
    mCursor.moveToFirst();
    int pathIndex = mCursor.getColumnIndex(
        MediaStore.Images.Media.DATA);
    String folderPath = "";
    while(!mCursor.isAfterLast()){
      folderPath = mCursor.getString(pathIndex);
      // リストに追加
      filePathList.add(folderPath);
      mCursor.moveToNext();
    }
    // 保持
    mfilePathList = filePathList;
  }

  /**
   * 代表画像ファイルパス取得
   * note.最新順に取得
   * @return
   */
  public ArrayList<ImageGridViewAdapter.GridViewItem> getFilePathForRepresentative()
  {
    ArrayList<ImageGridViewAdapter.GridViewItem> gridViewItems = new ArrayList<ImageGridViewAdapter.GridViewItem>();
    HashMap<String,Integer> folderPathList = new HashMap<String,Integer>();

    // URI情報の末尾に移動
    mCursor.moveToLast();
    int pathIndex = mCursor.getColumnIndex(
            MediaStore.Images.Media.DATA);
    int nameIndex = mCursor.getColumnIndex(
            MediaStore.Images.Media.DISPLAY_NAME);
    String filePath = "";
    String folderPath = "";
    // ファイルパス削除用
    String regex = "";
    Pattern pattern;
    Matcher matcher;
    // ファイル確認
    while(!mCursor.isBeforeFirst()){
      // ファイルパス取得
      filePath = mCursor.getString(pathIndex);
      // ファイル名
      regex = "/"+mCursor.getString(nameIndex);
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
      mCursor.moveToPrevious();
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
    // 指定フォルダのファイル一覧を取得
    File[] files =new File(folderPath).listFiles();
    // 名前順のため，更新日時順にソートし直す
    files = execFilelistSortByUpdateDate(files, SORT_KIND_ASCENDING);
    ArrayList<String > filePathList = new ArrayList<String>();
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
            filePathList.add(files[i].getPath());
            break;
          }
        }
      }
    }
    mfilePathList = filePathList;
    return mfilePathList;
  }

  /**
   * ファイルリストのソート処理
   * @param files_ ファイルリスト
   * @param sortKind ソート種別
   * @return ソート後のファイルリスト
   */
  private final int SORT_KIND_ASCENDING = 0; // 昇順
  private final int SROT_KIND_DESCENDING =1; // 降順
  private File[] execFilelistSortByUpdateDate(File[] files_, final int sortKind)
  {
    File[] files = files_;
    Arrays.sort(files, new Comparator<File>() {
      @Override
      public int compare(File f1, File f2) {
        if(sortKind == SORT_KIND_ASCENDING) {
          // 昇順
          return f1.lastModified() >= f2.lastModified() ? -1 : 1;
        }
        else
        {
          // 降順
          return f1.lastModified() >= f2.lastModified() ? 1 : -1;
        }
      }
    });
    return files;
  }


  /**
   * 指定ファイルの削除
   * @param filePath
   * @return
   */
  public static void deleteFile(String filePath)
  {
    String[] DELETE_PROJ = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
    File file = new File(filePath);
    if(file.exists()) {
      // DBから指定パス箇所取得
      Cursor cursor = ContextManager.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
          DELETE_PROJ,
          MediaStore.Images.Media.DATA + " = ?",
          new String[] { filePath },
          null);
      // 生データ削除
      if(cursor.getCount() != 0) {
        cursor.moveToFirst();
        Uri deleteUri = ContentUris.appendId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon(),
            cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))).build();
        ContextManager.getContext().getContentResolver().delete(deleteUri, null, null);
      }
      // ファイル削除
      file.delete();
      cursor.close();
    }
  }
  public static boolean deleteFile(File filePath)
  {
    return filePath.delete();
  }

  public static void deleteFile(ArrayList<String>  filePathList)
  {

    // 指定リスト内のファイル全削除
    for (String filePath: filePathList) {
      deleteFile(filePath);
    }
  }
  public void deleteAllFile()
  {
    // フォルダ内全画像削除
    for (String filePath: mfilePathList) {
      File file = new File(filePath);
      file.delete();
    }
  }

}
