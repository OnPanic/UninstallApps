package org.onpanic.uninstallapps.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.onpanic.uninstallapps.R;
import org.onpanic.uninstallapps.providers.AppsProvider;

public class DeleteAppDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        final Context context = getContext();

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Delete from db
                        context.getContentResolver().delete(
                                AppsProvider.CONTENT_URI,
                                AppsProvider.App._ID + "=" + arguments.getInt(AppsProvider.App._ID),
                                null
                        );

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // Do nothing
                        break;
                }
            }
        };

        return new AlertDialog.Builder(context)
                .setMessage(R.string.confirm_deletion)
                .setPositiveButton(R.string.delete, dialogClickListener)
                .setNegativeButton(R.string.cancel, dialogClickListener)
                .create();
    }
}
