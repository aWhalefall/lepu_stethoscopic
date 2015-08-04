package com.lepu.stethoscopic.fun.functiion.recorderData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.core.lib.application.BaseFragment;
import com.core.lib.core.AsyncRequest;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.adapter.NetRecordAdapter;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.AudioManagerCustom;
import com.lepu.stethoscopic.business.UndataManager;
import com.lepu.stethoscopic.model.SoundFile;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.DateUtil;
import com.lepu.stethoscopic.utils.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by guangdye on 2015/4/20.
 */
public class UploadedListFragment extends BaseFragment {


    private ListView listView;
    private ArrayList<SoundFile> list = new ArrayList<SoundFile>();
    private NetRecordAdapter mAdapter;
    private int pageIndex = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String engTime = DateUtil.getData2str(new Date());
//        UndataManager.getInstance().getVideoDataList(asyncRequest, engTime, pageIndex, Const.PAGESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notupdata, null, false);
        listView = (ListView) view.findViewById(R.id.lstview);
        mAdapter = new NetRecordAdapter(getActivity(), list, getActivity().getSupportFragmentManager(), Const.UPLOADED);
        listView.setOnItemLongClickListener(mAdapter);
        listView.setAdapter(mAdapter);
        String engTime = DateUtil.getData2str(new Date());
        UndataManager.getInstance().getVideoDataList(asyncRequest, engTime, pageIndex, Const.PAGESIZE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String engTime = DateUtil.getData2str(new Date());
        UndataManager.getInstance().getVideoDataList(asyncRequest, engTime, pageIndex, Const.PAGESIZE);
//        if (MyApplication.getInstance().getCurrentUser() == null) {
//            return;
//        }
//        List<SoundFile> listTemp = AudioManager.getInstance().getAllAudioFiles(
//                MyApplication.getInstance().getCurrentUser().getUserId(), Const.UPLOADED);
//        if (listTemp != null && listTemp.size() != 0 && mAdapter != null) {
//            list.clear();
//            list.addAll(listTemp);
//            mAdapter.notifyDataSetChanged();
//        }
//        String engTime = DateUtil.getData2str(new Date());
//        UndataManager.getInstance().getVideoDataList(asyncRequest, engTime, pageIndex, Const.PAGESIZE);
    }

    //网络回调
    private AsyncRequest asyncRequest = new AsyncRequest() {
        @Override
        public void RequestComplete(Object object, Object data) {
            User user = MyApplication.getInstance().getCurrentUser();
            if (user == null)
                return;
            ArrayList<SoundFile> soundLists = new ArrayList<SoundFile>();
            JSONArray jsonArray = ((JSONObject) data).optJSONArray("ListInfo");
            for (int i = 0, count = jsonArray.length(); i < count; i++) {
                SoundFile soundList = new SoundFile();
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    soundList.SoundType = jsonObject.optInt("SoundType");
                    soundList.UrlFilePath = Setting.URL_API_HOST_HTTP_IMG+jsonObject.optString("Sound");
                    soundList.Diagnose = jsonObject.optString("Diagnose");
                    soundList.SoundName = jsonObject.optString("SoundName");
                    soundList.PRID = jsonObject.optInt("ARID");
                    soundList.UploadState = Const.UPLOADED;
                    soundList.IsSee = jsonObject.optInt("IsSee");
                    soundList.UserId = Integer.valueOf(user.getUserId());
                    soundLists.add(soundList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (soundLists.size() == 0)
                return;
            //是否有值
            for (int i = 0, cou = soundLists.size(); i < cou; i++) {
                if (AudioManagerCustom.getInstance().isExists(user.getUserId(), soundLists.get(i).SoundName)) {
                	AudioManagerCustom.getInstance().updataServiceBack(soundLists.get(i));
                } else {
                	AudioManagerCustom.getInstance().replaceAudioFile(soundLists.get(i));
                }
            }
            //数据解析完成
            list.clear();
            list.addAll(soundLists);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void RequestError(Object object, int errorId, String errorMessage) {

        }
    };


}
