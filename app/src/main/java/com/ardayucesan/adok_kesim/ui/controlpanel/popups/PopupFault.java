package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.data.network.model.fault.Fault;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelActivity;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.FaultAdapter;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.FaultListAdapter;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.OrderListAdapter;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PopupFault {
    private static final int width = 700;
    private static final int height = 700;
    final PanelPresenter panelPresenter;
    final Context context;
    private PopupWindow popupWindow;
    RecyclerView faultRv;
    FaultListAdapter faultListAdapter;
    ArrayList<Fault> faultList;

    ImageView btnQuitFault;
    SearchView searchViewFault;
    String quality;
    public PopupFault(Context context, PanelPresenter panelPresenter) {
        this.context = context;
        this.panelPresenter = panelPresenter;
    }

    public void showPopUp(ArrayList<Fault> faultList,String quality) {
        this.faultList = faultList;
        this.quality = quality;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_fault, null);

        faultRv = popupView.findViewById(R.id.fault_view);
        searchViewFault = popupView.findViewById(R.id.searchViewFault);
        btnQuitFault = popupView.findViewById(R.id.btnQuitFault);

        faultRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        faultListAdapter = new FaultListAdapter(context,panelPresenter,faultList,"fault");
        faultRv.setItemViewCacheSize(faultList.size());
        faultRv.setAdapter(faultListAdapter);
        if (faultRv.getItemDecorationCount() > 0) {
            faultRv.removeItemDecorationAt(0);
        }
        faultRv.addItemDecoration(new LayoutMarginDecoration(1, 10));

        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setElevation(20);

        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        initializeSearchview();

        btnQuitFault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void initializeSearchview(){
        searchViewFault.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(s);
                return false;
            }
        });
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Fault> filteredlist = new ArrayList<Fault>();

        // running a for loop to compare elements.
        for (Fault item : faultList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getFaultDesc().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            faultListAdapter.filterList(filteredlist);
        }
    }

    public void hidePopup(){
        popupWindow.dismiss();
    }

    private PanelActivity getActivity() {
        return ((PanelActivity) context);
    }

}
