package org.onpanic.uninstallapps.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.onpanic.uninstallapps.R;
import org.onpanic.uninstallapps.adapters.SystemAppsAdapter;
import org.onpanic.uninstallapps.providers.AppsProvider;

public class SystemAppsList extends Fragment {
    private Context mContext;
    private PackageManager pm;
    private SystemAppsAdapter mAdapter;
    private Button cancel;
    private Button save;

    public SystemAppsList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_system_apps, container, false);

        mAdapter = new SystemAppsAdapter(pm, mContext);
        RecyclerView list = (RecyclerView) v.findViewById(R.id.apps_list);
        list.setLayoutManager(new LinearLayoutManager(v.getContext()));
        list.setAdapter(mAdapter);

        cancel = (Button) v.findViewById(R.id.sys_app_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        save = (Button) v.findViewById(R.id.sys_app_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = mContext.getContentResolver();
                for (String app : mAdapter.getSelectedApps()) {
                    ContentValues values = new ContentValues();
                    values.put(AppsProvider.App.PACKAGE_NAME, app);
                    cr.insert(AppsProvider.CONTENT_URI, values);
                }
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        pm = mContext.getPackageManager();
    }
}
