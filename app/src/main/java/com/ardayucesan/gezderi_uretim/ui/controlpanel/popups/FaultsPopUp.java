package com.ardayucesan.gezderi_uretim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.ardayucesan.gezderi_uretim.R;
import com.ardayucesan.gezderi_uretim.ui.controlpanel._PanelContract;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.adapters.FaultAdapter;
import com.ardayucesan.gezderi_uretim.common.Constants;

@SuppressWarnings("FieldCanBeLocal")
public class FaultsPopUp implements _BasePopUp.FaultPopUp {

    private static final String TAG = "__FaultsPopUp";
    //layout parameters
    private static final int width = 600;
    private static final int height = 750;
    private Boolean isForced;
    //layout view components
    private ListView faultsListView;
    private SearchView searchview;
    private TextView tvFaultHeader;
    //Windows and views
    private PopupWindow popupWindowFaults;
    private View popupViewFaults;
    //construct params
    private final Context context;
    private final _PanelContract.Presenter panelPresenter;
    private final _PanelContract.View panelView;

    public FaultsPopUp(Context context, _PanelContract.Presenter panelPresenter, _PanelContract.View panelView) {
        this.context = context;
        this.panelPresenter = panelPresenter;
        this.panelView = panelView;
    }

    @Override
    public void showPopUp(FaultAdapter faultsAdapter, Boolean isForced) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupViewFaults = inflater.inflate(R.layout.popup_faults, null);
        searchview = popupViewFaults.findViewById(R.id.searchViewStop);
        faultsListView = popupViewFaults.findViewById(R.id.fault_view);
        tvFaultHeader = popupViewFaults.findViewById(R.id.tvFaultHeader);
        faultsListView.setAdapter(faultsAdapter);
        this.isForced = isForced;

        searchview.setInputType(InputType.TYPE_CLASS_NUMBER);

        popupWindowFaults = new PopupWindow(popupViewFaults, width, height, true);
        popupWindowFaults.setElevation(20);

        if (isForced) {
            setForcedPopUpSettings();
            setForcedPopUpViews();
        }

        popupWindowFaults.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.END
                , 0, 0);

        initListenerComponents(faultsAdapter);
    }

    @Override
    public void initListenerComponents(FaultAdapter faultsAdapter) {

        searchview.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                faultsAdapter.getFilter().filter(s);
                return true;
            }
        });

        searchview.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchview.setQuery("", false);
            }
        });

        searchview.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                searchview.clearFocus();
            }
        });

        searchview.setOnCloseListener(new OnCloseListener() {
            @Override
            public boolean onClose() {
                searchview.setQuery("", false);
                faultsAdapter.getFilter().filter("");

                return false;
            }
        });

        faultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (!isForced) {
                    panelPresenter.postProductionStatus(Integer.parseInt(faultsAdapter.getFaultId(i)), faultsAdapter.getItem(i), Constants.STATUS_FAULT_CODE);
                }else{
                    panelPresenter.postProductionStatus(Integer.parseInt(faultsAdapter.getFaultId(i)), faultsAdapter.getItem(i), Constants.STATUS_FORCED_FAULT_CODE);
                }
//                ctimer.cancel();
                searchview.setQuery("", false);

            }
        });

        // dismiss the popup window when touched
        popupViewFaults.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                popupWindowStop.dismiss();
                return true;
            }
        });
    }

    @Override
    public void hidePopUp() {
        popupWindowFaults.dismiss();
    }

    public PopupWindow getPopUWindowInstance(){
        return popupWindowFaults;
    }

    void setForcedPopUpSettings() {
        panelView.hidePreviousTicketsPopup();
        popupWindowFaults.setOutsideTouchable(false);
        popupWindowFaults.setTouchable(true);
        popupWindowFaults.setTouchInterceptor(customPopUpTouchListenr);
    }

    void setForcedPopUpViews() {
        tvFaultHeader.setTextColor(context.getResources().getColor(R.color.red));
        tvFaultHeader.setText("Zorunlu duruş giriniz.");
    }

    View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getX() < 0 || motionEvent.getX() > 800) return true;
            if (motionEvent.getY() < 0 || motionEvent.getY() > 740) return true;

            return false;
        }

    };
}
