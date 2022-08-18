package com.ardayucesan.gezderi_uretim.ui.controlpanel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ardayucesan.gezderi_uretim.R;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends ArrayAdapter<String> {

    ArrayList<String> ticketList;
    ArrayList<String> filtered_list;
    Context context;
    private LayoutInflater mInflater;
    List<String> FilteredArrList;

    //    public OrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
//        this.order_list = objects;
//        this.filtered_list = objects;
//        this.context = context;
//        mInflater = LayoutInflater.from(context);
//

    public TicketAdapter(@NonNull Context context, int resource, ArrayList<String> ticketList) {
        super(context,resource,ticketList);
        this.context = context;
        this.ticketList = ticketList;
        this.filtered_list = ticketList;
    }
    //test torum
    //test
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                filtered_list = (ArrayList<String>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                FilteredArrList = new ArrayList<String>();

                if (ticketList == null) {
                    ticketList = new ArrayList<String>(filtered_list); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = ticketList.size();
                    results.values = ticketList;

                } else {

                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < ticketList.size(); i++) {
                        String data = ticketList.get(i);
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    @Override
    public long getItemId(int position) {
        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (filtered_list == null) {
            itemID = position;
        } else {
//            itemID = FilteredArrList.indexOf(order_list.get(position));
            itemID = ticketList.indexOf(filtered_list.get(position));
        }
        return itemID;
    }

    //viewhholder pattern
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        View row = convertView;

        ViewHolder holder = null;
        if (row == null) {
            row = LayoutInflater.from(context).
                    inflate(R.layout.faultcode_item, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView)  row.findViewById(R.id.customTV);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        holder.textView.setText(filtered_list.get(position));

        return row;
    }

    public int getCount() {
        return filtered_list.size();
    }

    public String getItem(int position) {
        return filtered_list.get(position);
    }

    private static class ViewHolder {
        TextView textView;
    }
}
