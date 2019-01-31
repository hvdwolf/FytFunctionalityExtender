package org.hvdw.fytfunctionalityextender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class AccOnReceiver extends BroadcastReceiver {
    private boolean use_root_access;
    private boolean switch_wifi_on;
    private boolean restart_player;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        use_root_access = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.USE_ROOT_ACCESS, true);
        switch_wifi_on = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.SWITCH_WIFI_ON, true);
        restart_player = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.RESTART_PLAYER, true);

        Log.d("fytfunctionalityextender-AccOnReceiver", "Detected an ACCON broadcast");

        if (switch_wifi_on == true) {
            Log.d("fytfunctionalityextender-AccOnReceiver", "Switch On WiFi");
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            //wifiManager.setWifiEnabled(false);
            Log.d("fytfunctionalityextender-AccOnReceiver", "Switched On WiFi");
        } else {
            Log.d("fytfunctionalityextender-AccOnReceiver", "It is not requested to switch on WiFi");
        }
    }
}
