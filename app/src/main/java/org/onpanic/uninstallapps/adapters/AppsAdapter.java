package org.onpanic.uninstallapps.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.onpanic.uninstallapps.R;
import org.onpanic.uninstallapps.fragments.UninstallAppsList;
import org.onpanic.uninstallapps.providers.AppsProvider;


public class AppsAdapter extends CursorRecyclerViewAdapter<AppsAdapter.ViewHolder> {

    private Context mContext;
    private ContentResolver mResolver;
    private PackageManager mPM;
    private UninstallAppsList.OnAppClickCallback mListener;

    public AppsAdapter(Context context, Cursor cursor, UninstallAppsList.OnAppClickCallback listener) {
        super(cursor);
        mContext = context;
        mListener = listener;
        mResolver = mContext.getContentResolver();
        mPM = mContext.getPackageManager();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
        final int id = cursor.getInt(cursor.getColumnIndex(AppsProvider.App._ID));
        final String name = cursor.getString(cursor.getColumnIndex(AppsProvider.App.PACKAGE_NAME));
        final Boolean active = (cursor.getInt(cursor.getColumnIndex(AppsProvider.App.ENABLED)) == 1);

        viewHolder.mActive.setChecked(active);
        viewHolder.mActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues fields = new ContentValues();
                fields.put(AppsProvider.App.ENABLED, isChecked);
                mResolver.update(
                        AppsProvider.CONTENT_URI, fields, "_ID=" + id, null
                );
            }
        });

        try {
            viewHolder.mName.setText(mPM.getApplicationLabel(mPM.getApplicationInfo(name, 0)));
            viewHolder.mImage.setImageDrawable(mPM.getApplicationIcon(name));
        } catch (PackageManager.NameNotFoundException e) {
            viewHolder.mName.setText(mContext.getString(R.string.invalid_package));
            e.printStackTrace();
        }

        viewHolder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteAppCallBack(id);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImage;
        final Switch mActive;
        final TextView mName;

        ViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.app_image);
            mActive = (Switch) view.findViewById(R.id.app_active);
            mName = (TextView) view.findViewById(R.id.app_name);
        }
    }
}
