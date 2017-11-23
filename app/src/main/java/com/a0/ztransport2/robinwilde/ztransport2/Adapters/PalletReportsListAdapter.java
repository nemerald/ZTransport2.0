package com.a0.ztransport2.robinwilde.ztransport2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.PalletReport;
import com.a0.ztransport2.robinwilde.ztransport2.R;

import java.util.ArrayList;

public class PalletReportsListAdapter extends ArrayAdapter<PalletReport> {

    private Context context;
    private ArrayList<PalletReport> allReports;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public PalletReportsListAdapter(Context context, ArrayList<PalletReport> mReports) {
        super(context, R.layout.custom_row_layout_pallets_reports);
        this.context = context;
        this.allReports = new ArrayList<>(mReports);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allReports .size();
    }

    @Override
    public PalletReport getItem(int position) {
        return allReports .get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(PalletReport item) {
        return allReports .indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 1:
                    convertView = mInflater.inflate(R.layout.custom_row_layout_pallets_reports, parent, false);
                    holder.tvInputTimeStamp = (TextView) convertView.findViewById(R.id.tvInputTimeStamp);
                    holder.tvPrId = (TextView) convertView.findViewById(R.id.tvPrId);
                    holder.tvDriverName = (TextView) convertView.findViewById(R.id.tvDriverName);
                    holder.tvFromPlace = (TextView) convertView.findViewById(R.id.tvFromPlace);
                    holder.tvToPlace = (TextView) convertView.findViewById(R.id.tvToPlace);
                    holder.tvNoOfPallets = (TextView) convertView.findViewById(R.id.tvNoOfPallets);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvInputTimeStamp.setText(allReports.get(position).getInputTimeStamp());
        holder.tvPrId.setText(allReports.get(position).getpRId());
        holder.tvDriverName.setText(allReports.get(position).getDriver());
        holder.tvFromPlace.setText(allReports.get(position).getFromPlace());
        holder.tvToPlace.setText(allReports.get(position).getToPlace());
        holder.tvNoOfPallets.setText(allReports.get(position).getNoOfpallets());
        holder.pos = position;
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    //---------------static views for each row-----------//
    static class ViewHolder {

        TextView tvInputTimeStamp, tvPrId, tvDriverName, tvFromPlace, tvToPlace, tvNoOfPallets;
        int pos; //to store the position of the item within the list
    }
}
