package com.lepu.stethoscopic.model;

/**
 * Created by weichyang on 2015/4/14.
 * 健康日志bean
 */
public class HealthLog {
    public String userId;  //用户id
    public String data; //日期
    public String updata; //日期
    public int ADI;   //摄入量
    public int consumption; //消耗量
    public String fastingBloodGlucose; //今日空腹血糖
    public int  insulin; //胰岛素
    public int  hypotensor; //降压药
    public int bloodPressure;  //血压
    public int systolicPressure; //收缩压

    //详细信息
    public int stapleFood;  //主食
    public int greenStuff;//蔬菜
    public int fruit;//水果
    public int meat;
    public int milk;

    public int qrecordStep;
    public int mrecordStep;
    public int hrecordStep;
    public String  bodyWeight;
    public String  heartRate;
    public String synchronizationTime; //同步时间


}
