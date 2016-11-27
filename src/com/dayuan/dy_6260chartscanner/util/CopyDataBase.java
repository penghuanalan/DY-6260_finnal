package com.dayuan.dy_6260chartscanner.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CopyDataBase {
    private static final String DB_PATH = "/data/data/com.dayuan.dy_6260chartscanner/databases/";  
    private static final String DB_NAME = "dayuan03.db";  
      
    public static void copyDBToDatabases(Context context) throws IOException {  
        String outFileName = DB_PATH + DB_NAME;  
      
        File file = new File(DB_PATH);  
        if (!file.mkdirs()) {  
            file.mkdirs();  
        }  
      
        if (new File(outFileName).exists()) {  
            // 数据库已经存在，无需复制  
            return;  
        }  
      
        InputStream myInput = context.getAssets().open(DB_NAME);  
        OutputStream myOutput = new FileOutputStream(outFileName);  
      
        byte[] buffer = new byte[1024];  
        int length;  
        while ((length = myInput.read(buffer)) > 0) {  
            myOutput.write(buffer, 0, length);  
        }  
      
        myOutput.flush();  
        myOutput.close();  
        myInput.close();  
    }   
}
