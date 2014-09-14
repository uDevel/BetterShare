package com.udevel.bettershareexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.udevel.bettershare.DialogFrag_BetterShare;

public class Activity_BetterShareExample extends Activity implements View.OnClickListener
{

    public static final String TAG_SHARE_DIALOG_FRAGMENT = "TAG_SHARE_DIALOG_FRAGMENT";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button shareButton = (Button) findViewById(R.id.bt_showShareDialog);
        shareButton.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        DialogFrag_BetterShare dialogFrag_betterShare = DialogFrag_BetterShare.getInstance();
        dialogFrag_betterShare.show(getFragmentManager(), TAG_SHARE_DIALOG_FRAGMENT);
    }
}
