package com.lepu.stethoscopic.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guangdye on 2015/4/22.
 */
public class Medication {
    public int UMID;
    public int MedicineID;
    public String MedicineName;
    public String Producer;
    public int BGTID; //血糖类型
    public String Dosage;
    public String Unit = "粒"; //用量单位
    public int UseType; //todo 1：口服  2：注射 3：外用   4：含服

    public static ArrayList<Medication> parseTestJson(String dataString) {
        Gson gson = new Gson();
        ArrayList<Medication> testArraylist = gson.fromJson(dataString,
                new TypeToken<List<Medication>>() {
                }.getType());
        return testArraylist;
    }
}
