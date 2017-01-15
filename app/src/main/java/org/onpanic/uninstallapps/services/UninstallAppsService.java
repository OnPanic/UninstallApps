package org.onpanic.uninstallapps.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.preference.PreferenceManager;

import org.onpanic.uninstallapps.R;
import org.onpanic.uninstallapps.constants.UninstallAppsConstants;
import org.onpanic.uninstallapps.notifications.TriggerNotification;
import org.onpanic.uninstallapps.providers.AppsProvider;

import java.io.DataOutputStream;
import java.io.IOException;

public class UninstallAppsService extends Service {

    public UninstallAppsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (intent != null && intent.getAction().equals(UninstallAppsConstants.DELETE_FILES)) {
            new Thread(new Runnable() {
                public void run() {
                    ContentResolver cr = getContentResolver();
                    String[] mProjection = new String[]{
                            AppsProvider.App._ID,
                            AppsProvider.App.PACKAGE_NAME,
                            AppsProvider.App.ENABLED
                    };

                    Cursor apps = cr.query(AppsProvider.CONTENT_URI, mProjection, AppsProvider.App.ENABLED + "=1", null, null);

                    if (apps != null && apps.getCount() > 0) {
                        Process p;

                        while (apps.moveToNext()) {
                            String current = apps.getString(apps.getColumnIndex(AppsProvider.App.PACKAGE_NAME));
                            try {
                                p = Runtime.getRuntime().exec("su");

                                DataOutputStream os = new DataOutputStream(p.getOutputStream());
                                os.writeBytes("/system/bin/pm uninstall " + current + "\n");
                                os.writeBytes("exit\n");
                                os.flush();
                                p.waitFor();

                            } catch (IOException | InterruptedException e) {
                                // Silent block
                            }

                            cr.delete(AppsProvider.CONTENT_URI,
                                    AppsProvider.App._ID + "=" + apps.getInt(apps.getColumnIndex(AppsProvider.App._ID)),
                                    null);
                        }

                        apps.close();
                        notifyRun();
                    }

                    stopSelf(startId);
                }
            }).start();
        }

        return Service.START_STICKY;
    }

    private void notifyRun() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean(getString(R.string.pref_runned_notification), false)) {
            TriggerNotification notification = new TriggerNotification(getApplicationContext());
            notification.show();
        }
    }
}
