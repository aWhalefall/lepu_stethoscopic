package com.lepu.stethoscopic.model;

import com.lepu.stethoscopic.utils.Const;

/**
 * Created by weichyang on 2015/5/4.
 * 录音文件表
 */
public class SoundFile {
    public int FileId;  //todo
    public int UserId; //用户名
    public String Time; //上传时间

    public String Filepath; //文件路径
    public String SoundName; //文件名
    public int SoundType = Const.HEART_SOUND; //todo 声音类型

    public int Position = 1; //todo 位置
    public int UploadState = Const.NOT_UPLOAD; //todo 上传状态
    public int PRID;
    public String UrlFilePath;
    public int IsSee;
    public String Diagnose;

}
