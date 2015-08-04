package com.lepu.stethoscopic.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;

import com.core.lib.application.BaseActivity;
import com.core.lib.application.BaseFragmentActivity;
import com.core.lib.core.ApiClient;
import com.core.lib.core.AsyncRequest;
import com.core.lib.core.HttpRequest;
import com.core.lib.core.HttpRequest.OnDownloadProgress;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.StringUtilBase;
import com.core.lib.utils.main.UIHelper;
import com.core.lib.utils.main.UtilityBase;
import com.core.lib.widget.MyDialog;
import com.lepu.stethoscopic.R;
import com.lepu.stethoscopic.main.activity.MainActivity;
import com.lepu.stethoscopic.main.bean.BeanCheckVersion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuxd 通用检查更新的类
 */
public class UpdateManager implements AsyncRequest {

    public static final int REQUEST_CODE = 1000;

    private static final String REQUEST_CHECK_UPDATE = "request_check_update";

    private static final int MSG_CHECK_SUCCESS = 10;
    private static final int MSG_CHECK_FAIL = 11;

    private static UpdateManager mUpdateManager = null;
    private Context mCurrentContext = null;

    private boolean mNeedProgress = false;

    private NotificationManager mNotificationManager = null;
    private Notification mNotification = null;
    private boolean mIsDownload = false;

    private UpdateManager() {
    }

    public static UpdateManager getInstance() {
        if (mUpdateManager == null) {
            mUpdateManager = new UpdateManager();
        }

        return mUpdateManager;
    }

    public void checkUpdate(String url, Context context, boolean needProgress) {

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        mNeedProgress = needProgress;
        mCurrentContext = context;

        if (mNeedProgress) {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showProgressDialog();
            } else if (context instanceof BaseFragmentActivity) {
                ((BaseFragmentActivity) context).showProgressDialog();
            }
        }

        String version = "";
        try {
            version = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        checkUpdate(url, version);
    }

