package com.udevel.bettershare;

import android.app.*;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.udevel.bettershare.constant.PrefKeys;
import com.udevel.bettershare.entity.ShareActivityInfo;
import com.udevel.bettershare.helpers.GeneralHelper;

import java.util.List;

/**
 * Created by benny on 5/28/2014.
 * Customized share dialog
 */
public class DialogFrag_BetterShare extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = DialogFrag_BetterShare.class.getSimpleName();
    public static final double DIALOG_HEIGHT_RATIO = 0.9;
    public static final double DIALOG_WIDTH_RATIO = 0.9;
    public static final int MAX_DIALOG_WIDTH = 450;
    public static final int NUM_OF_COLUMNS = 3;
    public static final int NUM_OF_ROW_WHEN_MINIMIZED = 3;
    public static final int NUM_OF_COLUMNS_LAND = 4;
    public static final int NUM_OF_ROW_WHEN_MINIMIZED_LAND = 2;

    private static DialogFrag_BetterShare mInstance;
    private static String dialogTag;

    private static String shareSubject = "Share subject";
    private static String shareText = "share text";
    private static boolean isPrioritizeLastUsed = true;

    private Context mContext;
    private int numOfColumns;
    private int numOfRowsWhenMinimized;
    private LayoutInflater mInflater;
    private ViewGroup root;
    private LinearLayout ll_share;
    private OnSendShareListener onSendShareListener;
    private TextView tv_shareMore;

    private List<ShareActivityInfo> shareActivityInfos;

    static public DialogFrag_BetterShare getInstance()
    {
        if (mInstance == null)
            mInstance = new DialogFrag_BetterShare();

        return mInstance;
    }

    static private void dismissItselfFromFragmentManager(Activity activity)
    {
        if (activity != null)
        {
            FragmentManager fragmentManager = activity.getFragmentManager();
            Fragment dialogFrag = fragmentManager.findFragmentByTag(dialogTag);
            ((DialogFragment) dialogFrag).dismiss();
            fragmentManager.executePendingTransactions();
            mInstance = null;
        }
    }

    /**
     * @param subject              Subject text that appears in share intent
     * @param text                 Content text that appears in share intent
     * @param isPrioritizeLastUsed If true, dialog will show last used share component as first icon.
     */
    static public void setup(String subject, String text, boolean isPrioritizeLastUsed)
    {
        DialogFrag_BetterShare.shareSubject = subject;
        DialogFrag_BetterShare.shareText = text;
        DialogFrag_BetterShare.isPrioritizeLastUsed = isPrioritizeLastUsed;
    }

    @Override
    public void show(FragmentManager manager, String tag)
    {
        if (!this.isAdded())
        {
            dialogTag = tag;
            super.show(manager, tag);
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag)
    {

        if (!this.isAdded())
        {
            dialogTag = tag;
            return super.show(transaction, tag);
        } else
            return -1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Log.i(TAG, "onCreateDialog");
        mContext = getActivity().getApplicationContext();
        mInflater = getActivity().getLayoutInflater();

        shareActivityInfos = GeneralHelper.getInstalledAppsForShare(mContext, isPrioritizeLastUsed);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.Dialog_No_Border);
        root = setupView();
        setupShare();
        alertDialogBuilder.setView(root);

        return alertDialogBuilder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        Log.i(TAG, "onCancel");
        super.onCancel(dialog);

        dismissItselfFromFragmentManager(getActivity());
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        Log.i(TAG, "onDismiss");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        Log.d(TAG, "onActivityCreated");
        if (savedInstanceState == null)
        {
            //getDialog().getWindow().setLayout(300,300);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        Log.i(TAG, "onStart");
        super.onStart();
        setDialogSize(false);
    }

    @Override
    public void onStop()
    {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView()
    {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.tv_shareMore)
        {
            v.setVisibility(View.GONE);

            final int numOfIconsWhenMinimized = numOfRowsWhenMinimized * numOfColumns;
            final int startIndex = numOfIconsWhenMinimized;
            final int endIndex = shareActivityInfos.size();

            loadShareAppIcons(endIndex, startIndex);
            setDialogSize(true);
        } else if (i == R.id.rl_share)
        {
            sendShare((ActivityInfo) v.getTag());
        }
    }

    private ViewGroup setupView()
    {
        ViewGroup root = (ViewGroup) mInflater.inflate(R.layout.dialogfrag_share, null);
        ll_share = (LinearLayout) root.findViewById(R.id.ll_share);
        tv_shareMore = (TextView) root.findViewById(R.id.tv_shareMore);
        tv_shareMore.setOnClickListener(this);
        tv_shareMore.setVisibility(View.GONE);
        return root;
    }

    private void setupShare()
    {
        //To determine layout
        if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE)
        {
            numOfColumns = NUM_OF_COLUMNS_LAND;
            numOfRowsWhenMinimized = NUM_OF_ROW_WHEN_MINIMIZED_LAND;
        } else
        {
            numOfColumns = NUM_OF_COLUMNS;
            numOfRowsWhenMinimized = NUM_OF_ROW_WHEN_MINIMIZED;
        }

        final int numOfIconsWhenMinimized = numOfRowsWhenMinimized * numOfColumns;

        final int startIndex = 0;

        if (shareActivityInfos.size() > numOfIconsWhenMinimized)
        {
            loadShareAppIcons(numOfIconsWhenMinimized, startIndex);
            tv_shareMore.setVisibility(View.VISIBLE);
        } else
        {
            loadShareAppIcons(shareActivityInfos.size(), startIndex);
        }
    }

    private void loadShareAppIcons(int countMinimized, int startIndex)
    {
        final PackageManager pm = mContext.getPackageManager();
        final int childMargin = GeneralHelper.convertDPtoPixel(getResources(), 5);
        for (int i = startIndex; i < countMinimized; i = i + numOfColumns)
        {
            final LinearLayout ll_row = (LinearLayout) mInflater.inflate(R.layout.list_element_share, root, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1.0f;
            params.setMargins(childMargin, childMargin, childMargin, childMargin);
            params.gravity = Gravity.CENTER;

            for (int j = 0; j < numOfColumns; j++)
            {
                int packageIndex = i + j;
                RelativeLayout appLayout = (RelativeLayout) mInflater.inflate(R.layout.list_element_share_child, null, false);
                ImageView iv_shareIcon = (ImageView) appLayout.findViewById(R.id.iv_share);
                TextView tv_shareAppName = (TextView) appLayout.findViewById(R.id.tv_shareAppName);
                if (packageIndex < shareActivityInfos.size())
                {
                    ActivityInfo activityInfo = shareActivityInfos.get(i + j).getActivityInfo();
                    String appName = ((String) pm.getApplicationLabel(activityInfo.applicationInfo));
                    String packageName = activityInfo.packageName;

                    appLayout.setTag(activityInfo);// For on click

                    Drawable icon;
                    try
                    {
                        if (activityInfo.name.equals("com.google.android.apps.docs.app.SendTextToClipboardActivity"))
                        {
                            icon = getResources().getDrawable(R.drawable.ic_action_copy);
                            appName = "Clipboard";
                        } else
                        {
                            icon = mContext.getPackageManager().getApplicationIcon(packageName);
                        }

                        iv_shareIcon.setImageDrawable(icon);
                    } catch (PackageManager.NameNotFoundException e)
                    {
                        e.printStackTrace();
                    }

                    tv_shareAppName.setText(appName);

                    appLayout.setOnClickListener(this);
                } else
                {
                    appLayout.setVisibility(View.INVISIBLE);
                }
                ll_row.addView(appLayout, params);
            }
            ll_share.addView(ll_row);
        }
    }

    private void setDialogSize(boolean isExtendHeight)
    {
        Point size = GeneralHelper.getWindowSize(mContext);
        Window window = getDialog().getWindow();

        int targetWidth = Math.min((int) (DIALOG_WIDTH_RATIO * size.x), GeneralHelper.convertDPtoPixel(getResources(), MAX_DIALOG_WIDTH));
        int targetHeight = (int) (DIALOG_HEIGHT_RATIO * size.y);

        if (!isExtendHeight)
            window.setLayout(targetWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        else
            window.setLayout(targetWidth, targetHeight);
        window.setGravity(Gravity.CENTER);
    }

    private void sendShare(ActivityInfo activityInfo)
    {
        // Save last used for share prioritizing
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PrefKeys.KEY_PREF_LAST_USED_SHARE_ACTIVITY, activityInfo.name);
        editor.commit();

        final ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        sendIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        sendIntent.setComponent(name);
        startActivity(sendIntent);

        dismissItselfFromFragmentManager(getActivity());
    }

    public interface OnSendShareListener
    {
        public void onSendShare(ActivityInfo activityInfo);
    }
}

