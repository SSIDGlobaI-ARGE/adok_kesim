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
import com.ardayucesan.gezderi_uretim.data.network.model.fault.Fault;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FaultAdapter extends ArrayAdapter<String> {

    ArrayList<String> faultDescriptionList;
    ArrayList<String> faultIdList;
    ArrayList<String> filtered_list;
    Context context;
    private LayoutInflater mInflater;
    List<String> FilteredArrList;
    List<Fault> faults;

    //    public OrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
//        this.order_list = objects;
//        this.filtered_list = objects;
//        this.context = context;
//        mInflater = LayoutInflater.from(context);
//

    public FaultAdapter(@NonNull Context context, int resource, ArrayList<String> fault_list, List<Fault> faults) {
        super(context,resource,fault_list);
        this.context = context;
        this.faults = faults;
        this.faultDescriptionList = fault_list;
        this.filtered_list = this.faultDescriptionList;
        this.faultIdList = setFaultIdList();
    }

    public ArrayList<String> setFaultIdList(){
        return (ArrayList<String>) faults.stream()
                .map(Fault::getFaultId)
                .collect(Collectors.toList());
    }


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

                if (faultDescriptionList == null) {
                    faultDescriptionList = new ArrayList<String>(filtered_list); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = faultDescriptionList.size();
                    results.values = faultDescriptionList;

                } else {

                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < faultDescriptionList.size(); i++) {
                        String data = faultDescriptionList.get(i);
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
            itemID = faultDescriptionList.indexOf(filtered_list.get(position));
        }
        return itemID;
    }

    public String getFaultId(int position){
        return faultIdList.get((int)getItemId(position));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.faultcode_item, parent, false);
        }
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.customTV);
        textViewItemName.setText(filtered_list.get(position));
        return convertView;
    }

    public int getCount() {
        return filtered_list.size();
    }

    public String getItem(int position) {
        return filtered_list.get(position);
    }

}