    private void checkUpdate(String url, String version) {

        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject params = new JSONObject();
        try {
            params.put("DeviceID", Const.DEVICEID);
            params.put("ApplicationID", Const.APPLICATIONID);
            params.put("ClientVersion", version);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("ht", params);

        ApiClient.http_post(Setting.getCheckUpdate(), map, null, this,
                REQUEST_CHECK_UPDATE);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHECK_SUCCESS:

                    if (mNeedProgress) {
                        if (mCurrentContext instanceof BaseActivity) {
                            ((BaseActivity) mCurrentContext).hideProgressDialog();
                        } else if (mCurrentContext instanceof BaseFragmentActivity) {
                            ((BaseFragmentActivity) mCurrentContext).hideProgressDialog();
                        }
                    }

                    BeanCheckVersion beanCheckVersion = (BeanCheckVersion) msg.obj;
                    if (beanCheckVersion != null && beanCheckVersion.getClientUpgrade()) {
                        showUpdateVersionDialog(beanCheckVersion);
                    } else {
                        if (mNeedProgress) {
                            UIHelper.showToast(mCurrentContext,
                                    R.string.ok);
                        }
                    }

                    //test
                    showUpdateVersionDialog(beanCheckVersion);

                    break;
                case MSG_CHECK_FAIL:

                    if (mNeedProgress) {
                        if (mCurrentContext instanceof BaseActivity) {
                            ((BaseActivity) mCurrentContext).hideProgressDialog();
                        } else if (mCurrentContext instanceof BaseFragmentActivity) {
                            ((BaseFragmentActivity) mCurrentContext).hideProgressDialog();
                        }

                        UIHelper.showToast(mCurrentContext, R.string.ok);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    // update app,download
    public void showUpdateVersionDialog(final BeanCheckVersion beanCheckVersion) {
        MyDialog dialog = new MyDialog(AppManager.getAppManager()
                .currentActivity())
                .setTitle(R.string.app_tip)
                .setMessage(R.string.ok)
                .setNegativeButton(R.string.ok,
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }
                        })
                .setPositiveButton(R.string.ok,
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if (mIsDownload) {
                                    return;
                                }

                                String filePath = beanCheckVersion
                                        .getClientDownloadUrl();
                                if (Utils.DEBUG) {
                                    filePath = "http://www.shidaiyinuo.com/app/yn_nurse_enuo.apk";
                                }
                                if (!StringUtilBase.stringIsEmpty(filePath)) {
                                    Intent openIntent = new Intent(mCurrentContext, MainActivity.class);
                                    showNotification(R.drawable.app_icon,
                                            R.string.app_name, openIntent);
                                    showDownloadFileDialog(filePath);
                                }
                            }
                        });
        dialog.create(null).show();
    }

    /*
     * 下载程序
     */
    public void showDownloadFileDialog(String downloadUrl) {

        // download
        String filePath = SdLocal.getDownloadApkPath(mCurrentContext,
                mCurrentContext.getString(R.string.app_name));

        HttpRequest.downloadFile(downloadUrl, filePath,
                new OnDownloadProgress() {

                    @Override
                    public void onProgressChanged(int progress) {
                        Message msg = new Message();
                        msg.what = MSG_PROGRESS_CHANGED;
                        msg.arg1 = progress;
                        mProgressHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Exception e) {
                        mProgressHandler.sendEmptyMessage(MSG_PROGRESS_ERROR);
                    }
                });
    }

    public static final int MSG_PROGRESS_CHANGED = 100;
    public static final int MSG_PROGRESS_ERROR = 101;

    @SuppressLint("HandlerLeak")
    private Handler mProgressHandler = new Handler() {

        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_PROGRESS_CHANGED:
                    int progress = msg.arg1;

                    mIsDownload = true;
                    mNotification.contentView.setProgressBar(R.id.progressBar,
                            100, progress, false);
                    mNotification.contentView.setTextViewText(
                            R.id.notification_textview, progress + "%");
                    mNotificationManager
                            .notify(R.string.app_name, mNotification);

                    if (progress == 100) {

                        mIsDownload = false;
                        UtilityBase
                                .installApk(
                                        mCurrentContext,
                                        SdLocal.getDownloadApkPath(
                                                mCurrentContext,
                                                mCurrentContext
                                                        .getString(R.string.app_name)));
                        // 删除通知
                        if (mNotificationManager != null) {
                            mNotificationManager.cancel(R.string.app_name);
                        }
                    }

                    break;
                case MSG_PROGRESS_ERROR:
                    UIHelper.showToast(mCurrentContext,
                            R.string.ok);
                    break;
                default:
                    break;
            }

        }

        ;
    };

    @SuppressWarnings("deprecation")
    public void showNotification(int iconId, int notifyId, Intent openIntent) {

        long when = System.currentTimeMillis();

        mNotification = new Notification(iconId, "更新 ", when);
        mNotification.defaults = Notification.DEFAULT_SOUND;// 发出默认声音

        RemoteViews rv = new RemoteViews(Const.PACKAGE_NAME,
                R.layout.main_update_notification);
        rv.setProgressBar(R.id.progressBar, 100, 0, false);
        rv.setImageViewResource(R.id.ImageView, R.drawable.app_icon);
        rv.setTextViewText(R.id.notification_textview, "0%");
        PendingIntent contentIntent = PendingIntent.getActivity(mCurrentContext,
                REQUEST_CODE, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);// 当点击消息时就会向系统发送openintent意图

        mNotification.contentView = rv;
        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(notifyId, mNotification);// 第一个参数为自定义的通知唯一标识
    }

    @Override
    public void RequestComplete(Object object, Object data) {
        if (object.equals(REQUEST_CHECK_UPDATE)) {

            BeanCheckVersion checkVersion = BeanCheckVersion
                    .parseCheckVersion((String) data);

            Message msg = mHandler.obtainMessage();
            msg.what = MSG_CHECK_SUCCESS;
            msg.obj = checkVersion;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void RequestError(Object object, int errorId, String errorMessage) {
        if (object.equals(REQUEST_CHECK_UPDATE)) {
            mHandler.sendEmptyMessage(MSG_CHECK_FAIL);
        }
    }

}
