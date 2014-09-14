package com.udevel.bettershare.entity;

import android.content.pm.ActivityInfo;

/**
 * Created by benny on 9/9/2014.
 */
public class ShareActivityInfo implements Comparable<ShareActivityInfo>
{
    static private int PRIORITY_LAST_USED_LEVEL = 4;
    static private int PRIORITY_RECENTLY_USED_LEVEL = 3;
    static private int PRIORITY_COMMON_LEVEL_1 = 1;
    static private int PRIORITY_COMMON_LEVEL_2 = 2;
    static private int PRIORITY_UNCOMMON_LEVEL = 0;

    private ActivityInfo activityInfo;
    private int priority;

    public ShareActivityInfo(ActivityInfo activityInfo)
    {
        this.activityInfo = activityInfo;
    }

    static public void injectPriority(ShareActivityInfo shareActivityInfo, String lastUsedActivityName)
    {
        if (shareActivityInfo == null)
        {
            return;
        }

        String name = shareActivityInfo.activityInfo.name;

        if (lastUsedActivityName != null && name.equals(lastUsedActivityName))
        {
            shareActivityInfo.setPriority(PRIORITY_LAST_USED_LEVEL);
        }
        else
        {
            switch (name)
            {
                //hangout
                case "com.google.android.apps.hangouts.phone.ShareIntentActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                //hangout old
                case "com.google.android.apps.babel.phone.ShareIntentActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.google.android.apps.plus.phone.SignOnActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.google.android.gm.ComposeActivityGmail":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.tumblr.ui.activity.PostFragmentActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.facebook.composer.shareintent.ImplicitShareIntentHandler":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.facebook.messenger.activity.ShareLauncherActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.whatsapp.ContactPicker":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.twitter.android.composer.ComposerActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.google.android.libraries.social.gateway.GatewayActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_2);
                    break;
                case "com.pushbullet.android.ui.PushActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_1);
                    break;
                case "com.google.android.apps.docs.app.SendTextToClipboardActivity":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_1);
                    break;
                case "com.android.email.activity.MessageCompose":
                    shareActivityInfo.setPriority(PRIORITY_COMMON_LEVEL_1);
                    break;
                default:
                    shareActivityInfo.setPriority(PRIORITY_UNCOMMON_LEVEL);
                    break;
            }
        }
    }

    public ActivityInfo getActivityInfo()
    {
        return activityInfo;
    }

    public void setActivityInfo(ActivityInfo activityInfo)
    {
        this.activityInfo = activityInfo;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(ShareActivityInfo another)
    {
        return another.priority - this.priority;
    }
}
