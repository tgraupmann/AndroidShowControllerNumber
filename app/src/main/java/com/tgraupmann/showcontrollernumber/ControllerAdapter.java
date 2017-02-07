package com.tgraupmann.showcontrollernumber;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by timot on 2/6/2017.
 */

public class ControllerAdapter extends ArrayAdapter<ControllerData> {
    private static final String TAG = ControllerAdapter.class.getSimpleName();

    public ControllerAdapter (Context context, List<ControllerData> items) {
        super(context, 0, items);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ControllerData item = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.controller_row, parent, false);
        }

        final Activity activity = (Activity)convertView.getContext();

        final TextView txtDeviceName = (TextView)convertView.findViewById(R.id.txtDeviceName);
        txtDeviceName.setText(item.mName);

        final TextView txtDeviceId = (TextView)convertView.findViewById(R.id.txtDeviceId);
        txtDeviceId.setText(Long.toString(item.mDeviceId)+" "+item.mDescriptor);

        final TextView txtPlayerNum = (TextView)convertView.findViewById(R.id.txtPlayerNum);
        txtPlayerNum.setText(Long.toString(item.mPlayerNum));

        final TextView txtKeyCode = (TextView)convertView.findViewById(R.id.txtKeyCode);
        txtKeyCode.setText(Long.toString(item.mKeyCode));

        return convertView;
    }
}
