package com.newbiechen.chatdemo.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by PC on 2016/11/26.
 */

public final class FileUtils {

    /**
     * 获取外存的路径
     * @return String：storagePath
     */
    public static String getStoragePath(){
        //判断是否存在外置存储
        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)){
            File file = Environment.getExternalStorageDirectory();
            return file.getAbsolutePath();
        }
        else {
            return "";
        }
    }

    /**
     * 根据文件的相对路径，获取文件
     * @return File 文件
     */
    public static File getStorageFile(String filePath){
        //首先获取文件的外存路径
        String storagePath = getStoragePath();
        //获取文件
        File file = new File(storagePath+File.separator+filePath);
        //判断文件是否存在
        if (file.exists()){
            return file;
        }
        else{
            return null;
        }
    }

}
