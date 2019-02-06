package org.hvdw.fytfunctionalityextender;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.support.v4.app.JobIntentService;

public class MyJobService extends JobIntentService {
    public static final String TAG = "FFE-MyJobService";
    private boolean use_root_access;
    private boolean switch_wifi_on;
    private boolean restart_player;
    private String packagename_call;
    private String intent_call;
    private String sys_call;
    private Context mContext;

    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {

        Log.d(TAG,"FFE jobservice onHandleWork starts");
        Context mContext = MyJobService.this;

        use_root_access = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(MySettings.USE_ROOT_ACCESS, true);
        switch_wifi_on = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(MySettings.SWITCH_WIFI_ON, true);
        restart_player = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(MySettings.RESTART_PLAYER, true);
        packagename_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.ACCON_PACKAGENAME_ENTRY, "");
        intent_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.ACCON_INTENT_ENTRY, "");
        sys_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.ACCON_SYSCALL_ENTRY, "");

        // my real code
        if (switch_wifi_on == true) {
            Log.d(TAG, "Switch On WiFi");
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            Log.d(TAG, "Switched On WiFi");
        } else {
            Log.d(TAG, "It is not requested to switch on WiFi");
        }

        if (restart_player == true) {
            Log.d(TAG, "Restart the active default media player");
            Utils myUtils = new Utils();
            myUtils.shellExec("input keyevent 126");
        }

        if (packagename_call != "") {
            Log.d(TAG, "call apk by packagename");
            Utils myUtils = new Utils();
            myUtils.startActivityByPackageName(mContext, packagename_call);
        }

        if (intent_call != "") {
            Log.d(TAG, "call apk by intent");
            Utils myUtils = new Utils();
            myUtils.startActivityByIntentName(mContext, intent_call);
        }

        if (sys_call != "") {
            Log.d(TAG, "do a system call");
            Utils myUtils = new Utils();
            myUtils.shellExec(sys_call);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"FFE jobservice complete");
    }
}