package com.ardayucesan.adok_kesim.ui.controlpanel.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.data.network.model.fault.Fault;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final static String TAG = "__CutterAdapter";
    ArrayList<Fault> productList;
    Context context;
    private LayoutInflater mInflater;
    PanelPresenter presenter;
    String type;

    public ProductListAdapter(@NonNull Context context, PanelPresenter presenter, ArrayList<Fault> productList, String type) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.presenter = presenter;
        this.productList = productList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fault_stop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String faultName = productList.get(holder.getAdapterPosition()).getFaultDesc();

        holder.tvProductName.setText(faultName);

        holder.btnSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                presenter.startWorkOrder(orderList.get(holder.getAdapterPosition()).getWorkOrderId());
                if (type.equals("stop")) {
                    presenter.postProductionStatus(productList.get(holder.getAdapterPosition()).getFaultId(), productList.get(holder.getAdapterPosition()).getFaultDesc(), "6");
                    Log.d(TAG, "onClick: clicked item : " + productList.get(holder.getAdapterPosition()).getFaultDesc());
                }
                if(type.equals("fault")){
                    presenter.saveToMemory("2K", productList.get(holder.getAdapterPosition()).getFaultId());
                    Log.d(TAG, "onClick: clicked item : " + productList.get(holder.getAdapterPosition()).getFaultDesc());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView btnSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvOrderCode);
            btnSelect = itemView.findViewById(R.id.btnOrderSelect);
        }
    }

    public void filterList(ArrayList<Fault> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        productList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }


//    Shipment getItem(int id) {
//        return shipmentList.get(id);
//    }
//    private void showInfoDialog(Shipment shipment) {
//        final Dialog dialog = new Dialog(((BarcodeActivity) context));
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.order_info_dialog);
//
//        TextView tvOrdernumber = (TextView) dialog.findViewById(R.id.tvOrdernumber);
//        TextView tvCustomer = (TextView) dialog.findViewById(R.id.tvCustomer);
//        TextView tvProduct = (TextView) dialog.findViewById(R.id.tvProduct);
//        TextView tvDeadline = (TextView) dialog.findViewById(R.id.tvDeadline);
//        TextView tvQuantity = (TextView) dialog.findViewById(R.id.tvQuantity);
//        TextView tvSize = (TextView) dialog.findViewById(R.id.tvSize);
//        ImageView iconViewDialog = dialog.findViewById(R.id.iconViewDialog);
////        Button btnBackPending = dialog.findViewById(R.id.btnBackPending);
//        ConstraintLayout constraintLayoutDialogInfo = dialog.findViewById(R.id.constraintLayoutDialogInfo);
//
//        Boolean completedStatus = shipment.isShipped;
//
//        if (completedStatus) {
//            constraintLayoutDialogInfo.setBackground(context.getResources().getDrawable(R.drawable.completed_bg));
//            iconViewDialog.setImageDrawable(context.getResources().getDrawable(com.thecode.aestheticdialogs.R.drawable.ic_tick));
//        }
//
//        tvOrdernumber.setText(shipment.workorder_ordernumber);
//        tvCustomer.setText(shipment.customer_name);
//        tvProduct.setText(shipment.product_name);
//        tvDeadline.setText(shipment.workorder_deadline);
//        tvQuantity.setText(shipment.total);
//        tvSize.setText(shipment.size);
//
//        Button positiveButton = (Button) dialog.findViewById(R.id.btn_positive_flat);
//
//        positiveButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//    }

//    public String getItem(int position) {
//        return filtered_list.get(position);
//    }

}

