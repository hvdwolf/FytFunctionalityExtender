package org.hvdw.fytfunctionalityextender;

import android.app.Activity;

import android.content.res.AssetManager;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;

class Utils {
    private static Context mContext = null;
    public static final String TAG = "fytfunctionalityextender-Utils";
    private boolean use_root_access;
    private static SharedPreferences sharedprefs = null;


    public static void init (Context context) {
        if(mContext != null)
			return;
		mContext = context;
		sharedprefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		// backward compatible
		/*if(!mPrefer.getBoolean(PREF_COMPATIBLE, false))
		{
			SharedPreferences prefer = mContext.getSharedPreferences(PREFER, Context.MODE_WORLD_READABLE);
			if(prefer.contains(AUTOMODE))
				mPrefer.edit().putBoolean(AUTOMODE, prefer.getBoolean(AUTOMODE, false)).commit();
			if(prefer.contains(FAVORMASKVALUE))
				mPrefer.edit().putInt(FAVORMASKVALUE, prefer.getInt(FAVORMASKVALUE, 250)).commit();
			if(prefer.contains(PROXIMITYMAX))
				mPrefer.edit().putFloat(PROXIMITYMAX, prefer.getFloat(PROXIMITYMAX, ProximitySensor.DEFAULT_DISTANCE)).commit();
			if(prefer.contains(PROXIMITYMIN))
				mPrefer.edit().putFloat(PROXIMITYMIN, prefer.getFloat(PROXIMITYMIN, ProximitySensor.DEFAULT_DISTANCE)).commit();
			mPrefer.edit().putBoolean(PREF_COMPATIBLE, true).commit();
		} */
    }

/**********************************************************************************************************************************************/

    public void whichActionToPerform (Context context, String callMethod, String actionString) {
        if (callMethod.equals("pkgname")) {
            //Log.d(TAG, " the callmethond is indeed pkgname");
            startActivityByPackageName(context, actionString);
        }
        if (callMethod.equals("pkg_intent")) {
            startActivityByIntentName(context, actionString);
        }
        if (callMethod.equals("sys_call")) {
            //SharedPreferences sharedprefs = new SharedPreferences("org.hvdw.fytfunctionalityextender");
            //sharedprefs.makeWorldReadable();
            use_root_access = sharedprefs.getBoolean(MySettings.USE_ROOT_ACCESS, true);
            //executeSystemCall(actionString);
            String[] cmd = actionString.split(";");
            if (use_root_access == true) {
                rootExec(cmd);
            } else {
                shellExec(cmd);
            }
        }
    };



/*  More complicated versions of above shell and su call. As I want to run multiple commands I also need to look at that. 
    copied from https://stackoverflow.com/questions/20932102/execute-shell-command-from-android/26654728
    from the code of CarloCannas
*/
    public static String shellExec(String... strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try {
            Process sh = Runtime.getRuntime().exec("sh");
            outputStream = new DataOutputStream(sh.getOutputStream());
            response = sh.getInputStream();

            for (String s : strings) {
                s = s.trim();
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                sh.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closer.closeSilently(outputStream, response);
        }
        return res;
    }


    public static String rootExec(String... strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try {
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();

            for (String s : strings) {
                s = s.trim();
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closer.closeSilently(outputStream, response);
        }
        return res;
    }

    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }
/* end of shell and su call functions/methods */

    private static void executeBroadcast(String input) {
        StringBuffer output = new StringBuffer();
        String cmd = "am broadcast -a " + input;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            Log.d(TAG, cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public void startActivityByIntentName(Context context, String component) {
        Intent sIntent = new Intent(Intent.ACTION_MAIN);
        sIntent.setComponent(ComponentName.unflattenFromString(component));
        sIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sIntent);
    }


    public void startActivityByPackageName(Context context, String packageName) {
        PackageManager pManager = context.getPackageManager();
        Intent intent = pManager.getLaunchIntentForPackage(packageName);
        Log.d(TAG, " startActivityByPackageName: " + packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

}