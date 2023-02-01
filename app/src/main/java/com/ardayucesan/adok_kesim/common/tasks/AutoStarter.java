package com.ardayucesan.adok_kesim.common.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ardayucesan.adok_kesim.ui.controlpanel.PanelActivity;

public class AutoStarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, PanelActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
