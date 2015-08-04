package com.lepu.stethoscopic.fun.functiion.recorderData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.core.lib.application.BaseFragment;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.adapter.RecordAdapter;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.AudioManagerCustom;
import com.lepu.stethoscopic.model.SoundFile;
import com.lepu.stethoscopic.utils.Const;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guangdye on 2015/4/20.
 */
public class NotUploadListFragment extends BaseFragment {
    private ListView listView;
    private File[] files;
    private ArrayList<SoundFile> list = new ArrayList<SoundFile>();
    private RecordAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notupdata, null, false);
        listView = (ListView) view.findViewById(R.id.lstview);
        mAdapter = new RecordAdapter(getActivity(), list, getActivity().getSupportFragmentManager(), Const.NOT_UPLOAD);
        listView.setOnItemLongClickListener(mAdapter);
        listView.setAdapter(mAdapter);
        loadDb();

        return view;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        MainActivity mainActivity = (MainActivity) activity;
//        mainActivity.setDataListStateChange(new StateChange() {
//            @Override
//            public void onChange() {
//                loadDb();
//            }
//        });
//    }

    public void loadDb() {
        if (MyApplication.getInstance().getCurrentUser() == null) {
            return;
        }
        List<SoundFile> listTemp = AudioManagerCustom.getInstance().getAllAudioFiles(
                MyApplication.getInstance().getCurrentUser().getUserId(), Const.NOT_UPLOAD);
        if (listTemp != null && listTemp.size() != 0 && mAdapter != null) {
            list.clear();
            list.addAll(listTemp);
            mAdapter.notifyDataSetChanged();
        }
    }

//    private void updateData() {
//        files = null;
//        if (SDCardUtil.sdCardExists()) {
//            // File file = new File(SDCardUtil.DATA_DIRECTORY);
//            File file = new File(SdLocal.getYuYinFolder(getActivity()));
//            WavFileNameFilter filenameFilter = new WavFileNameFilter(".wav"); //wav
//            files = file.listFiles(filenameFilter);
//            if (files == null) {
//                return;
//            }
//            if (files.length == 0) {
//                return;
//            }
//            for (int i = 0, count = files.length; i < count; i++) {
//                //list.add(files[i]);
//            }
//            if (list.size() != 0) {
//                mAdapter.notifyDataSetChanged();
//            }
//        }
}
