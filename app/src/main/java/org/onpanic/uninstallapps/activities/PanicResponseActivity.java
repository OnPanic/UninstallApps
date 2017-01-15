package org.onpanic.uninstallapps.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import org.onpanic.uninstallapps.constants.UninstallAppsConstants;
import org.onpanic.uninstallapps.services.UninstallAppsService;

import info.guardianproject.panic.PanicResponder;

public class PanicResponseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PanicResponder.receivedTriggerFromConnectedApp(this)) {
            // File deletion can take a while
            Intent intent = new Intent(this, UninstallAppsService.class);
            intent.setAction(UninstallAppsConstants.DELETE_FILES);

            startService(intent);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}
