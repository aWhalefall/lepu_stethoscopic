package com.lepu.stethoscopic.utils;

/*
 * 放网络请求的url地址，每个接口需要加上注释
 */
public class Setting {

    private static final boolean DEBUG = false;
    // private static final String HOST_TEST = "192.168.21.18:8080";
    private static final String HOST_TEST = "apitest.tzq.ixinzang.com";
    private static final String HOST_ONLINE = "api.tzq.test.ixinzang.com";
    //todo 先用爱心脏的测试
    //private static final String HOST_ONLINE = "apitest.ixinzang.com";
    //private static final String HOST_ONLINE = "apitest.tzq.ixinzang.com";

    private static final String HOST_TEST_IMG = "img.test.ixinzang.com";
    private static final String HOST_ONLINE_IMG = "img.test.ixinzang.com";

    private final static String HOST = DEBUG ? HOST_TEST : HOST_ONLINE;
    private final static String HOST_IMG = DEBUG ? HOST_TEST_IMG:HOST_ONLINE_IMG;


    private final static String HTTP = "http://";
    private final static String HTTPS = "https://";
    private final static String SPLIT = "/";

    // ==========================================================================================
    public final static String URL_API_HOST_HTTP = HTTP + HOST + SPLIT;
    public final static String URL_API_HOST_HTTPS = HTTPS + HOST + SPLIT;
    public final static String URL_API_HOST_HTTP_IMG = HTTP + HOST_IMG + SPLIT;
    public final static String URL_API_HOST_HTTPS_IMG = HTTPS + HOST_IMG
            + SPLIT;
    /*
     * test 接口
     */
    public static String getTestJsonUrl(int page) {
        return String
                .format("api.shidaiyinuo.com?api=0&app=nursetf&method=getHospitalList&checkcode=abc&from=1&page=%d",
                        page);
    }

    public static String getTestUrl(int test) {
        return "http://www.baidu.com";
    }

    public static String getTestXmlUrl() {
        return "";
    }

    public static String postTestJsonUrl() {
        return "";
    }

    // =================================================================================
    /*
     * get请求url
	 */
    public static String getSampleUrl(String uid) {
        return String.format("%s?uid=%s", URL_API_HOST_HTTP, uid);
    }

    /*
     * post请求url
     */
    public static String postSampleUrl() {
        return "";
    }

    /*
     * 获取异常日志上传接口
     */
    public static String getUploadLogUrl() {
        return "";
    }

    /*
     * 获取检查更新接口
     */
    public static String getCheckUpdate() {
        return URL_API_HOST_HTTP + "system/checkUpdate";
    }

    /*
     * 获取检查更新接口
     */
    public static String getBloodUpdate() {
        return URL_API_HOST_HTTP + "bloodGlucose/submitBloodGlucose";
    }

    /*
     * 获取上一次同步时间
     */
    public static String getBloodLastSyncTime() {
        return URL_API_HOST_HTTP + "bloodGlucose/getFirstBloodDate";
    }

    /*
     * 获取上个月血糖数据
     */
    public static String getBloodSyncData() {
        return URL_API_HOST_HTTP + "bloodGlucose/bgSyncByMonthList";
    }
}
