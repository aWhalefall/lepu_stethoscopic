package com.lepu.stethoscopic.business;

import com.core.lib.core.ApiClient;
import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.secure.EncodeMD5;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.Setting;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guangdye on 2015/4/14.
 */
public class LoginManager {

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        return LoginManagerHolder.loginManager;
    }

    public void register(AsyncRequest request, String phone, String pwd, String code) {

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("LoginID", phone);
            params.put("Password", pwd);
            params.put("CAPTCHA", code);  //验证码
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "user/register", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(AsyncRequest request, String phone, String pwd) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("LoginID", phone);
            params.put("Password", pwd);
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "user/login", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCheckCode(AsyncRequest request, String phone) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("LoginID", phone);
            map.put("ht", params);

            ApiClient.post(Setting.URL_API_HOST_HTTP + "user/sendRegisterCAPTCHA", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCheckCodeForFindPwd(AsyncRequest request, String phone) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);

            params.put("LoginID", phone);
            map.put("ht", params);
            ApiClient.http_post(Setting.URL_API_HOST_HTTP + "user/sendResetPasswordCAPTCHA", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPassword(AsyncRequest request, String phone, String code, String pwd) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);

            params.put("LoginID", phone);
            params.put("CAPTCHA", code);
            params.put("Password", pwd);
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "user/ResetPassword", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPasswordByProtection(AsyncRequest request, String phone, String pwd, String q1Id, String q1, String q2Id, String q2) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();

            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);

            params.put("LoginID", phone);
            params.put("SecurityQuestion1", q1Id);
            params.put("SecurityAnswer1", EncodeMD5.getMd5(q1));
            params.put("SecurityQuestion2", q2Id);
            params.put("SecurityAnswer2", EncodeMD5.getMd5(q2));

            params.put("Password", EncodeMD5.getMd5(pwd));
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "user/resetPasswordBySecurityQuestion", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    pasmPostProfile  编辑个人信息
     */
//    public void updateProfileInfo(AsyncRequest request, User user) {
//        try {
//            Map<String, Object> map = new HashMap<String, Object>();
//            JSONObject params = new JSONObject();
//            params.put("DeviceID", Const.DEVICEID);
//
//            params.put("UserID", user.getUserId());
//            params.put("LoginToken", user.getToken());
//            params.put("TrueName", user.getTrueName());
//            params.put("Gender", user.getGender());
//            params.put("BirthYear", user.getBirthYear());
//            params.put("BirthMonth", user.getBirthMonth());
//            params.put("BirthDay", user.getBirthDay());
//            params.put("Height", user.getHeight());
//            params.put("Weight", user.getWeight());
//            params.put("CoronaryHeartDisease", user.isGuanxin ? 1 : 0);
//            params.put("Hypertension", user.isHypertension ? 1 : 0);
//            params.put("Nephroma", user.isNephroma ? 1 : 0);
//            params.put("Oculopathy", user.isEyeproblem ? 1 : 0);
//            params.put("FamilyGlycuresis", user.isSugarHistory ? 1 : 0);
//            map.put("ht", params);
//            ApiClient.http_post(Setting.URL_API_HOST_HTTP + "user/pasmPostProfile", map, null, request, "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void getSecurityQuestionList(AsyncRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            map.put("ht", params);
            ApiClient.http_post(Setting.URL_API_HOST_HTTP + "user/getSecurityQuestionList", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User parseUser(String data) {
        try {
            User user = new User();
            JSONObject dataInfo = new JSONObject((String) data);
            user.setUserId(dataInfo.optString("UserID"));
            user.setTrueName(dataInfo.optString("TrueName"));
            user.setNickName(dataInfo.optString("NickName"));
            user.setGender(dataInfo.optInt("Gender"));
            user.setPhone(dataInfo.optString("MobilePhone"));
            user.setToken(dataInfo.optString("LoginToken"));
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public String getUserJsonString(User user) {
//        JSONObject json = new JSONObject();
//        try {
//            json.put("UserID", user.getUserId());
//            json.put("TrueName", user.getTrueName());
//            json.put("NickName", user.getNickName());
//            json.put("Gender", user.getGender());
//            json.put("BirthYear", user.getBirthYear());
//            json.put("BirthMonth", user.getBirthMonth());
//            json.put("BirthDay", user.getBirthDay());
//            json.put("Height", user.getHeight());
//            json.put("Weight", user.getWeight());
//            json.put("MobilePhone", user.getPhone());
//            json.put("LoginToken", user.getToken());
//
//            json.put("CoronaryHeartDisease", user.isGuanxin ? 1 : 0);
//            json.put("Hypertension", user.isHypertension ? 1 : 0);
//            json.put("Nephroma", user.isNephroma ? 1 : 0);
//            json.put("Oculopathy", user.isEyeproblem ? 1 : 0);
//            json.put("FamilyGlycuresis", user.isSugarHistory ? 1 : 0);
//            return json.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 获取密保信息
     *
     * @param request
     */
    public void getSecurityQuestion(AsyncRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "user/getSecurityQuestionList", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取用户绑定密保问题列表
     *
     * @param request
     * @param phone
     */
    public void getUserSecurityQuestionList(AsyncRequest request, String phone) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("LoginID", phone);
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "user/getUserSecurityQuestionList", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class LoginManagerHolder {
        static LoginManager loginManager = new LoginManager();
    }

}
