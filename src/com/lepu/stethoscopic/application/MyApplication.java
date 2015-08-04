package com.lepu.stethoscopic.application;

import android.app.Application;

import com.lepu.stethoscopic.model.Medication;
import com.lepu.stethoscopic.model.User;

import org.json.JSONObject;

public class MyApplication extends Application {
    private static MyApplication instance;
    private User loginUser;
    private User currentUser;
    private boolean isLogin; //是否登录
    private JSONObject version;
    private Medication medication;

    public static MyApplication getInstance() {
        return instance;
    }

    public static void setInstance(MyApplication instance) {
        MyApplication.instance = instance;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    /*
     * 每次启动这个独立进程的时候，都会初始化一个Application对象，就调用了一次oncreate方法。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public User getUser() {
        return loginUser;
    }

    public void setUser(User user) {
        this.loginUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public JSONObject getVersion() {
        return version;
    }

    public void setVersion(JSONObject version) {
        this.version = version;
    }
}
