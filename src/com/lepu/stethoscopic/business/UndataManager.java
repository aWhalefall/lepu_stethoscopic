package com.lepu.stethoscopic.business;

import com.core.lib.core.ApiClient;
import com.core.lib.core.AsyncRequest;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.model.SoundFile;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.AbsAction;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.HttpTask;
import com.lepu.stethoscopic.utils.Setting;

import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by weichyang on 2015/5/5.
 * 上传接口
 */
public class UndataManager {

    private UndataManager() {
    }

    public static UndataManager getInstance() {
        return UndataManagerHolder.undataManage;
    }


    static class UndataManagerHolder {
        static UndataManager undataManage = new UndataManager();
    }


    /**
     * 上传到录音文件
     *
     * @param request
     */
    public void updataVideoStream(AsyncRequest request, SoundFile sound) {
        try {
            User user = MyApplication.getInstance().getCurrentUser();
            if (user == null)
                return;
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("UserID", user.getUserId());
            params.put("LoginToken", user.getToken());
            params.put("SoundName", sound.SoundName);
            params.put("SoundTime", sound.Time);
            params.put("SoundType", sound.SoundType);
            params.put("Position", sound.Position);

            AbsAction absAction = new AbsAction();
            absAction.jsonString = params.toString();
            absAction.fileBody = new FileBody(new File(sound.Filepath));
            absAction.url = Setting.URL_API_HOST_HTTP + "sound/addSound";
            HttpTask httpTask = new HttpTask();
            httpTask.setActitons(absAction);
            httpTask.setAsyCallBack(request);
            httpTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private byte[] readFile(String fileName) throws IOException {
        byte[] buffer = null;
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 获取上传列表
     *
     * @param request
     */
    public void getVideoDataList(AsyncRequest request, String endTime, int pageIndex, int pageSize) {
        try {
            User user = MyApplication.getInstance().getCurrentUser();
            if (user == null)
                return;
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("UserID", user.getUserId());
            params.put("LoginToken", user.getToken());
            params.put("BeginTime", Const.STARTTIME);
            params.put("EndTime", endTime);
            params.put("PageIndex", pageIndex);
            params.put("PageSize", pageSize);
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "sound/getSoundList", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改客户端展示的名称
     *
     * @param request
     */
    public void renameCustomfileName(AsyncRequest request, int arid, String rename) {
        try {
            User user = MyApplication.getInstance().getCurrentUser();
            if (user == null)
                return;
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("UserID", user.getUserId());
            params.put("LoginToken", user.getToken());
            params.put("ARID", arid);
            params.put("SoundName", rename);
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "sound/updateSoundName", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除网络上的数据
     *
     * @param request
     */
    public void deleteCustomFile(AsyncRequest request, int arid) {
        try {
            User user = MyApplication.getInstance().getCurrentUser();
            if (user == null)
                return;
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject params = new JSONObject();
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("UserID", user.getUserId());
            params.put("LoginToken", user.getToken());
            params.put("ARID", arid);
            map.put("ht", params);
            ApiClient.post(Setting.URL_API_HOST_HTTP + "sound/deleteSound", map, null, request, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
