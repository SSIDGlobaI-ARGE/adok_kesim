package com.ardayucesan.adok_kesim.ui.controlpanel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.common.Constants;
import com.ardayucesan.adok_kesim.data.network.model.BarcodeHolder;
import com.ardayucesan.adok_kesim.data.network.model.fault.Fault;
import com.ardayucesan.adok_kesim.data.network.model.user.Operator;
import com.ardayucesan.adok_kesim.data.network.model.workorder.WorkHolder;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.FaultAdapter;
import com.ardayucesan.adok_kesim.ui.controlpanel.adapters.TicketAdapter;
import com.ardayucesan.adok_kesim.ui.controlpanel.dialog_alerts.ProgressDialog;
import com.ardayucesan.adok_kesim.ui.controlpanel.dialog_alerts.StandartDialog;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.PopupFault;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.OrderPopupNew;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.PasswordPopup;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.PopupPrevBarcode;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.PopupPrint;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.PopupPrintPreview;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.PopupQuality;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.PopupStop;

import java.util.ArrayList;
import java.util.List;

public class PanelActivity extends AppCompatActivity implements _PanelContract.View {

    public static final String TAG = "__PanelActivity";
    PanelPresenter panelPresenter;
    //activity header
    ConstraintLayout header;
    //order data views
    TextView tvProductName;
    TextView tvCustomerName;
    TextView tvWorkOrderNo;
    TextView tvOrderNumber;
    TextView tv1K;
    TextView tv2K;
    TextView tvWorkOrderQuantity;
    TextView tvCutQuantity;
    TextView tvDeadline;
    View divider;
    //sensor data views
    TextView tvEncoder;
    //Machine views
    TextView tvMachineName;
    TextView tvMachineStatus;
    TextView tvUserName;
    //buttons
    Button btnDelete;
//    Button btnPreviousTicket;
    Button btnStop;
    Button btnFinish;
    Button btnSaveMemory;
    Button btnStart;
    Button btnWorkOrder;

    Button btnPrintMemory;
    ImageView imgButtonMenu;
    //progress indicator
    ProgressBar progressBar;
    //fault adapters -> for FaultsListView
    FaultAdapter faultsAdapter;
    //Ticket adapter -> for Ticket List Views
    TicketAdapter ticketAdapter;
    //popup instances
    PopupStop popupStop;
    PasswordPopup passwordPopup;
    OrderPopupNew orderPopup;
    PopupQuality popupQuality;
    PopupPrintPreview popupPrintPreview;
    PopupFault popupFault;

    PopupPrint popupPrint;

    PopupPrevBarcode popupPrevBarcode;
    //custom AlertDialog class
    StandartDialog standartDialog;
    ProgressDialog progressDialog;

    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;
    Integer notificationID = 100;
    private final String CHANNEL_ID = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        panelPresenter = new PanelPresenter(this, this);

        panelPresenter.start();

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteQuantityClick();
            }
        });

        btnFinish.setOnClickListener(view -> {
                    onUserClicked();
                }
        );

