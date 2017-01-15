package org.onpanic.uninstallapps.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.onpanic.uninstallapps.R;


public class LockedByPermissions extends Fragment {
    private boolean mIsRooted;
    private Context mContext;
    private OnRequestRoot mListener;

    public LockedByPermissions() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Snackbar.make(getActivity().findViewById(android.R.id.content),
                R.string.root_is_needed,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.grant,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIsRooted) {
                            mListener.requestRoot();
                        } else {
                            Toast.makeText(mContext, R.string.not_rooted, Toast.LENGTH_LONG).show();
                        }
                    }
                }).show();

        return inflater.inflate(R.layout.fragment_locked, container, false);
    }

    public void setIsRooted(boolean isRooted) {
        mIsRooted = isRooted;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (mContext instanceof OnRequestRoot) {
            mListener = (OnRequestRoot) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OnRequestRoot");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRequestRoot {
        void requestRoot();
    }
}
