package org.onpanic.uninstallapps;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.onpanic.uninstallapps.Utils.RootUtils;
import org.onpanic.uninstallapps.dialogs.DeleteAppDialog;
import org.onpanic.uninstallapps.fragments.LockedByPermissions;
import org.onpanic.uninstallapps.fragments.UninstallAppsList;
import org.onpanic.uninstallapps.fragments.UninstallSettings;
import org.onpanic.uninstallapps.providers.AppsProvider;


public class UninstallAppsActivity extends AppCompatActivity implements
        UninstallAppsList.OnAppClickCallback {

    private MenuItem settingsIcon;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uninstall_apps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();

        // Do not overlapping fragments.
        if (savedInstanceState != null) return;

        initFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_uninstall_apps, menu);
        settingsIcon = menu.findItem(R.id.action_settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            settingsIcon.setVisible(false);

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new UninstallSettings())
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        switch (mFragmentManager.getBackStackEntryCount()) {
            case 0:
                super.onBackPressed();
                break;
            case 1:
                settingsIcon.setVisible(true);
                mFragmentManager.popBackStack();
                initFragment();
                break;
            default:
                mFragmentManager.popBackStack();
        }
    }

    private void initFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        String su = RootUtils.getSuPath();

        if (su != null && RootUtils.checkPermissions(su)) {
            transaction.replace(R.id.fragment_container, new UninstallAppsList());
        } else {
            transaction.replace(R.id.fragment_container, new LockedByPermissions());
        }

        transaction.commit();
    }

    @Override
    public void deleteAppCallBack(int id) {
        DeleteAppDialog dialog = new DeleteAppDialog();
        Bundle arguments = new Bundle();
        arguments.putInt(AppsProvider.App._ID, id);
        dialog.setArguments(arguments);
        dialog.show(getSupportFragmentManager(), "DeleteAppDialog");
    }
}
