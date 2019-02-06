package org.hvdw.fytfunctionalityextender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;



public class AccOnReceiver extends BroadcastReceiver {
    public static final String TAG = "FFE-AccOnReceiver";
    private boolean use_root_access;
    private boolean switch_wifi_on;
    private boolean restart_player;
    private String packagename_call;
    private String intent_call;
    private String sys_call;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        use_root_access = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.USE_ROOT_ACCESS, true);
        switch_wifi_on = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.SWITCH_WIFI_ON, true);
        restart_player = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.RESTART_PLAYER, true);
        packagename_call = PreferenceManager.getDefaultSharedPreferences(context).getString(MySettings.ACCON_PACKAGENAME_ENTRY, "");
        intent_call = PreferenceManager.getDefaultSharedPreferences(context).getString(MySettings.ACCON_INTENT_ENTRY, "");
        sys_call = PreferenceManager.getDefaultSharedPreferences(context).getString(MySettings.ACCON_SYSCALL_ENTRY, "");


        Log.d(TAG, "Detected an ACCON broadcast");

        /* As background execution is not allowed on Android 8.0 we do need a job service */
        //if ((android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) && (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) ) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "On Android 8; starting the job service");
            /* Via the JobIntentService */
            MyJobService.enqueueWork(context, new Intent());

            /* Via a dummy foreground service */
//            Log.d(TAG, "On Android 8; starting the Android O service");
//            Intent intent1 = new Intent(context.getApplicationContext(), AndroidOActivity.class);
//            context.startForegroundService(intent1);
            /* End of start of dummy foreground class */

        } else {

            if (switch_wifi_on == true) {
                Log.d(TAG, "Switch On WiFi");
                WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
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
                myUtils.startActivityByPackageName(context, packagename_call);
            }

            if (intent_call != "") {
                Log.d(TAG, "call apk by intent");
                Utils myUtils = new Utils();
                myUtils.startActivityByIntentName(context, intent_call);
            }

            if (sys_call != "") {
                Log.d(TAG, "do a system call");
                Utils myUtils = new Utils();
                myUtils.shellExec(sys_call);
            }
        } /* end of android 8 check, else statement (not on android 8) */
    } /* end of onReceive */
}
