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
import com.lepu.stethoscopic.business.AudioManagerCustom;
import com.lepu.stethoscopic.business.UndataManager;
import com.lepu.stethoscopic.fun.functiion.recorderData.DiagnosisFragment;
import com.lepu.stethoscopic.fun.functiion.recorderData.PlayAudioFragment;
import com.lepu.stethoscopic.model.SoundFile;
import com.lepu.stethoscopic.utils.Const;
import com.lepu.stethoscopic.utils.SdLocal;

import java.io.File;
import java.util.ArrayList;

public class NetRecordAdapter extends BaseAdapter implements AdapterView.OnItemLongClickListener {
    private SoundFile file;
    private Context mContext;
    private ArrayList<SoundFile> mData;
    private LayoutInflater mInflater;
    private FragmentManager fragmentManager;
    private String renameValue;
    int type;
    private ProgressDialog progressDialog;

    public NetRecordAdapter(Context context, ArrayList<SoundFile> data,
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
        if (!file.Diagnose.isEmpty()) {
            hodler.txt_diagnosis.setVisibility(View.VISIBLE);
            hodler.textviewUpdata.setVisibility(View.INVISIBLE);
            hodler.txt_diagnosis.setTextColor(mContext.getResources().getColor(R.color.blue));
        } else {
            hodler.txt_diagnosis.setVisibility(View.VISIBLE);
            hodler.textviewUpdata.setVisibility(View.INVISIBLE);
            hodler.txt_diagnosis.setTextColor(mContext.getResources().getColor(R.color.txt_hint));
        }

        hodler.txt_diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!file.Diagnose.isEmpty()) {
                    DiagnosisFragment diagnosisFragment = new DiagnosisFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", file.Diagnose);
                    diagnosisFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.relative_content, diagnosisFragment).
                            addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                } else {
                    final MyDialog myDialog = new MyDialog(AppManager.getAppManager().currentActivity());
                    myDialog.setMessage("我们将在3~5个工作日给到专家诊断反馈结果，请耐心等待");
                    myDialog.setPositiveButton(R.string.app_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });
                    myDialog.create(null).show();
                }
            }
        });

        hodler.ic_soundType.setImageResource(file.SoundType == Const.HEART_SOUND ? R.drawable.ic_heart : R.drawable.ic_lung);
        hodler.imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayAudioFragment playAudioFragment = new PlayAudioFragment();
                Bundle bundle = new Bundle();
                bundle.putString("filepath", file.UrlFilePath);
                bundle.putString("filename", file.SoundName);
                bundle.putBoolean("isFromNet",true);
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
                        setCurrentFile(file);
                        progressDialog = new ProgressDialog(AppManager.getAppManager().currentActivity());
                        progressDialog.setMessage("正在删除音频文件...");
                        progressDialog.show();
                        //删除网络
                        UndataManager.getInstance().deleteCustomFile(asyncRequest, file.PRID);
                    }
                });
                dialog.create(null).show();
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
            progressDialog.dismiss();
            AppManager.getAppManager().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (file.Filepath != null) {
                        File fileTemp = new File(file.Filepath);
                        fileTemp.delete();
                        //删除数据库
                        AudioManagerCustom.getInstance().deleteAudioFile(file.UserId + "", file.Filepath);
                    }
                    //更新
                    mData.remove(file);
                    NetRecordAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void RequestError(Object object, int errorId, String errorMessage) {
            progressDialog.dismiss();
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
        setCurrentFile(file);
        dialog.addView(dialogView);
//        dialog.setTitleFill();
//        dialog.setTitleLineHidden();
        dialog.setNegativeButton(R.string.app_cancel, null);
        dialog.setPositiveButton(R.string.app_ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renameValue = editText.getText().toString().trim();
                if (!renameValue.equals("")) {
                    UndataManager.getInstance().renameCustomfileName(renameAsyncRequest, file.PRID, renameValue);
                } else {
                    UIHelper.showToast(mContext, R.string.notifyname);
                }
            }
        });
        dialog.create(null).show();
        return false;
    }

    private AsyncRequest renameAsyncRequest = new AsyncRequest() {
        @Override
        public void RequestComplete(Object object, Object data) {
            if (file.Filepath != null) {
                file.SoundName = renameValue;
                //修改本地文件名字
                File file1 = new File(file.Filepath);
                file1.renameTo(new File(SdLocal.getYuYinFolder(mContext) + "/" + renameValue));
                String path = SdLocal.getYuYinFolder(mContext) + "/" + renameValue;
                AudioManagerCustom.getInstance().renameSoundFile(String.valueOf(file.UserId), file.SoundName, renameValue, path);
            }
            //修改数据库文件
            NetRecordAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void RequestError(Object object, int errorId, String errorMessage) {
            progressDialog.dismiss();
            AppManager.getAppManager().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UIHelper.showToast(AppManager.getAppManager().currentActivity(), R.string.connectfail);
                }
            });
        }
    };

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
