package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.data.network.model.fault.Fault;
import com.ardayucesan.adok_kesim.data.network.model.user.Operator;
import com.ardayucesan.adok_kesim.data.network.model.workorder.WorkHolder;
import com.ardayucesan.adok_kesim.data.network.repository.IPanelRepository;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelActivity;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.OrderListAdapter;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderPopupNew {

    private static final int width = 1200;
    private static final int height = 700;
    private static final String TAG = "__OrderPopup";
    //components
    private PopupWindow popupWindowOrder;
    private View popupViewOrder;
    private RecyclerView rvOrder;
    //    private SearchView searchView;
    private ProgressBar pbar;
    private ImageView btnQuitOrder;

    private final Context context;
    private final PanelPresenter presenter;
    private IPanelRepository panelRepository;

    private Operator user;
    private ArrayList<WorkHolder> productList = new ArrayList<>();
    private OrderListAdapter productListAdapter;
    private EditText etProduct;

    public OrderPopupNew(Context context, PanelPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void showPopUp(Operator user, ArrayList<WorkHolder> productList) {
        this.user = user;
        this.productList = productList;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupViewOrder = inflater.inflate(R.layout.popup_order, null);

        rvOrder = popupViewOrder.findViewById(R.id.rvOrder);
        pbar = popupViewOrder.findViewById(R.id.progressBarOrder);
        btnQuitOrder = popupViewOrder.findViewById(R.id.btnQuitOrder);
        etProduct = popupViewOrder.findViewById(R.id.searchViewProduct);
        Button btnFilter = popupViewOrder.findViewById(R.id.btnFilter);
        ImageView imgBtnClearFilter = popupViewOrder.findViewById(R.id.imgBtnClearFilter);

        rvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        productListAdapter = new OrderListAdapter(context, presenter, productList);
        rvOrder.setItemViewCacheSize(productList.size());
        rvOrder.setAdapter(productListAdapter);
        if (rvOrder.getItemDecorationCount() > 0) {
            rvOrder.removeItemDecorationAt(0);
        }
        rvOrder.addItemDecoration(new LayoutMarginDecoration(1, 10));


        popupWindowOrder = new PopupWindow(popupViewOrder, width, height, true);
        popupWindowOrder.setElevation(20);

        outsideClickableSettings();

        popupWindowOrder.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        btnQuitOrder.setOnClickListener(view -> hidePopup());

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etProduct.getText().length() >= 1) {
                    presenter.getProductList(etProduct.getText().toString());
                    hideKeyboard();
                }
            }
        });
        imgBtnClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etProduct.setText("");
                presenter.getProductList("cls");
                hideKeyboard();
            }
        });

    }

    public void hidePopup() {
        hideKeyboard();
        popupWindowOrder.dismiss();
    }

    public void outsideClickableSettings() {
        popupWindowOrder.setOutsideTouchable(false);
        popupWindowOrder.setTouchable(true);
        popupWindowOrder.setTouchInterceptor(customPopUpTouchListenr);
    }

    View.OnTouchListener customPopUpTouchListenr = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getX() < 0 || motionEvent.getX() > width) return true;
            if (motionEvent.getY() < 0 || motionEvent.getY() > height) return true;

            return false;
        }

    };

    public void updateList(ArrayList<WorkHolder> filteredList) {
        productListAdapter.filterList(filteredList);
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<WorkHolder> filteredlist = new ArrayList<WorkHolder>();

        // running a for loop to compare elements.
        for (WorkHolder item : productList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getWorkOrderName().toLowerCase().contains(text.toLowerCase())) {
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
            productListAdapter.filterList(filteredlist);
        }
    }

    public void showProductProgressBar(){
        pbar.setVisibility(View.VISIBLE);
    }
    public void hideProductProgressBar(){
        pbar.setVisibility(View.GONE);
    }


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
        imm.hideSoftInputFromWindow(popupViewOrder.getWindowToken(), 0);
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
