package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.data.network.model.BarcodeHolder;
import com.ardayucesan.adok_kesim.data.network.model.user.Operator;
import com.ardayucesan.adok_kesim.data.network.model.workorder.WorkHolder;
import com.ardayucesan.adok_kesim.data.network.repository.IPanelRepository;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelActivity;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.BarcodeListAdapter;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.OrderListAdapter;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.util.ArrayList;

public class PopupPrevBarcode {

    private static final int width = 1200;
    private static final int height = 700;
    private static final String TAG = "__OrderPopup" ;
    //components
    private PopupWindow popupWindow;
    private View popupView;
    private RecyclerView rvOrder;
//    private SearchView searchView;
    private ProgressBar pbar;
    private ImageView btnQuitOrder;

    private final Context context;
    private final PanelPresenter presenter;
    private IPanelRepository panelRepository;

    private Operator user;
    private ArrayList<BarcodeHolder> barcodeList = new ArrayList<>();
    private BarcodeListAdapter barcodeListAdapter;

    public PopupPrevBarcode(Context context, PanelPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void showPopUp(ArrayList<BarcodeHolder> barcodeList) {
        this.barcodeList = barcodeList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_order, null);

        rvOrder = popupView.findViewById(R.id.rvOrder);
        pbar = popupView.findViewById(R.id.progressBarOrder);
        btnQuitOrder = popupView.findViewById(R.id.btnQuitOrder);

        rvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        popupWindow = new PopupWindow(popupView, width, height, true);
        barcodeListAdapter = new BarcodeListAdapter(context,presenter,barcodeList,popupWindow);
        rvOrder.setItemViewCacheSize(barcodeList.size());
        rvOrder.setAdapter(barcodeListAdapter);
        if (rvOrder.getItemDecorationCount() > 0) {
            rvOrder.removeItemDecorationAt(0);
        }
        rvOrder.addItemDecoration(new LayoutMarginDecoration(1, 10));

        popupWindow.setElevation(20);

        outsideClickableSettings();

        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        btnQuitOrder.setOnClickListener(view -> hidePopup());

    }

    public void hidePopup() {
        hideKeyboard();
        popupWindow.dismiss();
    }

    public void outsideClickableSettings() {
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(customPopUpTouchListenr);
    }

    View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getX() < 0 || motionEvent.getX() > width) return true;
            if (motionEvent.getY() < 0 || motionEvent.getY() > height) return true;

            return false;
        }

    };

//    public void initSearchView() {
//
//        searchView.setOnQueryTextListener(new OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                orderAdapter.getFilter().filter(s);
//                return true;
//            }
//        });
//
//        searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                searchView.setQuery("", false);
//            }
//        });
//
//        searchView.setOnSearchClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                searchView.clearFocus();
//            }
//        });
//
//        searchView.setOnCloseListener(new OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                searchView.setQuery("", false);
//                orderAdapter.getFilter().filter("");
//                hideKeyboard();
//                return false;
//            }
//        });
//
////        lvOrder.setOnClickListener(new );
////        searchView.requestFocus();
//    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(popupView.getWindowToken(), 0);
    }

    public void showProgress() {
        pbar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        pbar.setVisibility(View.GONE);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void clearOrderCode() {
//        searchView.setQuery("",false);
    }

    private PanelActivity getActivity() {
        return ((PanelActivity) context);
    }
}
