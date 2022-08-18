package com.ardayucesan.gezderi_uretim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;

import com.ardayucesan.gezderi_uretim.R;
import com.ardayucesan.gezderi_uretim.ui.controlpanel._PanelContract;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.adapters.TicketAdapter;

@SuppressWarnings("FieldCanBeLocal")
public class PreviousTicketPopUp implements _BasePopUp.TicketPopUp {

    private static final String TAG = "__PreviousTicketPopUp";
    private static final int width = 600;
    private static final int height = 750;
    private ListView ticketsListView;
    private PopupWindow popupWindowPrevTicket;
    private SearchView searchview;
    private View popupViewTickets;
    //construct params
    private final Context context;
    private _PanelContract.Presenter panelPresenter;

    public PreviousTicketPopUp(Context context, _PanelContract.Presenter panelPresenter) {
        this.context = context;
        this.panelPresenter = panelPresenter;
    }

    @Override
    public void showPopUp(TicketAdapter ticketsAdapter) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupViewTickets = inflater.inflate(R.layout.popup_prevticket, null);

        searchview = popupViewTickets.findViewById(R.id.searchView);
        ticketsListView = popupViewTickets.findViewById(R.id.barcode_view);
        ticketsListView.setAdapter(ticketsAdapter);
        searchview.setInputType(InputType.TYPE_CLASS_NUMBER);

        popupWindowPrevTicket = new PopupWindow(popupViewTickets, width, height, true);
        popupWindowPrevTicket.setElevation(20);

        popupWindowPrevTicket.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.END, 0, 0);

        initListenerComponents(ticketsAdapter);
    }

    @Override
    public void initListenerComponents(TicketAdapter ticketsAdapter) {
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ticketsAdapter.getFilter().filter(s);
                return false;
            }
        });

        searchview.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchview.setQuery("", false);

            }
        });

        searchview.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchview.clearFocus();
            }
        });

        ticketsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                searchview.setQuery("", false);

                panelPresenter.fetchTicketData(ticketsAdapter.getItem(i));
            }
        });

        popupViewTickets.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });
    }

    @Override
    public void hidePopUp() {

    }

}
