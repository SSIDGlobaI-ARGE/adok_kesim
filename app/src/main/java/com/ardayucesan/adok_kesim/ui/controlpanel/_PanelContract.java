package com.ardayucesan.adok_kesim.ui.controlpanel;

import com.ardayucesan.adok_kesim.data.network.model.BarcodeHolder;
import com.ardayucesan.adok_kesim.data.network.model.fault.Fault;
import com.ardayucesan.adok_kesim.data.network.model.ticket.single_ticket_data.Ticket;
import com.ardayucesan.adok_kesim.data.network.model.user.Operator;
import com.ardayucesan.adok_kesim.data.network.model.workorder.Order;
import com.ardayucesan.adok_kesim.data.network.model.workorder.WorkHolder;

import java.util.ArrayList;
import java.util.List;

public interface _PanelContract {

    interface View {
        void init();

        void showProgress();

        void hideProgress();

        void showError();

        void updateQuantity(float length);

        void updateTotalQuantity(String totalLength);

        void onQuitClick();

        void onMemorySaveClick();

        void onPreviousTicketsClick();

        void onDeleteQuantityClick();

        void onStopClick();

        void onStartClick();

        void onWorkOrderStartClick();

        void onPrintMemoryClick();

        void onWorkOrderClick();

        void onWorkOrderFinishClick();

        void showPopupStop(ArrayList<Fault> faults);

        void loadPreviousTicketList(List<String> tickets);

        void hideStopPopup();

        void showPreviousTicketsPopup(ArrayList<BarcodeHolder> barcodeList);

        void hidePreviousTicketsPopup();

        void showDialogAlert(String message);

        void updateViewStop(String faultName);

        void updateViewActive(String activeName);

        void updateViewFinish();

        void changeOrderButtonText(Boolean hasOrder);

        void setMachineName(String machineName);

        void clearOrderViews();

        void clearUserName();

        void clearWorkorder();

        void setUserName(String userName);

        void setWorkOrderStatus(String workOrderStatus);
        void updateViews(String productName, String customerName, String deadline,String orderNumber, String workOrderQTY, String totalCutQTY,String description,String kQuantity,String kkQuantity,String workOrderCode);
        void showErrorToast(String header, String description);
        void showSuccessToast(String header, String description);

        void showOrderPopup(Operator user , ArrayList<WorkHolder> orderList);

        void showBarcodePreviewPopup(String barcode);

        void update1K(float value);

        void update2K(float value);

        void showProductFaultPopup(ArrayList<Fault> faultList,String quality);

        void hideOrderPopup();

        void hideQualityPopup();

        void hidePreviewPopup();

        void hideProductFaultPopup();

        void hidePopupPrint();

        void hidePreviousBarcodePopup();
    }

    interface Presenter {
        void start();

        void quitFromPanel();

        void postDeleteQuantity();

        void simpleDeleteQuantitynTotalLength();

        void fetchPreviousBarcodesList();

        void fetchStopReasons(Boolean isForced);

        void printTicket(Ticket ticket);

        void postQuitData();

        void postProductionStatus(String stopcode, String faultName, String status);

//        void postQuantityAndPrint(boolean shiftMode);

//        void setViews();

        void setUser(Operator user);

        void setOrder(Order order);

        void startOrderPopup();

        void startUserPopup();

        void clearOrder();

        void clearUser();

        void clearTotalLength();

        void clearQuantity(String quality);
    }
}
