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
  // サブアクティビティ起動時タグ(フォルダパス)
  public static final String FOLDER_PATH = "FOLDER_PATH";
  // 画像ファイル拡張子
  public static final String[] IMAGE_EXTENSIONS={".jpg",".jpeg",".png",".bmp",".gif","tiff","tif"};

}
