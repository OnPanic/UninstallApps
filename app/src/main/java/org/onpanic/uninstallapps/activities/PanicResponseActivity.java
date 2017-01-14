package org.onpanic.uninstallapps.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;

import info.guardianproject.panic.PanicResponder;

public class PanicResponseActivity extends Activity {
    private ContentResolver cr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PanicResponder.receivedTriggerFromConnectedApp(this)) {
            cr = getContentResolver();

        }

        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}
