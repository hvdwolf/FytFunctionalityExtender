package org.hvdw.fytfunctionalityextender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/* On Android 8 we are not allowed to start a background activity on BOOT_COMPLETED
   so we need some AndroidO foreground activity which does "something" so that our receiver
   can also do something
*/

public class AndroidOActivity extends Activity {
    public static final String TAG = "FFE-AndroidOActivity";
    private boolean use_root_access;
    private boolean switch_wifi_on;
    private boolean restart_player;
    private String packagename_call;
    private String intent_call;
    private String sys_call;
    Activity mActivity;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        Log.d(TAG, "The broadcast receiver detected an ACCON broadcast");
        Log.d(TAG, "Obviously we are on Android 8 so that's why this AndroidOActivity is started.");

        use_root_access = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(MySettings.USE_ROOT_ACCESS, true);
        switch_wifi_on = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(MySettings.SWITCH_WIFI_ON, true);
        restart_player = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(MySettings.RESTART_PLAYER, true);
        packagename_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.ACCON_PACKAGENAME_ENTRY, "");
        intent_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.ACCON_INTENT_ENTRY, "");
        sys_call = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MySettings.ACCON_SYSCALL_ENTRY, "");

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
            Utils myUtils = new Utils();
            myUtils.startActivityByPackageName(mContext, packagename_call);
        }

        if (intent_call != "") {
            Utils myUtils = new Utils();
            myUtils.startActivityByIntentName(mContext, intent_call);
        }

        if (sys_call != "") {
            Utils myUtils = new Utils();
            myUtils.shellExec(sys_call);
        }


    }
}