package org.onpanic.uninstallapps.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.onpanic.uninstallapps.R;

public class UninstallAppsList extends Fragment {

    public UninstallAppsList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uninstall_apps, container, false);
    }
}
