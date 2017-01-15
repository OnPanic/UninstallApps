package org.onpanic.uninstallapps.adapters;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.onpanic.uninstallapps.R;

import java.util.ArrayList;
import java.util.List;

public class SystemAppsAdapter extends RecyclerView.Adapter<SystemAppsAdapter.viewHolder> {

    private ArrayList<String> selectedApps;
    private List<PackageInfo> availableApps;
    private PackageManager mManager;
    private Context mContext;
    private CheckBox check;

    public SystemAppsAdapter(PackageManager pm, Context context) {
        selectedApps = new ArrayList<>();
        mManager = pm;
        mContext = context;
        availableApps = mManager.getInstalledPackages(0);
    }

    @Override
    public SystemAppsAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sys_app_layout, parent, false);

        check = (CheckBox) view.findViewById(R.id.sys_app_selected);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SystemAppsAdapter.viewHolder holder, int position) {
        final PackageInfo info = availableApps.get(position);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    selectedApps.add(info.packageName);
                } else {
                    selectedApps.remove(selectedApps.lastIndexOf(info.packageName));
                }
            }
        });

        try {
            holder.name.setText(mManager.getApplicationLabel(mManager.getApplicationInfo(info.packageName, 0)));
            holder.icon.setImageDrawable(mManager.getApplicationIcon(info.packageName));
        } catch (PackageManager.NameNotFoundException e) {
            holder.name.setText(mContext.getString(R.string.invalid_package));
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return availableApps.size();
    }

    public ArrayList<String> getSelectedApps() {
        return selectedApps;
    }

    class viewHolder extends RecyclerView.ViewHolder {

        public final CheckBox selected;
        public final ImageView icon;
        public final TextView name;

        viewHolder(final View row) {
            super(row);
            icon = (ImageView) row.findViewById(R.id.sys_app_icon);
            name = (TextView) row.findViewById(R.id.sys_app_name);
            selected = (CheckBox) row.findViewById(R.id.sys_app_selected);
        }
    }
}
