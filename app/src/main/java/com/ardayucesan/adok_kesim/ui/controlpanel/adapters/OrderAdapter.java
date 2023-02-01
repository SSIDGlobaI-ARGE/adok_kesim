package com.ardayucesan.adok_kesim.ui.controlpanel.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ardayucesan.adok_kesim.data.network.model.workorder.WorkHolder;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;
import com.ardayucesan.adok_kesim.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class OrderAdapter extends ArrayAdapter<WorkHolder> {

    private final static String TAG = "__OrderAdapter";
    ArrayList<String> workOrderList;
    ArrayList<WorkHolder> orderList;
    ArrayList<String> filtered_list;
    Context context;
    private LayoutInflater mInflater;
    List<String> FilteredArrList;
    PanelPresenter presenter;

    @Override
    public int getPosition(@Nullable WorkHolder item) {
        return super.getPosition(item);
    }

    public OrderAdapter(@NonNull Context context, int resource, ArrayList<String> workOrderList, PanelPresenter presenter, ArrayList<WorkHolder> orderList) {
        super(context,resource);
        this.context = context;
        this.workOrderList = workOrderList;
        this.filtered_list = this.workOrderList;
        this.presenter = presenter;
        this.orderList = orderList;
    }

//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//
//            @Override
//            protected void publishResults(CharSequence constraint,FilterResults results) {
//
//                filtered_list = (ArrayList<String>) results.values; // has the filtered values
//                notifyDataSetChanged();  // notifies the data with new filtered values
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                FilteredArrList = new ArrayList<String>();
//
//                if (workOrderList == null) {
//                    workOrderList = new ArrayList<String>(filtered_list); // saves the original data in mOriginalValues
//                }
//
//                /********
//                 *
//                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
//                 *  else does the Filtering and returns FilteredArrList(Filtered)
//                 *
//                 ********/
//                if (constraint == null || constraint.length() == 0) {
//
//                    // set the Original result to return
//                    results.count = workOrderList.size();
//                    results.values = workOrderList;
//
//                } else {
//
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < workOrderList.size(); i++) {
//                        String data = workOrderList.get(i);
//                        if (data.toLowerCase().startsWith(constraint.toString())) {
//                            FilteredArrList.add(data);
//                        }
//                    }
//                    // set the Filtered result to return
//                    results.count = FilteredArrList.size();
//                    results.values = FilteredArrList;
//                }
//                return results;
//            }
//        };
//        return filter;
//    }

    @Override
    public long getItemId(int position) {
        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (filtered_list == null) {
            itemID = position;
        } else {
//            itemID = FilteredArrList.indexOf(order_list.get(position));
            itemID = workOrderList.indexOf(filtered_list.get(position));
        }
        return itemID;
    }

    static class ViewHolder {

        TextView tvOrderText;
        Button btnSelect;
//        int visiblity = View.INVISIBLE;

    }

    LayoutInflater inflater;

    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder mViewHolder = null;
//        View row = null;

        if (convertView == null) {

            mViewHolder = new ViewHolder();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            convertView = inflater.inflate(R.layout.order_item, null);

            mViewHolder.tvOrderText = convertView.findViewById(R.id.tvOrderCode);
            mViewHolder.btnSelect = convertView.findViewById(R.id.btnOrderSelect);
            Log.d(TAG, "getView: position + visibility  : " + position + " - " + orderList.get(position).getVisiblity());

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
//            row = convertView;
        }

//        mViewHolder.btnSelect.setVisibility(orderList.get(position).getVisiblity());
        mViewHolder.tvOrderText.setText(workOrderList.get(position));

        mViewHolder.btnSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: selected : " + workOrderList.get(position));
//                int id = workOrderList.indexOf(filtered_list.get(position));
                presenter.startWorkOrder(orderList.get(position).getWorkOrderId());
            }
        });


//        TextView tvOrderText = row.findViewById(R.id.tvOrderCode);
//            Button btnSelect = row.findViewById(R.id.btnOrderSelect);
////        TextView tvOrderCode = (TextView) convertView.findViewById(R.id.tvOrderCode);
////        Button btnSelect = (Button) convertView.findViewById(R.id.btnMultiOrderSelect);
//
//        tvOrderText.setText(workOrderList.get(position));
////        Log.d(TAG, "getView: position : " + position);
//
//            int itemID = workOrderList.indexOf(tvOrderText.getText().toString());
//        Log.d(TAG, "getView: id item id: " + itemID);
//        Log.d(TAG, "getView: id item string : " + workOrderList.get(itemID));
//
//        if (orderList.get(position).getIsSelectable() == true) {
//                Log.d(TAG, "getView: position : " + position);
//                for (WorkHolder selectableStateHolder : orderList) {
//                    Log.d(TAG, "getView: selectable-id : " + selectableStateHolder.getIsSelectable() + " - " + selectableStateHolder.getWorkOrderId());
//                }
//                btnSelect.setVisibility(View.VISIBLE);
//            }
//
//        btnSelect.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d(TAG, "onClick: selected : " + workOrderList.get(position));
////                int id = workOrderList.indexOf(filtered_list.get(position));
//                    presenter.startWorkOrder(machine, orderList.get(position).getWorkOrderId());
//                }
//        });

        return convertView;
    }

    public int getCount() {
        return filtered_list.size();
    }

    public WorkHolder getItem(int position) {
        return orderList.get(position);
    }

}

