package com.ardayucesan.adok_kesim.ui.controlpanel.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.data.network.model.workorder.WorkHolder;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private final static String TAG = "__CutterAdapter";
    ArrayList<WorkHolder> orderList;
    Context context;
    private LayoutInflater mInflater;
    PanelPresenter presenter;

    public OrderListAdapter(@NonNull Context context, PanelPresenter presenter, ArrayList<WorkHolder> orderList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.presenter = presenter;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String orderName = orderList.get(holder.getAdapterPosition()).getWorkOrderName();


        holder.tvOrderCode.setText(orderName);

        holder.btnSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.startWorkOrder(orderList.get(holder.getAdapterPosition()).getWorkOrderId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode;
        TextView btnSelect;
        TextView tvShipmentProduct;
        TextView tvShipmentSize;
        TextView tvProductionOrderItem;
        ImageView btnOrderItemInfo;
        ConstraintLayout constraintLayoutHeader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            btnSelect = itemView.findViewById(R.id.btnOrderSelect);
        }
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

