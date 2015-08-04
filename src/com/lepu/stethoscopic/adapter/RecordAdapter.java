package com.lepu.stethoscopic.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.lib.core.AsyncRequest;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.UIHelper;
import com.core.lib.widget.MyDialog;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.application.MyApplication;
import com.lepu.stethoscopic.business.AudioManagerCustom;
import com.lepu.stethoscopic.business.UndataManager;
import com.lepu.stethoscopic.fun.functiion.recorderData.PlayAudioFragment;
import com.lepu.stethoscopic.model.SoundFile;
import com.lepu.stethoscopic.model.User;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.SdLocal;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class RecordAdapter extends BaseAdapter implements AdapterView.OnItemLongClickListener {
    private SoundFile file;
    private Context mContext;
    //private File[] mData;
    private ArrayList<SoundFile> mData;
    private LayoutInflater mInflater;
    private FragmentManager fragmentManager;
    int type;
    private ProgressDialog dialog;

    public RecordAdapter(Context context, ArrayList<SoundFile> data,
                         FragmentManager supportFragmentManager, int type) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(this.mContext);
        this.fragmentManager = supportFragmentManager;
        this.type = type;
    }


    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHodler hodler = null;
        if (view == null) {
            hodler = new ViewHodler();
            view = mInflater.inflate(R.layout.item_updata, null);
            hodler.textViewfileName = (TextView) view.findViewById(R.id.fileName);
            hodler.textViewCreatetime = (TextView) view.findViewById(R.id.createtime);
            hodler.imageViewPlay = (ImageView) view.findViewById(R.id.imgview_play);
            hodler.textviewDelete = (TextView) view.findViewById(R.id.textview_delete);
            hodler.textviewUpdata = (TextView) view.findViewById(R.id.textview_updata);
            hodler.txt_diagnosis = (TextView) view.findViewById(R.id.txt_diagnosis);
            hodler.ic_soundType = (ImageView) view.findViewById(R.id.ic_soundType);
            view.setTag(hodler);
        } else {
            hodler = (ViewHodler) view.getTag();
        }

        final SoundFile file = mData.get(position);

        hodler.textViewfileName.setText(file.SoundName);
        hodler.textViewCreatetime.setText(file.Time); //todo
        hodler.txt_diagnosis.setVisibility(View.INVISIBLE);

        hodler.ic_soundType.setImageResource(file.SoundType == Const.HEART_SOUND ? R.drawable.ic_heart : R.drawable.ic_lung);

        hodler.imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayAudioFragment playAudioFragment = new PlayAudioFragment();
                Bundle bundle = new Bundle();
                bundle.putString("filepath", file.Filepath);
                bundle.putString("filename", file.SoundName);
                playAudioFragment.setArguments(bundle);
                fragmentManager.beginTransaction().add(R.id.relative_content, playAudioFragment).
                        addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                //play(file.getAbsolutePath());
            }
        });
        hodler.textviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除本地文件
                MyDialog dialog = new MyDialog(AppManager.getAppManager().currentActivity());
                dialog.setMessage(R.string.isconfirm);
                dialog.setNegativeButton(R.string.app_cancel, null);
                dialog.setPositiveButton(R.string.app_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //file.delete(); //todo
                        File fileTemp = new File(file.Filepath);
                        fileTemp.delete();
                        AudioManagerCustom.getInstance().deleteAudioFile(file.UserId + "", file.Filepath);
                        mData.remove(position);
                        RecordAdapter.this.notifyDataSetChanged();
                    }
                });
                dialog.create(null).show();
            }
        });

        hodler.textviewUpdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行上传协议
                dialog = new ProgressDialog(AppManager.getAppManager().currentActivity());
                dialog.setMessage("正在上传音频文件...");
                dialog.show();
                setCurrentFile(file);
                UndataManager.getInstance().updataVideoStream(asyncRequest, file);
            }
        });

        return view;
    }

    private void setCurrentFile(SoundFile file) {
        this.file = file;
    }

    private AsyncRequest asyncRequest = new AsyncRequest() {
        @Override
        public void RequestComplete(Object object, Object data) {
            /*
               1.修改数据库的状态 上传状态
               2.插入raid
               3.重新加载出来
             */
            JSONObject jsonObject = (JSONObject) object;
            String arid = jsonObject.optJSONObject("DetailInfo").optString("ARID");
            String sound = jsonObject.optJSONObject("DetailInfo").optString("Sound");
            String soundName = jsonObject.optJSONObject("DetailInfo").optString("SoundName");
            User user = MyApplication.getInstance().getCurrentUser();
            AudioManagerCustom.getInstance().insertServiceBack(user.getUserId(), arid, sound, soundName);
            if (dialog != null)
                dialog.dismiss();
            AppManager.getAppManager().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //刷新adapter 移除上传数据
                    if (mData.size() > 0) {
                        mData.remove(file);
                        RecordAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public void RequestError(Object object, int errorId, String errorMessage) {
            dialog.dismiss();
            AppManager.getAppManager().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UIHelper.showToast(AppManager.getAppManager().currentActivity(), R.string.connectfail);
                }
            });

        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        MyDialog dialog = new MyDialog(AppManager.getAppManager().currentActivity());
        View dialogView = View.inflate(mContext, R.layout.view_dialog_updata, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edite_content);
        final SoundFile file = mData.get(position);
        editText.setText(file.SoundName);

        dialog.addView(dialogView);
        dialog.setTitleFill();
        dialog.setTitleLineHidden();
        dialog.setNegativeButton(R.string.app_cancel, null);
        dialog.setPositiveButton(R.string.app_ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String renameValue = editText.getText().toString().trim();
                if (!renameValue.equals("")) {
                    //修改数据库文件
                    // file.Filepath = SdLocal.getYuYinFolder(mContext) + "/" + renameValue;
                    //File file1 = new File(file.Filepath);
                    //修改本地文件
                    // file1.renameTo(new File(SdLocal.getYuYinFolder(mContext) + "/" + renameValue));
                	AudioManagerCustom.getInstance().renameSoundFile(String.valueOf(file.UserId), file.SoundName,
                            renameValue, SdLocal.getYuYinFolder(mContext) + "/" + renameValue);
                    file.SoundName = renameValue;
                    RecordAdapter.this.notifyDataSetChanged();
                } else {
                    UIHelper.showToast(mContext, "修改名字不能为空");
                }
            }
        });
        dialog.create(null).show();
        return false;
    }
    static class ViewHodler {
        TextView txt_diagnosis;
        ImageView ic_soundType;
        TextView textViewfileName;
        TextView textViewCreatetime;
        TextView textviewDelete; //删除
        TextView textviewUpdata; //上传
        ImageView imageViewPlay;
    }

}
