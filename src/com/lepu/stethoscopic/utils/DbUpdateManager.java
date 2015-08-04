package com.lepu.stethoscopic.utils;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by weichyang on 2015/4/22.
 */
public class DbUpdateManager {

    public DbUpdateManager() {
        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        if (dbHelp == null)
            return;

    }
    /**
     * Upgrade tables. In this method, the sequence is:
     * <b>
     * <p>[1] Rename the specified table as a temporary table.
     * <p>[2] Create a new table which name is the specified name.
     * <p>[3] Insert data into the new created table, data from the temporary table.
     * <p>[4] Drop the temporary table.
     * </b>
     *
     * @param db        The database.
     * @param tableName The table name.
     * @param columns   The columns range, format is "ColA, ColB, ColC, ... ColN";
     */
    protected void upgradeTables(SQLiteDatabase db, String tableName, String columns) {
        try {
            db.beginTransaction();

            // 1, Rename table.
            String tempTableName = tableName + "_temp";
            String sql = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
            db.execSQL(sql);

            // 2, Create table. Load data
            onCreateTable(db);

            // 3, Load data
            sql = "INSERT INTO " + tableName +
                    " (" + columns + ") " +
                    " SELECT " + columns + " FROM " + tempTableName;


            db.execSQL(sql);

            // 4, Drop the temporary table.
            db.execSQL("DROP TABLE IF EXISTS " + tempTableName, null);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 创建新表
     *
     * @param db
     */
    private void onCreateTable(SQLiteDatabase db) {
        //todo
       //create table  if not exists a (id integer primary key autoincrement,code text not null,name text,username text);
    }

    /**
     * 创建表 备份表
     *
     * @param db
     * @param newTable
     * @param oldTable
     */
    private void onCreateTableOfCopy(SQLiteDatabase db, String newTable, String oldTable) {
        db.execSQL("CREATE TABLE " + newTable + " AS SELECT * FROM " + oldTable + "");

    }


    /**
     * 得到数据表的列名字集合
     *
     * @param db
     * @param tableName
     * @return
     */
    protected String[] getColumnNames(SQLiteDatabase db, String tableName) {
        String[] columnNames = null;
        Cursor c = null;

        try {
            c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (null != c) {
                int columnIndex = c.getColumnIndex("name");
                if (-1 == columnIndex) {
                    return null;
                }

                int index = 0;
                columnNames = new String[c.getCount()];
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    columnNames[index] = c.getString(columnIndex);
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(c);
        }

        return columnNames;
    }

    /**
     * @param c
     */
    private void closeCursor(Cursor c) {
        if (c != null) {
            c.moveToFirst();
            c.close();
        }
    }

    /*

    18-20
    19-20
    版本升级逻辑
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
{
    int upgradeVersion  = oldVersion;

    if (18 == upgradeVersion) {
        // Create table C
        String sql = "CREATE TABLE ...";
        db.execSQL(sql);
        upgradeVersion = 19;
    }

    if (19 == upgradeVersion) {
        // Modify table C
        upgradeVersion = 20;
    }

    if (upgradeVersion != newVersion) {
        // Drop tables
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        // Create tables
        onCreate(db);
    }
}
     */

}
