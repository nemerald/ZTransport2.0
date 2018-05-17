package com.a0.ztransport2.robinwilde.ztransport2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.User;
import com.a0.ztransport2.robinwilde.ztransport2.R;

import java.util.ArrayList;


public class EmployeesListAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> allEmployees;
    private ArrayList hasReportedToday;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public EmployeesListAdapter(Context context, ArrayList<User> mEmployees, ArrayList hasReportedToday) {
        super(context, R.layout.custom_row_layout_employees);
        this.context = context;
        this.allEmployees = new ArrayList<>(mEmployees);
        this.hasReportedToday = new ArrayList();
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allEmployees .size();
    }

    @Override
    public User getItem(int position) {
        return allEmployees .get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(User item) {
        return allEmployees .indexOf(item);
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
                    convertView = mInflater.inflate(R.layout.custom_row_layout_employees,parent, false);
                    holder.tvEmployeeName = (TextView) convertView.findViewById(R.id.tvEmployeeName);
                    holder.tvEmployeePhoneNumber = (TextView) convertView.findViewById(R.id.tvEmployeePhoneNumber);
                    holder.tvHasReported = (TextView) convertView.findViewById(R.id.tvHasReported);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvEmployeeName.setText(allEmployees.get(position).getName());
        holder.tvEmployeePhoneNumber.setText(allEmployees.get(position).getPhoneNumber());
        for (User user: allEmployees) {
            if(hasReportedToday.contains(user.getName().trim().toLowerCase())){
                holder.tvHasReported.setText("Ja");
            }else{
                holder.tvHasReported.setText("Nej");
            }
        }
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
        TextView tvEmployeeName, tvEmployeePhoneNumber, tvHasReported;
        int pos; //to store the position of the item within the list
    }
}
