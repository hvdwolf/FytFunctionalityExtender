package org.hvdw.fytfunctionalityextender;

import android.util.Log;
//import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
//import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceFragment;
//import android.preference.PreferenceManager;
//import android.preference.PreferenceActivity;
//import android.preference.ListPreference;
import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ProgressBar;
import android.util.AttributeSet;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.List;
import java.util.ArrayList;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;



public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    Context mContext;
    AttributeSet attrs;

    private ProgressBar pb;
    static Runnable myRunnable;
    private static Handler myHandler;
    private boolean zygote_reboot;

    private BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter = new IntentFilter();



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();

        //Toast mToast = Toast.makeText(mContext, "Retrieving installed apps", Toast.LENGTH_LONG);
        //mToast.show();

    }

    public static final String TAG = "fytfunctionalityextender-SettingsFragment";



    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N){
            Log.d(TAG, "onCreate: in Sofia 6.0.1 sdk23");
            //Old version used on Sofia 6.0.1 sdk23
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.preferences);
        } else {
            Log.d(TAG, "onCreate: Running on Android 8.0.0 sdk26");
            getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
            addPreferencesFromResource(R.xml.preferences);
        }

        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        //getPackages(mContext);
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
            /* All the settings belonging to the SofiaServer */
/*            case MySettings.BAND_CALL_OPTION:
                intent.setAction(MySettings.ACTION_BAND_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.BAND_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_BAND_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_BAND_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
*/
            case MySettings.USE_ROOT_ACCESS:
                intent.setAction(MySettings.ACTION_USE_ROOT_ACCESS_CHANGED);
                intent.putExtra(MySettings.EXTRA_USE_ROOT_ACCESS_ENABLED, sharedPreferences.getBoolean(key, true));
                toastText = "BOOLEAN_KEY";
                break;
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

            case MySettings.DVD_CALL_OPTION:
                intent.setAction(MySettings.ACTION_DVD_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.DVD_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_DVD_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_DVD_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
/*            case MySettings.EQ_CALL_OPTION:
                intent.setAction(MySettings.ACTION_EQ_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.EQ_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_EQ_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_EQ_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break; */
            case MySettings.MEDIA_CALL_OPTION:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.MEDIA_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_MEDIA_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_MEDIA_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
/*            case MySettings.NAVI_CALL_OPTION:
                intent.setAction(MySettings.ACTION_NAVI_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.NAVI_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_NAVI_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_NAVI_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break; */
            case MySettings.ACC_ON_CALL_OPTION:
                intent.setAction(MySettings.ACTION_ACC_ON_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_ON_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACC_ON_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACC_ON_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_ON_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACC_OFF_CALL_OPTION:
                intent.setAction(MySettings.ACTION_ACC_OFF_CALL_OPTION_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_OFF_CALL_OPTION_STRING, sharedPreferences.getString(key, ""));
                break;
            case MySettings.ACC_OFF_CALL_ENTRY:
                intent.setAction(MySettings.ACTION_ACC_OFF_CALL_ENTRY_CHANGED);
                intent.putExtra(MySettings.EXTRA_ACC_OFF_CALL_ENTRY_STRING, sharedPreferences.getString(key, ""));
                break;
/*            case MySettings.SHOW_CPU_TEMP:
                intent.setAction(MySettings.ACTION_SHOW_CPU_TEMP_CHANGED);
                intent.putExtra(MySettings.EXTRA_SHOW_CPU_TEMP_ENABLED, sharedPreferences.getBoolean(key, false));
                toastText = "BOOLEAN_KEY";
                additionalText = "\nWait up to 1 minute for the update of the time in the status bar";
                break; */
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