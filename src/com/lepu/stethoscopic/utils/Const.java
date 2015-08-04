package com.lepu.stethoscopic.utils;

public class Const {

    /**
     * 用户配置常量
     */
    public static final String PACKAGE_NAME = "com.lepu.stethoscopic";
    public static final String USER_INFO = "USER_INFO";

    public static final String CONFIG_APP_FIRST_USE = "appFirstUse";
    public static final String CONFIG_APP_UPGRADE_USE = "appUpgradeUse";

    public static final String CONFIG_APP_EXCEPTION_IF = "appExceptionIf";
    public static final String CONFIG_APP_EXCEPTION_VALUE = "appExceptionValue";


    public static final int DEVICEID = 2;// android
    public static final int APPLICATIONID = 12;// 听诊器
    public static final int MAN = 2;
    public static final int WOMEN = 1;


    /*
     * gls test
     */
    public static final String CONFIG_GLS_TEST_FM_COUNT_SUCCESS = "configGlsTestFmCountSuccess";
    public static final String CONFIG_GLS_TEST_FM_COUNT_FAIL = "configGlsTestFmCountFail";

    public static final String[] SPORTYPETIPS = {"(散步,太极拳，做体操)",
            "(羽毛球,乒乓球,健美操)", "(自行车,爬楼,跳绳)",

    };
    public static final String STARTTIME = "2015-01-01 00:00:00";
    public static final int PAGESIZE = 50;
    public static int Local_error = -1;

    public static class UserInfo {
        public static final String UserID = "UserID";
        public static final String TrueName = "TrueName";
        public static final String NickName = "NickName";
        public static final String Gender = "Gender";
        public static final String BirthYear = "BirthYear";
        public static final String BirthMonth = "BirthMonth";
        public static final String BirthDay = "BirthDay";
        public static final String Height = "Height";
        public static final String Weight = "Weight";

        public static final String MobilePhone = "MobilePhone";
        public static final String LoginToken = "LoginToken";

        public static final String CoronaryHeartDisease = "CoronaryHeartDisease";
        public static final String Hypertension = "Hypertension";
        public static final String Nephroma = "Nephroma";
        public static final String Oculopathy = "Oculopathy";
        public static final String FamilyGlycuresis = "FamilyGlycuresis";
    }


    public static final int HEART_SOUND = 1;  //心音1 肺音2
    public static final int PULMONARY_SOUND = 2;

    public static final int UPLOADED = 1;  //上传1 未上传0
    public static final int NOT_UPLOAD = 0;

}
