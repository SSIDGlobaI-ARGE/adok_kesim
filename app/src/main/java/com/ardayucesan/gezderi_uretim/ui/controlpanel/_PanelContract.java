package com.ardayucesan.gezderi_uretim.ui.controlpanel;

import com.ardayucesan.gezderi_uretim.data.network.model.fault.Fault;
import com.ardayucesan.gezderi_uretim.data.network.model.ticket.single_ticket_data.Ticket;
import com.ardayucesan.gezderi_uretim.data.network.model.user.Operator;
import com.ardayucesan.gezderi_uretim.data.network.model.workorder.Order;

import java.util.List;

public interface _PanelContract {

    interface View {
        void init();

        void showProgress();

        void hideProgress();

        void showError();

        void updateQuantity(float length);

        void updateTotalQuantity(float totalLength);

        void onQuitClick();

        void onRollFinishClick();

        void onPreviousTicketsClick();

        void onDeleteQuantityClick();

        void onFaultClick();

        void onStartClick();

        void onWorkOrderStartClick();

        void onWorkOrderClick();

        void onWorkOrderFinishClick();

        void loadFaultList(List<Fault> faults);

        void loadPreviousTicketList(List<String> tickets);

        void showFaultsPopup(Boolean isForced);

        void hideFaultsPopup();

        void showPreviousTicketsPopup();

        void hidePreviousTicketsPopup();

        void showDialogAlert(String message);

        void updateViewFault(String faultName);

        void updateViewActive(String activeName);

        void changeOrderButtonText(Boolean hasOrder);

        void setMachineName(String machineName);

        void clearOrderViews();

        void clearUserName();

        void clearWorkorder();

        void setSpeed(float speed);

        void setUserName(String userName);

        void setWorkOrderStatus(String workOrderStatus);

        void updateViews(String productName,String productColorCode,String workOrderNo,String workOrderQTY,String totalCutQTY,String speed,String  thickness, String weight,String description);

        void showErrorToast(String header, String description);

        void setTotalCut(String totalCut);

        void showSuccessToast(String header, String description);


    }

    interface Presenter {
        void start();

        void quitFromPanel();

        void postDeleteQuantity();

        void simpleDeleteQuantitynTotalLength();

        void fetchPreviousBarcodesList();

        void fetchFaults(Boolean isForced);

        void printTicket(Ticket ticket);

        void fetchTicketData(String barcode);

        void getMachineName();

        void postQuitData();

        void postProductionStatus(int faultId, String faultName, String status);

        void postQuantityAndPrint(boolean shiftMode);

//        void setViews();

        void setUser(Operator user);

        void setOrder(Order order);

        void startOrderPopup();

        void startUserPopup();

        void clearOrder();

        void clearUser();

        void clearTotalLength();
    }
}
