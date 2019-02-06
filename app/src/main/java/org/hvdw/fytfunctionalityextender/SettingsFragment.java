package org.hvdw.fytfunctionalityextender;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.os.Handler;
import android.widget.ProgressBar;
import android.util.AttributeSet;


public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    Context mContext;
    AttributeSet attrs;

    private ProgressBar pb;
    static Runnable myRunnable;
    private static Handler myHandler;
    private boolean zygote_reboot;

    private BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter = new IntentFilter();

    public static final String TAG = "FFE-SettingsFragment";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            Log.d(TAG, "onCreate: in Sofia 6.0.1 sdk23");
            //Running on Sofia 6.0.1 sdk23
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.preferences);
        } else {
            Log.d(TAG, "onCreate: Running on Android 8.0.0 sdk26");
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
            addPreferencesFromResource(R.xml.preferences);
        }

        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent intent = new Intent();
        String toastText = "";
        String additionalText = "";

        switch (key) {
            case MySettings.USE_ROOT_ACCESS:
                intent.setAction(MySettings.ACTION_USE_ROOT_ACCESS_CHANGED);
                intent.putExtra(MySettings.EXTRA_USE_ROOT_ACCESS_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            /* ACCON settings */
            case MySettings.SWITCH_WIFI_ON:
                intent.setAction(MySettings.ACTION_SWITCH_WIFI_ON_CHANGED);
                intent.putExtra(MySettings.EXTRA_SWITCH_WIFI_ON_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.RESTART_PLAYER:
                intent.setAction(MySettings.ACTION_RESTART_PLAYER_CHANGED);
                intent.putExtra(MySettings.EXTRA_RESTART_PLAYER_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.ACCON_PACKAGENAME_ENTRY:
                intent.setAction(MySettings.ACTION_ACCON_PACKAGENAME_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCON_PACKAGENAME_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACCON_INTENT_ENTRY:
                intent.setAction(MySettings.ACTION_ACCON_INTENT_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCON_INTENT_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACCON_SYSCALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACCON_SYSCALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCON_SYSCALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
                /* ACCOFF settings */
            case MySettings.SWITCH_WIFI_OFF:
                intent.setAction(MySettings.ACTION_SWITCH_WIFI_OFF_CHANGED);
                intent.putExtra(MySettings.EXTRA_SWITCH_WIFI_OFF_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.PAUSE_PLAYER:
                intent.setAction(MySettings.ACTION_PAUSE_PLAYER_CHANGED);
                intent.putExtra(MySettings.EXTRA_PAUSE_PLAYER_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
            case MySettings.ACCOFF_SYSCALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACCOFF_SYSCALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACCOFF_SYSCALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            default:
                Log.d(TAG, "Invalid setting encountered");
                break;
       }

        Log.d(TAG, "updated key is " + key);
        if (toastText.equals("BOOLEAN_KEY")) {
            toastText = "You updated boolean key \"" + (String)key + "\" to \"" + String.valueOf(sharedPreferences.getBoolean(key, false)) + "\"";
        } else {
            Log.d(TAG, "updated string is " + sharedPreferences.getString(key, ""));
            toastText = "You updated key \"" + key + "\" to \"" + sharedPreferences.getString(key, "") + "\"";
        }
        if (additionalText != "") {
            toastText = toastText + additionalText;
        }
        Toast mToast = Toast.makeText(mContext, toastText, Toast.LENGTH_LONG);
        mToast.show();

        if (intent.getAction() != null) {
            getActivity().sendBroadcast(intent);
        }


    }

}