package org.hvdw.fytfunctionalityextender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class AccOffReceiver extends BroadcastReceiver {
    private boolean use_root_access;
    private boolean switch_wifi_off;
    private boolean pause_player;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        use_root_access = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.USE_ROOT_ACCESS, true);
        switch_wifi_off = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.SWITCH_WIFI_OFF, true);
        pause_player = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MySettings.PAUSE_PLAYER, true);

        Log.d("fytfunctionalityextender-AccOffReceiver", "Detected an ACCOFF broadcast");

        if (switch_wifi_off == true) {
            Log.d("fytfunctionalityextender-AccOffReceiver", "Switch Off WiFi");
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
            Log.d("fytfunctionalityextender-AccOffReceiver", "Switched Off WiFi");
        } else {
            Log.d("fytfunctionalityextender-AccOffReceiver", "It is not requested to switch off WiFi");
        }
    }
}