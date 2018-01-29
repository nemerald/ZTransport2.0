package com.a0.ztransport2.robinwilde.ztransport2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;
import com.a0.ztransport2.robinwilde.ztransport2.R;

import java.util.ArrayList;


public class TimeReportsListAdapter extends ArrayAdapter<TimeReport> {

    private Context context;
    private ArrayList<TimeReport> allReports;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public TimeReportsListAdapter(Context context, ArrayList<TimeReport> mReports) {
        super(context, R.layout.custom_row_layout_time_reports);
        this.context = context;
        this.allReports = new ArrayList<>(mReports);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allReports .size();
    }

    @Override
    public TimeReport getItem(int position) {
        return allReports .get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(TimeReport item) {
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
                    convertView = mInflater.inflate(R.layout.custom_row_layout_time_reports,parent, false);
                    holder.tvTrId = (TextView) convertView.findViewById(R.id.tvTrId);
                    holder.tvYear = (TextView) convertView.findViewById(R.id.tvYear);
                    holder.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
                    holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
                    holder.tvWeek = (TextView) convertView.findViewById(R.id.tvWeek);
                    holder.tvInputTimeStamp = (TextView) convertView.findViewById(R.id.tvInputTimeStamp);
                    holder.tvDriverName = (TextView) convertView.findViewById(R.id.tvDriverName);
                    holder.tvCostumer = (TextView) convertView.findViewById(R.id.tvCostumer);
                    holder.tvArea = (TextView) convertView.findViewById(R.id.tvArea);
                    holder.tvHours = (TextView) convertView.findViewById(R.id.tvHours);
                    holder.tvIsRoute = (TextView) convertView.findViewById(R.id.tvIsRoute);
                    holder.tvWorkDescription = (TextView) convertView.findViewById(R.id.tvWorkDescription);
                    holder.tvChangedByAdmin = (TextView) convertView.findViewById(R.id.tvChangedByAdmin);
                    holder.tvReportedBy = (TextView) convertView.findViewById(R.id.tvReportedBy);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.driveId.setText(String.valueOf(allReports.get(position).getDriveId()));
        holder.tvTrId.setText(allReports.get(position).getReportId());
        holder.tvYear.setText(allReports.get(position).getYear());
        holder.tvMonth.setText(allReports.get(position).getMonth());
        holder.tvDay.setText(allReports.get(position).getDay());
        holder.tvWeek.setText(allReports.get(position).getWeek());
        holder.tvInputTimeStamp.setText(allReports.get(position).getReportTimeStamp());
        holder.tvDriverName.setText(allReports.get(position).getReportDriverName());
        holder.tvCostumer.setText(allReports.get(position).getCostumer());
        holder.tvArea.setText(allReports.get(position).getArea());
        holder.tvHours.setText(allReports.get(position).getHours());
        if(allReports.get(position).isRoute()){
            holder.tvIsRoute.setText("("+context.getString(R.string.yes_letter)+")");
        }else{
            holder.tvIsRoute.setText("("+context.getString(R.string.no_letter)+")");
        }
        holder.tvWorkDescription.setText(allReports.get(position).getWorkDescription());
        if(allReports.get(position).isChangedByAdmin()){
            holder.tvChangedByAdmin.setText(context.getString(R.string.yes_letter));
        }else{
            holder.tvChangedByAdmin.setText(context.getString(R.string.no_letter));
        }
        holder.tvReportedBy.setText(allReports.get(position).getReportReporterName());
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

        TextView tvTrId, tvYear, tvMonth, tvDay, tvWeek, tvInputTimeStamp, tvDriverName, tvCostumer,
                 tvArea, tvHours, tvIsRoute, tvWorkDescription, tvChangedByAdmin, tvReportedBy;
        int pos; //to store the position of the item within the list
    }
}
