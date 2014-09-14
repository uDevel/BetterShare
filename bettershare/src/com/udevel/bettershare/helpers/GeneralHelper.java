package com.udevel.bettershare.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import com.udevel.bettershare.constant.PrefKeys;
import com.udevel.bettershare.entity.ShareActivityInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by benny on 5/13/2014.
 */

public class GeneralHelper
{
    public static String getAppInfo(Context context) throws PackageManager.NameNotFoundException
    {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        String versionCode = String.valueOf(packageInfo.versionCode);
        String versionName = packageInfo.versionName;
        return versionCode + " - " + versionName;
    }

    public static String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer))
        {
            return capitalize(model);
        } else
        {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s)
    {
        if (s == null || s.length() == 0)
        {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first))
        {
            return s;
        } else
        {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static int convertDPtoPixel(Resources res, float dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    public static int getActionBarHeight(Context context)
    {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {

            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static int getNavigationBarHeight(Context context)
    {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context)
    {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static Point getActivitySize(Activity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getWindowSize(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static boolean isPowerConnected(Context context)
    {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    public static ArrayList<ShareActivityInfo> getInstalledAppsForShare(Context context, boolean isPrioritizeLastUsed)
    {
        ArrayList<ShareActivityInfo> result = new ArrayList<>();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Content to share");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String lastUsedShareActivityName = null;
        if(isPrioritizeLastUsed)
            lastUsedShareActivityName = sharedPref.getString(PrefKeys.KEY_PREF_LAST_USED_SHARE_ACTIVITY, null);

        for (final ResolveInfo app : activityList)
        {
            if (app.activityInfo != null)
            {
                ShareActivityInfo shareActivityInfo = new ShareActivityInfo(app.activityInfo);
                ShareActivityInfo.injectPriority(shareActivityInfo, lastUsedShareActivityName);

                result.add(shareActivityInfo);
            }
        }

        Collections.sort(result);

        for(final ShareActivityInfo shareActivityInfo : result)
            Log.d("HELPER", shareActivityInfo.getActivityInfo().name);
        return result;
    }
}


