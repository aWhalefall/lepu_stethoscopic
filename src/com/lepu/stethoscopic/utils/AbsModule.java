package com.lepu.stethoscopic.utils;


import org.json.JSONObject;

public abstract class AbsModule {
    protected JSONObject backJson;
    protected JSONObject jsonObj;
    protected Object result;
    public String type, message, status;


    public abstract void parseData() throws Exception;

    public void setMyObject(Object result) {
        this.result = result;
    }

}
