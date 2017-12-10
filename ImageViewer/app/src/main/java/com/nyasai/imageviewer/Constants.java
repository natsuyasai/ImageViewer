package com.nyasai.imageviewer;

/**
 * 定数クラス
 * 全クラスで共通して使用する定数のみを定義する
 */
public final class Constants {
  // デバッグモード(0:OFF,1:ON)
  public static final boolean DEBUG_MODE = true;

  // サブアクティビティ起動時タグ(ファイルパス)
  public static final String FILE_PATH = "FILE_PATH";
  // サブアクティビティ起動時タグ(ファイルパスリスト)
  public static final String FILE_PATH_LIST = "FILE_PATH_LIST";
  // サブアクティビティ起動時タグ(画像位置)
  public static final String FILE_POSITION = "FILE_POSITION";
  // サブアクティビティ起動時タグ(フォルダパス)
  public static final String FOLDER_PATH = "FOLDER_PATH";
  // モード
  public static final String MODE = "MODE";
  // リクエストコード
  public static final int REQ_CODE_SUB_ACT = 10;
  // 画像ファイル拡張子
  public static final String[] IMAGE_EXTENSIONS={".jpg",".jpeg",".JPG",".JPEG",".png",".PNG",".bmp",".BMP",".gif",".GIF","tiff",".TIFF","tif",".TIF"};

  // 通常起動
  public static final int MODE_DEF = 0;
  // 暗黙的インテントによる起動
  public static final int MODE_IMPLICIT_INTENT = 1;


}
