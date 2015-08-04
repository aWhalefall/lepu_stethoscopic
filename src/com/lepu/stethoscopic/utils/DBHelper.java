package com.lepu.stethoscopic.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.core.lib.utils.main.LogUtilBase;
import com.core.lib.utils.main.StringUtilBase;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.config.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    public static final String DATABASE_NAME_MAIN = "audio.db";
    public static final String DATABASE_NAME_TEST = "test.db";

    //数据库版本号
    private static final int DATABASE_VERSION = 2;

    private static Context mContext = null;
    public static Object mLockObj = new Object();

    private String mDbName = "";

    public static DBHelper getInstance(String dbName) {
        if (mContext == null) {
            LogUtilBase.LogD("event", "context is null");
            return null;
        }
        return new DBHelper(dbName);
    }

    private DBHelper(String dbName) {
        super(mContext, dbName, null, DATABASE_VERSION);
        mDbName = dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            LogUtilBase.LogD(null, "onUpgrade==oldVersion" + oldVersion
                    + "newVersion=" + newVersion);
            //程序升级，更新为true
            AppConfig.setConfig(mContext, Const.CONFIG_APP_UPGRADE_USE, true);

            for (int i = (oldVersion + 1); i <= newVersion; i++)
                upgrade(db, i);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (this != null)
            this.close();
        super.finalize();
    }

    /*
     * 升级程序时，修改了数据库，需要在以下用sql语句去修改
     */
    private void upgrade(SQLiteDatabase db, int version) {
        LogUtilBase.LogD("DBHelper", version + "");
        switch (version) {
            case 1:
                break;
            case 2:
                //程序升级使用

                break;
            default:
                break;
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public static void checkDatabase(Context context) {
        synchronized (mLockObj) {
            mContext = context;
            DBHelper helper1 = getInstance(DATABASE_NAME_MAIN);
            if (helper1 != null) {
                helper1.createDatabase();
                helper1.close();
            }

            DBHelper helper2 = getInstance(DATABASE_NAME_TEST);
            if (helper2 != null) {
                helper2.createDatabase();
                helper2.close();
            }
        }
    }

    /**
     * 创建数据库
     */
    private void createDatabase() {
        String dbPath = SdLocal.getDatabasePath(mContext, mDbName);

        File pathFile = new File(dbPath);
        // 为了解决程序向下降级而加
        // int oldVersion = getDatabaseVersion();
        // if(oldVersion > DATABASE_NEW_VERSION)
        // {
        // FileUtilBase.deleteFile(pathFile);
        // LogUtilBase.LogD(TAG, "删除旧的数据库");
        //
        // //清除用户配置文件数据
        // LoginUtil.clearLoginInfo(context);
        // PedometerUtil.clearInfo(context);
        // }
        if (!pathFile.exists()) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                db.close();
                copyDB();
            } catch (Exception e) {
                LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
            }
        }
    }

    private void copyDB() {
        String dbPath = SdLocal.getDatabasePath(mContext, mDbName);
        int id = 0;
        if (mDbName.equals(DATABASE_NAME_MAIN))
            id = R.raw.audio;
        else if (mDbName.equals(DATABASE_NAME_TEST))
            id = R.raw.test;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = mContext.getResources().openRawResource(id);
            fos = new FileOutputStream(dbPath);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }

        } catch (Exception e) {
            LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
            }

        }
    }

    // ================
    public void updateVersion() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = SdLocal.getDatabasePath(mContext, mDbName);
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);

            checkDB.beginTransaction();
            int oldVersion = checkDB.getVersion();
            checkDB.setVersion(DATABASE_VERSION);
            checkDB.setTransactionSuccessful();

            int newVersion = checkDB.getVersion();
            LogUtilBase.LogD(TAG, "oldVersion=" + oldVersion);
            LogUtilBase.LogD(TAG, "newVersion=" + newVersion);

        } catch (SQLiteException e) {
            LogUtilBase.LogD(TAG, Log.getStackTraceString(e));
        } finally {
            if (checkDB != null) {
                checkDB.endTransaction();
                checkDB.close();
            }
        }
    }

    public int getDatabaseVersion() {
        SQLiteDatabase checkDB = null;
        int version = 0;
        try {
            String myPath = SdLocal.getDatabasePath(mContext, mDbName);
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            version = checkDB.getVersion();
        } catch (SQLiteException e) {
            LogUtilBase.LogD(TAG, Log.getStackTraceString(e));
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }

        return version;
    }

    public ArrayList<HashMap<String, Object>> query(String sql) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        synchronized (mLockObj) {
            try {
                db = this.getReadableDatabase();
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    int count = cursor.getColumnCount();
                    for (int i = 0; i < count; i++) {
                        map.put(cursor.getColumnName(i),
                                getColumnValue(cursor, i));
                    }
                    result.add(map);
                }
            } catch (Exception e) {
                LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
            } finally {
                try {
                    if (cursor != null)
                        cursor.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    if (db != null)
                        db.close();
                } catch (Exception e) {
                    LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
                }
            }
        }

        return result;
    }

    private Object getColumnValue(Cursor cursor, int index) {
        Object result = null;
        try {
            result = cursor.getString(index);
        } catch (Exception e) {

        }
        return result;
    }

    public boolean executeIsExists(String sql) {
        boolean exists = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        synchronized (mLockObj) {
            try {
                db = this.getReadableDatabase();
                cursor = db.rawQuery(sql, null);
                exists = cursor.moveToFirst();
            } catch (Exception e) {
                LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
            } finally {
                try {
                    if (cursor != null)
                        cursor.close();
                } catch (Exception e) {
                    LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
                }
                try {
                    if (db != null)
                        db.close();
                } catch (Exception e) {
                    LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
                }
            }
        }

        return exists;
    }

    public boolean isTableExists(String tableName) {
        String sql = String
                .format("select count(*) from sqlite_master where type='table' and name='?'",
                        tableName);
        String result = executeScalar(sql);
        return !StringUtilBase.stringIsEmpty(result)
                && Integer.parseInt(result) == 1;
    }

    public String executeScalar(String sql) {
        ArrayList<HashMap<String, Object>> resultArray = query(sql);
        if (resultArray.size() > 0) {
            HashMap<String, Object> json = resultArray.get(0);
            Iterator<Object> it = json.values().iterator();
            while (it.hasNext()) {
                return (String) it.next();
            }
        }
        return "";
    }

    public boolean executeNonQuery(String sql) {
        SQLiteDatabase db = null;
        synchronized (mLockObj) {
            try {
                db = getWritableDatabase();
                db.execSQL(sql);
            } catch (Exception e) {
                LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
            } finally {
                try {
                    if (db != null)
                        db.close();
                } catch (Exception e) {
                    LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
                }
            }
        }

        return true;
    }

    public boolean executeNonQuery(String sql, Object[] args) {
        SQLiteDatabase db = null;
        synchronized (mLockObj) {
            try {
                db = getWritableDatabase();
                db.execSQL(sql, args);
            } catch (Exception e) {
                LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
            } finally {
                try {
                    if (db != null)
                        db.close();
                } catch (Exception e) {
                    LogUtilBase.LogD("Exception", Log.getStackTraceString(e));
                }
            }
        }

        return true;
    }

}
