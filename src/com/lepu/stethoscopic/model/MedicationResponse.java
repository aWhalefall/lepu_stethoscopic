package com.lepu.stethoscopic.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guangdye on 2015/4/22.
 */
public class MedicationResponse {
    public int UMID;
    public int MedicineID;
    public String MedicineName = "哈药";
    public String Producer;
    public int BGTID = 2; //血糖类型
    public String Dosage = "5";
    public String Unit = "粒"; //用量单位
    public int UseType; //1：口服  2：注射 3：外用   4：含服

    public static ArrayList<MedicationResponse> parseTestJson(String dataString) {
        Gson gson = new Gson();
        ArrayList<MedicationResponse> testArraylist = gson.fromJson(dataString,
                new TypeToken<List<MedicationResponse>>() {
                }.getType());
        return testArraylist;
    }
}
