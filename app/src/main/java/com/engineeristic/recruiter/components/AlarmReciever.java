package com.engineeristic.recruiter.components;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AlarmReciever extends BroadcastReceiver{
	//private Log mLogger = new Log(AlarmReciever.class);
	@Override
	public void onReceive(Context context, Intent intent)
    {
        ComponentName receiver = new ComponentName(context, AlarmReciever.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
           // Log.w("DEVICE REBOOTED called"," "+"");
        }
	}
}