//        btnPreviousTicket.setOnClickListener(view -> onPreviousTicketsClick());

        btnStop.setOnClickListener(view -> onStopClick());

        btnSaveMemory.setOnClickListener(view -> onMemorySaveClick());

        btnPrintMemory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onPrintMemoryClick();
            }
        });

        btnStart.setOnClickListener(view -> onStartClick());

        btnWorkOrder.setOnClickListener(view -> {
            onWorkOrderClick();
        });

        imgButtonMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordPopup.showPopUp();
            }
        });
    }

    @Override
    public void init() {
        //order views
        tvWorkOrderNo = findViewById(R.id.tvWorkOrderCode);
        tvProductName = findViewById(R.id.tv_urun_adi);
        tvCustomerName = findViewById(R.id.tv_musteri_adi);
        tvDeadline = findViewById(R.id.tv_son_tarih);
        tvOrderNumber = findViewById(R.id.tv_siparis_no);
        tv1K = findViewById(R.id.tv_1k);
        tv2K = findViewById(R.id.tv_2k);
        tvWorkOrderQuantity = findViewById(R.id.tv_is_emri_miktari);
        tvCutQuantity = findViewById(R.id.tv_kesim_miktari);
        //machine views
        tvMachineName = findViewById(R.id.tvMachine);
        tvMachineStatus = findViewById(R.id.tv_status);
        tvUserName = findViewById(R.id.tv_user_name);
        //sensor views
        tvEncoder = findViewById(R.id.tv_encoder);
        //buttons
        btnSaveMemory = findViewById(R.id.btn_top_bitir);
        btnPrintMemory = findViewById(R.id.btn_etiket_yazdir);
//        btnPreviousTicket = findViewById(R.id.btn_eski_etiket);
        btnDelete = findViewById(R.id.btn_sil);
        btnStop = findViewById(R.id.btn_ariza);
        btnFinish = findViewById(R.id.btn_bitir);
        btnStart = findViewById(R.id.btn_calistir);
        btnWorkOrder = findViewById(R.id.btn_is_emri);
        imgButtonMenu = findViewById(R.id.imgMenu);
        divider = findViewById(R.id.divDescription);
        //progress indicator
        progressBar = findViewById(R.id.panelProgressBar);
        //activity header
        header = findViewById(R.id.navbar);

        tvEncoder.setClickable(false);

        initViewDependencies();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "permch";
            String description = "channel that stays permanent";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void updateQuantity(float length) {
        tvEncoder.setText(String.format("%.2f", length / 100) + " m");
    }

    @Override
    public void updateTotalQuantity(String totalLength) {
        tvCutQuantity.setText(totalLength);
//        tvTotalLength.setText(String.format("%.0f", totalLength / 100) + " m");
    }

    @Override
    public void onDeleteQuantityClick() {
        panelPresenter.clearLength();
    }

    @Override
    public void onQuitClick() {
//        panelPresenter.postQuitData();
    }

    @Override
    public void onMemorySaveClick() {
//        panelPresenter.postQuantityAndPrint(false);
        popupQuality.showPopUp();
    }

    @Override
    public void onPreviousTicketsClick() {
        panelPresenter.fetchPreviousBarcodesList();
    }

    @Override
    public void hideStopPopup() {
        popupStop.hidePopup();
    }

    @Override
    public void showPreviousTicketsPopup(ArrayList<BarcodeHolder> barcodeList) {
        popupPrevBarcode.showPopUp(barcodeList);
    }

    @Override
    public void hidePreviousTicketsPopup() {
        popupPrevBarcode.hidePopup();
    }

    @Override
    public void onStopClick() {
        panelPresenter.fetchStopReasons(false);
    }

    @Override
    public void onStartClick() {
        panelPresenter.postProductionStatus("0", "",  Constants.STATUS_ACTIVE_CODE);
    }

    @Override
    public void onWorkOrderStartClick() {
        panelPresenter.getWorkOrders();
    }

    @Override
    public void onPrintMemoryClick() {
        popupPrint.showPopUp();
    }

    @Override
    public void onWorkOrderClick() {

        if (btnWorkOrder.getText().equals(getString(R.string.emri_ba_lat))) {
            onWorkOrderStartClick();
        } else {
            onWorkOrderFinishClick();
        }
    }

    @Override
    public void onWorkOrderFinishClick() {
        panelPresenter.finishWorkOrder();
//        panelPresenter.postQuitData();
    }

    public void onUserClicked() {
        panelPresenter.startUserPopup();
    }

    private void startNotification() {
//        Log.d(TAG, "startNotification: here");
        Intent intent = new Intent(this, PanelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.mach_head)
                .setContentTitle("MachBEE Çalışıyor")
                .setContentText("Uygulamaya gitmek için tıklayın")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public void showPopupStop(ArrayList<Fault> stopList) {

        popupStop.showPopUp(stopList);
//        ArrayList<String> stopDescriptions = (ArrayList<String>) faults.stream()
//                .map(Fault::getFaultDesc)
//                .collect(Collectors.toList());
//
//        faultsAdapter = new FaultAdapter(this, R.layout.order_item, stopDescriptions, faults);
    }

    @Override
    public void loadPreviousTicketList(List<String> tickets) {
        Log.d(TAG, "loadPreviousTicketList: ticket list : " + tickets);
        ticketAdapter = new TicketAdapter(this, R.layout.stop_item, new ArrayList<String>(tickets));
    }

    @Override
    public void showDialogAlert(String message) {
        standartDialog.showDialog(message);
    }

    @Override
    public void showOrderPopup(Operator user, ArrayList<WorkHolder> orderList) {
        orderPopup.showPopUp(user, orderList);
    }

    @Override
    public void showBarcodePreviewPopup(String barcode) {
        popupPrintPreview.showPopUp(barcode);
    }

    @Override
    public void update1K(float value) {
        tv1K.setText(String.format("%.2f", value / 100) + " m");
    }

    @Override
    public void update2K(float value) {
        tv2K.setText(String.format("%.2f", value / 100) + " m");
    }

    @Override
    public void showProductFaultPopup(ArrayList<Fault> faultList, String quality) {
        popupFault.showPopUp(faultList, quality);
    }

    @Override
    public void hideOrderPopup() {
        orderPopup.hidePopup();
    }

    @Override
    public void hideQualityPopup() {
        popupQuality.hidePopup();
    }

    @Override
    public void hidePreviewPopup() {
        popupPrintPreview.hidePopup();
    }

    @Override
    public void hideProductFaultPopup() {
        popupFault.hidePopup();
    }

    @Override
    public void hidePopupPrint() {
        popupPrint.hidePopup();
    }

    @Override
    public void hidePreviousBarcodePopup() {

    }

    @Override
    public void updateViewStop(String stopReason) {
        tvMachineStatus.setTextColor(getColor(R.color.red));
        tvMachineStatus.setText(stopReason);
//        header.setBackground(getResources().getDrawable(R.drawable.nav_shadow_stop));
        hideUiElements();
        if (!stopReason.equals("Zorunlu Duruş")) {
            popupStop.hidePopup();
        }
    }

    @Override
    public void updateViewActive(String activeName) {
        tvMachineStatus.setTextColor(getColor(R.color.green));
        tvMachineStatus.setText(activeName);
        showUiElements();
    }

    @Override
    public void updateViewFinish() {
        tvMachineStatus.setTextColor(getColor(R.color.red));
        tvMachineStatus.setText("İş Emri Yok !");
    }

    void hideUiElements() {
        btnStop.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnSaveMemory.setVisibility(View.GONE);
//        btnPreviousTicket.setVisibility(View.GONE);
        btnPrintMemory.setVisibility(View.GONE);
        btnWorkOrder.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
    }

    void showUiElements() {

        btnDelete.setVisibility(View.VISIBLE);
        btnSaveMemory.setVisibility(View.VISIBLE);
//        btnPreviousTicket.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
        btnWorkOrder.setVisibility(View.VISIBLE);
        btnPrintMemory.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.GONE);
    }

//    @Override
//    public void setViews(String machineName, String userName, String productName, String colorCode, String stretchPay, String workOrderNo, String workOrderQty, String cutQty) {
//        //order views
//        tvProductName.setText(productName);
//        tvColorCode.setText(colorCode);
//        tvWorkOrderNo.setText(workOrderNo);
//        tvWorkOrderQuantity.setText(workOrderQty);
//        tvCutQuantity.setText(cutQty);
//        //machine views
//        tvMachineName.setText(machineName);
//        tvMachineStatus.setText(Constants.STATUS_ACTIVE_TEXT);
//        tvUserName.setText(userName);
//    }

    @Override
    public void changeOrderButtonText(Boolean hasOrder) {
        if (hasOrder) {
            btnWorkOrder.setText(R.string.emri_bitir);
        } else {
            btnWorkOrder.setText(R.string.emri_ba_lat);
        }
    }

    @Override
    public void setMachineName(String machineName) {

        tvMachineName.setText(machineName);
    }

    @Override
    public void clearOrderViews() {
        tvWorkOrderNo.setText("-");
        tvProductName.setText("-");
        tvCustomerName.setText("-");
        tvDeadline.setText("-");
        tvOrderNumber.setText("-");
        tvWorkOrderQuantity.setText("-");
        tvCutQuantity.setText("-");
        tv1K.setText("-");
        tv2K.setText("-");

//        divider.setVisibility(View.GONE);
    }

    @Override
    public void clearUserName() {
        tvUserName.setTextColor(getColor(R.color.red));
        tvUserName.setText("Kullanıcı yok");
    }

    @Override
    public void clearWorkorder() {

        tvMachineStatus.setTextColor(getColor(R.color.red));
        tvMachineStatus.setText("İş Emri Yok");
    }

    @Override
    public void setUserName(String userName) {
        tvUserName.setTextColor(getColor(R.color.black));
        tvUserName.setText(userName);
    }

    @Override
    public void setWorkOrderStatus(String workStatus) {
        if (workStatus.equals(Constants.STATUS_ACTIVE_TEXT)) {
            tvMachineStatus.setTextColor(getColor(R.color.green));
        }
        tvMachineStatus.setText(workStatus);
    }

    @Override
    public void updateViews(String productName, String customerName, String deadline, String orderNumber, String workOrderQTY, String totalCutQTY, String description, String kQuantity, String kkQuantity,String workOrderCode) {
        tvWorkOrderNo.setText(workOrderCode);
        tvProductName.setText(productName);
        tvCustomerName.setText(customerName);
        tvDeadline.setText(deadline);
        tvOrderNumber.setText(orderNumber);
        tv1K.setText(kQuantity);
        tv2K.setText(kkQuantity);
        tvWorkOrderQuantity.setText(workOrderQTY);
        tvCutQuantity.setText(totalCutQTY);
    }

    @Override
    public void showErrorToast(String header, String description) {
        // Get your custom_toast.xml ayout
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.error_toast,
                (ViewGroup) findViewById(R.id.error_toast_id));

        // set a message
        TextView tvHeader = (TextView) layout.findViewById(R.id.tvHeader);
        TextView tvDescription = (TextView) layout.findViewById(R.id.tvDescription);
        tvHeader.setText(header);
        tvDescription.setText(description);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void showSuccessToast(String header, String description) {
        // Get your custom_toast.xml ayout
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.success_toast,
                (ViewGroup) findViewById(R.id.success_toast_id));

        // set a message
        TextView tvHeader = (TextView) layout.findViewById(R.id.tvHeader);
        TextView tvDescription = (TextView) layout.findViewById(R.id.tvDescription);
        tvHeader.setText(header);
        tvDescription.setText(description);

        // Toast...
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    void initViewDependencies() {
        popupStop = new PopupStop(this, panelPresenter);

        standartDialog = new StandartDialog(this);

        progressDialog = new ProgressDialog(this);

        passwordPopup = new PasswordPopup(this, this);

        orderPopup = new OrderPopupNew(this, panelPresenter);

        popupQuality = new PopupQuality(this, panelPresenter);

        popupPrint = new PopupPrint(this,panelPresenter);

        popupPrintPreview = new PopupPrintPreview(this, panelPresenter);

        popupFault = new PopupFault(this, panelPresenter);

        popupPrevBarcode = new PopupPrevBarcode(this,panelPresenter);
    }

    @Override
    public void onBackPressed() {

    }

}