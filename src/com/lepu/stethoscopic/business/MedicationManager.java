package com.lepu.stethoscopic.business;

import com.core.lib.core.ApiClient;
import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.UIHelper;
import com.lepu.stethoscopic.model.Medication;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.Setting;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guangdye on 2015/4/24.
 */
public class MedicationManager {
    private MedicationManager() {
    }

    public static MedicationManager getInstance() {
        return MedicationManagerHolder.medicationManager;
    }

    public void submit(Medication medication, User user, int opFlag, AsyncRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);

            params.put("UserID", user.getUserId());
            params.put("LoginToken", user.getToken());

            params.put("MedicineID", medication.MedicineID);
            params.put("UseType", medication.UseType);
            params.put("BGTID", medication.BGTID);
            params.put("Dosage", medication.Dosage);
            params.put("Unit", medication.Unit);
            params.put("OpFlag", opFlag);//1 新增 2 修改

            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "medicine/submitUserMedicine", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Medication medication, User user, AsyncRequest request) {
        try {
            if (user == null) {
                UIHelper.showToast(AppManager.getAppManager().currentActivity(), "请先登录");
                return;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);

            params.put("UserID", user.getUserId());
            params.put("LoginToken", user.getToken());

            params.put("UMID", medication.UMID);//用户用药ID

            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "medicine/deleteUserMedicine", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData(AsyncRequest request, User user) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("UserID", user.getUserId());
            params.put("LoginToken", user.getToken());
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "medicine/getUserMedicineList", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MedicationManagerHolder {
        static MedicationManager medicationManager = new MedicationManager();
    }

}
