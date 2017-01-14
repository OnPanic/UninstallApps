package org.onpanic.uninstallapps.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import org.onpanic.uninstallapps.R;
import org.onpanic.uninstallapps.constants.UninstallAppsConstants;
import org.onpanic.uninstallapps.notifications.TriggerNotification;

public class UninstallAppsService extends Service {
    private SharedPreferences prefs;

    public UninstallAppsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (intent != null && intent.getAction().equals(UninstallAppsConstants.DELETE_FILES)) {

        }

        return Service.START_STICKY;
    }

    private void notifyRun() {
        if (prefs.getBoolean(getString(R.string.pref_runned_notification), false)) {
            TriggerNotification notification = new TriggerNotification(getApplicationContext());
            notification.show();
        }
    }
}
