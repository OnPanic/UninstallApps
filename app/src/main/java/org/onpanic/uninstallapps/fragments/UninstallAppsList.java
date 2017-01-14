package org.onpanic.uninstallapps.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.onpanic.uninstallapps.R;
import org.onpanic.uninstallapps.adapters.AppsAdapter;
import org.onpanic.uninstallapps.providers.AppsProvider;

public class UninstallAppsList extends Fragment {
    private FloatingActionButton mFab;
    private ContentResolver mContentResolver;
    private AppsObserver mPathsObserver;
    private Context mContext;
    private AppsAdapter mApps;
    private OnAppClickCallback mListener;

    private String[] mProjection = new String[]{
            AppsProvider.App._ID,
            AppsProvider.App.PACKAGE_NAME,
            AppsProvider.App.ENABLED
    };

    public UninstallAppsList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_uninstall_apps, container, false);

        mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mApps = new AppsAdapter(
                mContext,
                mContentResolver.query(
                        AppsProvider.CONTENT_URI, mProjection, null, null, null
                ),
                mListener
        );

        mContentResolver.registerContentObserver(AppsProvider.CONTENT_URI, true, mPathsObserver);

        RecyclerView list = (RecyclerView) v.findViewById(R.id.apps_list);
        list.setLayoutManager(new LinearLayoutManager(v.getContext()));
        list.setAdapter(mApps);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        mPathsObserver = new AppsObserver(new Handler());

        if (mContext instanceof OnAppClickCallback) {
            mListener = (OnAppClickCallback) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OnAppClickCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mContentResolver.unregisterContentObserver(mPathsObserver);
    }

    public interface OnAppClickCallback {
        void deleteAppCallBack(int id);
    }

    class AppsObserver extends ContentObserver {
        AppsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            mApps.changeCursor(mContentResolver.query(
                    AppsProvider.CONTENT_URI, mProjection, null, null, null
            ));
        }
    }
}
