package com.lepu.stethoscopic.business;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lepu.stethoscopic.model.SoundFile;
import com.lepu.stethoscopic.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weichyang on 2015/5/4.
 */
public class AudioManagerCustom {
    private AudioManagerCustom audioManager;

//    /**
//     * 插入数据库 audio list
//     *
//     * @param soundList
//     */
//    public void insertAudioList(SoundList soundList) {
//
//        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
//        if (dbHelp == null)
//            return;
//        SQLiteDatabase db = dbHelp.getWritableDatabase();
//        String sql = "REPLACE into audioList (" +
//                "UserId,SoundName," +
//                "SoundType,PRID," +
//                "IsSee,Diagnose,UrlFilePath) values(" +
//                "?,?,?,?,?,?,?)";
//        db.execSQL(sql, new Object[]{
//                soundList.UserId, soundList.SoundName, soundList.SoundType
//                , soundList.PRID, soundList.IsSee, soundList.Diagnose, soundList.UrlFilePath});
//        db.close();
//    }

    static class AudioManagerHolder {
        static AudioManagerCustom mediaManager = new AudioManagerCustom();
    }

    public static AudioManagerCustom getInstance() {
        return AudioManagerHolder.mediaManager;
    }

    private AudioManagerCustom() {
        this.audioManager = AudioManagerCustom.getInstance();
    }

    /**
     * 更新audioFile
     */
    public void replaceAudioFile(SoundFile soundFile) {
        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        if (dbHelp == null)
            return;
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        String sql = "REPLACE into audio (" +
                "UserId,Time,SoundName," +
                "SoundType,Filepath,Position," +
                "UploadState) values(" +
                "?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{soundFile.UserId, soundFile.Time, soundFile.SoundName
                , soundFile.SoundType, soundFile.Filepath, soundFile.Position
                , soundFile.UploadState});

        db.close();
    }

    /**
     * 删除指定的文件
     *
     * @param userId
     * @param filePath
     */
    public void deleteAudioFile(String userId, String filePath) {
        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        if (dbHelp == null)
            return;
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        String sql = "delete  from audio where UserId=? and Filepath=?";
        db.execSQL(sql, new Object[]{userId, filePath});

        db.close();
    }

    /**
     * 更新文件名字
     *
     * @param userId
     * @param oldName
     * @param newName
     */
    public void renameSoundFile(String userId, String oldName, String newName, String path) {
        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        if (dbHelp == null)
            return;
        SQLiteDatabase db = dbHelp.getWritableDatabase();
//        String sql = "UPDATE audio set SoundName=? , Filepath=? where UserId=? and SoundName=?";
        String sql = "UPDATE audio set SoundName=?  where UserId=? and SoundName=?";
        db.execSQL(sql, new Object[]{newName,  userId, oldName});

        db.close();
    }

    public List<SoundFile> getAllAudioFiles(String userId, int isupdata) {
        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        if (dbHelp == null)
            return null;
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        String sql = "SELECT * from audio where UserId=? and UploadState=? ORDER BY Time DESC";
        Cursor queryResult = db.rawQuery(sql, new String[]{userId, String.valueOf(isupdata)});
        if (queryResult == null || queryResult.getCount() == 0) {
            return null;
        }
        ArrayList<SoundFile> soundFiles = new ArrayList<SoundFile>();
        cursorParse(soundFiles, queryResult);
        queryResult.close();
        dbHelp.close();
        return soundFiles;
    }

    /**
     * 解析
     *
     * @param soundFiles
     */
    private void cursorParse(ArrayList<SoundFile> soundFiles, Cursor cursor) {
        while (cursor.moveToNext()) {
            SoundFile soundFile = new SoundFile();
            soundFile.UserId = cursor.getInt(cursor.getColumnIndex("UserId"));
            soundFile.Time = cursor.getString(cursor.getColumnIndex("Time"));
            soundFile.Filepath = cursor.getString(cursor.getColumnIndex("Filepath"));
            soundFile.SoundName = cursor.getString(cursor.getColumnIndex("SoundName"));
            soundFile.SoundType = cursor.getInt(cursor.getColumnIndex("SoundType"));
            soundFile.Position = cursor.getInt(cursor.getColumnIndex("Position"));
            soundFile.UploadState = cursor.getInt(cursor.getColumnIndex("UploadState"));
            soundFile.PRID = cursor.getInt(cursor.getColumnIndex("PRID"));
            soundFile.UrlFilePath = cursor.getString(cursor.getColumnIndex("UrlFilePath"));
            soundFile.IsSee = cursor.getInt(cursor.getColumnIndex("IsSee"));
            soundFiles.add(soundFile);
        }
    }

    /**
     * 插入服务器返回的数据
     */
    public void insertServiceBack(String userId, String arid, String urlpath, String soundName) {

        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        if (dbHelp == null)
            return;
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        String sql = "UPDATE audio set PRID =?,UrlFilePath=?,UploadState=1 where UserId=? and SoundName=?";
        db.execSQL(sql, new String[]{arid, urlpath, userId, soundName});
        dbHelp.close();
        db.close();
    }


    /**
     * 跟心服务器返回的数据
     */
    public void updataServiceBack(SoundFile soundFile) {
        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        if (dbHelp == null)
            return;
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        String sql = "UPDATE audio set PRID =?,UrlFilePath=?,UploadState=1,Diagnose=?,IsSee=? where UserId=? and SoundName=?";
        db.execSQL(sql, new Object[]{soundFile.PRID, soundFile.UrlFilePath, soundFile.Diagnose, soundFile.IsSee, soundFile.UserId, soundFile.SoundName});
        dbHelp.close();
        db.close();
    }


    public boolean isExists(String userId, String soundName) {
        DBHelper dbHelp = DBHelper.getInstance(DBHelper.DATABASE_NAME_MAIN);
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        String sql = "SELECT * from audio where UserId=? and SoundName=?";
        Cursor queryResult = db.rawQuery(sql, new String[]{userId, soundName});
        if (queryResult == null || queryResult.getCount() == 0) {
            return false;
        }
        if (queryResult.moveToNext()) {
            return true;
        }
        return false;
    }

}


