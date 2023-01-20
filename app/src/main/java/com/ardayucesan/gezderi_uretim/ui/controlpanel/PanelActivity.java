package com.ardayucesan.gezderi_uretim.ui.controlpanel;

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

import com.ardayucesan.gezderi_uretim.R;
import com.ardayucesan.gezderi_uretim.common.Constants;
import com.ardayucesan.gezderi_uretim.data.network.model.fault.Fault;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.adapters.FaultAdapter;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.adapters.TicketAdapter;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.dialog_alerts.ProgressDialog;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.dialog_alerts.StandartDialog;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.popups.FaultsPopUp;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.popups.PasswordPopup;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.popups.PreviousTicketPopUp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PanelActivity extends AppCompatActivity implements _PanelContract.View {

    public static final String TAG = "__PanelActivity";

    //presenter for this activity
    PanelPresenter panelPresenter;
    //activity header
    ConstraintLayout header;
    //order data views
    TextView tvProductName;
    TextView tvColorCode;
    TextView tvWorkOrderNo;
    TextView tvWorkOrderQuantity;
    TextView tvCutQuantity;
    TextView tvOrderSpeed;
    TextView tvThickness;
    TextView tvWeight;
    TextView tvDesc;
    View divider;
    //sensor data views
    TextView tvSpeed;
    TextView tvEncoder;
    TextView tvTotalLength;
    //Machine views
    TextView tvMachineName;
    TextView tvMachineStatus;
    TextView tvUserName;
    //buttons
    Button btnDelete;
    Button btnPreviousTicket;
    Button btnFault;
    Button btnFinish;
    Button btnFinishRoll;
    Button btnStart;
    Button btnWorkOrder;
    ImageView imgButtonMenu;
    //progress indicator
    ProgressBar progressBar;
    //fault adapters -> for FaultsListView
    FaultAdapter faultsAdapter;
    //Ticket adapter -> for Ticket List Views
    TicketAdapter ticketAdapter;
    //popup instances
    FaultsPopUp faultsPopUp;
    PreviousTicketPopUp ticketsPopUp;
    PasswordPopup passwordPopup;
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

        btnPreviousTicket.setOnClickListener(view -> onPreviousTicketsClick());

        btnFault.setOnClickListener(view -> onFaultClick());

        btnFinishRoll.setOnClickListener(view -> onRollFinishClick());

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

//        createNotificationChannel();

        Log.d(TAG, "onCreate: im here");
//        startNotification();

    }

    @Override
    public void init() {
        //order views
        tvProductName = findViewById(R.id.tv_urun_adi);
        tvColorCode = findViewById(R.id.tv_renk_kodu);
        tvWorkOrderNo = findViewById(R.id.tv_is_emri_no);
        tvWorkOrderQuantity = findViewById(R.id.tv_is_emri_miktari);
        tvCutQuantity = findViewById(R.id.tv_kesim_miktari);
        tvThickness = findViewById(R.id.tvKalınlık);
        tvWeight = findViewById(R.id.tvGramaj);
        tvDesc = findViewById(R.id.tvDesc);
        //machine views
        tvMachineName = findViewById(R.id.tvMachine);
        tvMachineStatus = findViewById(R.id.tv_status);
        tvUserName = findViewById(R.id.tv_user_name);
        //sensor views
        tvEncoder = findViewById(R.id.tv_encoder);
        tvTotalLength = findViewById(R.id.tvTotalLength);
        tvSpeed = findViewById(R.id.tv_speed);
        tvOrderSpeed = findViewById(R.id.tvOrderSpeed);
        //buttons
        btnFinishRoll = findViewById(R.id.btn_top_bitir);
        btnPreviousTicket = findViewById(R.id.btn_etiket);
        btnDelete = findViewById(R.id.btn_sil);
        btnFault = findViewById(R.id.btn_ariza);
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
        tvEncoder.setText(String.format("%.0f", length / 100) + " m");

    }

    @Override
    public void updateTotalQuantity(float totalLength) {
        tvTotalLength.setText(String.format("%.0f", totalLength / 100) + " m");
    }

    @Override
    public void onDeleteQuantityClick() {
        panelPresenter.simpleDeleteQuantitynTotalLength();
    }

    @Override
    public void onQuitClick() {
        panelPresenter.postQuitData();
    }

    @Override
    public void onRollFinishClick() {
        panelPresenter.postQuantityAndPrint(false);
    }

    @Override
    public void onPreviousTicketsClick() {
        panelPresenter.fetchPreviousBarcodesList();
    }

    @Override
    public void showFaultsPopup(Boolean isForced) {
        if (faultsPopUp.getPopUWindowInstance() != null) {
            faultsPopUp.getPopUWindowInstance().dismiss();
        }
        faultsPopUp.showPopUp(faultsAdapter, isForced);
    }

    @Override
    public void hideFaultsPopup() {
        faultsPopUp.hidePopUp();
    }

    @Override
    public void showPreviousTicketsPopup() {
        ticketsPopUp.showPopUp(ticketAdapter);
    }

    @Override
    public void hidePreviousTicketsPopup() {
        ticketsPopUp.hidePopUp();
    }

    @Override
    public void onFaultClick() {
        panelPresenter.fetchFaults(false);
    }

    @Override
    public void onStartClick() {
        panelPresenter.postProductionStatus(0, "", Constants.STATUS_ACTIVE_CODE);
    }

    @Override
    public void onWorkOrderStartClick() {
        panelPresenter.startOrderPopup();
    }

    @Override
    public void onWorkOrderClick() {

        if (btnWorkOrder.getText().equals(getString(R.string.emri_ba_lat))) {
            onWorkOrderStartClick();
//            btnWorkOrder.setText(R.string.emri_bitir);
        } else {
            onWorkOrderFinishClick();
//            btnWorkOrder.setText(R.string.emri_ba_lat);
        }
    }

    @Override
    public void onWorkOrderFinishClick() {
        panelPresenter.clearTotalLength();
        panelPresenter.postQuitData();
    }

    public void onUserClicked() {
        panelPresenter.startUserPopup();
    }

    private void startNotification() {
        // Sets an ID for the notification
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

    @Override
    public void loadFaultList(List<Fault> faults) {

        ArrayList<String> faultDescriptions = (ArrayList<String>) faults.stream()
                .map(Fault::getFaultDesc)
                .collect(Collectors.toList());

        faultsAdapter = new FaultAdapter(this, R.layout.faultcode_item, faultDescriptions, faults);
    }

    @Override
    public void loadPreviousTicketList(List<String> tickets) {
        Log.d(TAG, "loadPreviousTicketList: ticket list : " + tickets);
        ticketAdapter = new TicketAdapter(this, R.layout.faultcode_item, new ArrayList<String>(tickets));
    }

    @Override
    public void showDialogAlert(String message) {
        standartDialog.showDialog(message);
    }

    @Override
    public void updateViewFault(String faultName) {
        tvMachineStatus.setTextColor(getColor(R.color.red));
        tvMachineStatus.setText(faultName);
//        header.setBackground(getResources().getDrawable(R.drawable.nav_shadow_stop));
        hideUiElements();
        if (!faultName.equals("Zorunlu Duruş")) {
            faultsPopUp.hidePopUp();
        }
    }

    @Override
    public void updateViewActive(String activeName) {
        tvMachineStatus.setTextColor(getColor(R.color.green));
        tvMachineStatus.setText(activeName);
        showUiElements();
    }

    void hideUiElements() {
        btnFault.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnFinishRoll.setVisibility(View.GONE);
        btnPreviousTicket.setVisibility(View.GONE);
        btnWorkOrder.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
    }

    void showUiElements() {

        btnDelete.setVisibility(View.VISIBLE);
        btnFinishRoll.setVisibility(View.VISIBLE);
        btnPreviousTicket.setVisibility(View.VISIBLE);
        btnFault.setVisibility(View.VISIBLE);
        btnWorkOrder.setVisibility(View.VISIBLE);
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
        tvProductName.setText("-");
        tvColorCode.setText("-");
        tvWorkOrderNo.setText("-");
        tvWorkOrderQuantity.setText("-");
        tvCutQuantity.setText("-");
        tvOrderSpeed.setText("-");
        tvThickness.setText("-");
        tvWeight.setText("-");
        tvDesc.setText("");

        tvDesc.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
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
    public void setSpeed(float displacement) {
//        Log.d(TAG, "setSpeed: speed");
        tvSpeed.setText(String.format("%.0f", (displacement*60) / 100) + " m/dk");
    }

//    @Override
//    public void setSpeed(Integer displacement) {
//        tvSpeed.setText(String.format("%.2f", (displacement / 100)) + "m/sn");
////        tvEncoder.setText(String.format("%.2f", length / 100) + " m");
//    }

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
    public void updateViews(String productName, String productColorCode, String workOrderNo, String workOrderQTY, String totalCutQTY, String speed, String thickness , String weight,String description) {
        tvProductName.setText(productName);
        tvColorCode.setText(productColorCode);
        tvWorkOrderNo.setText(workOrderNo);
        tvWorkOrderQuantity.setText(workOrderQTY);
        tvCutQuantity.setText(totalCutQTY);
        tvOrderSpeed.setText(speed);
        tvThickness.setText(thickness);
        tvWeight.setText(weight);

        if(!description.equals("")){
            tvDesc.setText(description);
            tvDesc.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }else{
            tvDesc.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
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
    public void setTotalCut(String totalCut) {
        tvCutQuantity.setText(totalCut);
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
        faultsPopUp = new FaultsPopUp(this, panelPresenter, this);

        ticketsPopUp = new PreviousTicketPopUp(this, panelPresenter);

        standartDialog = new StandartDialog(this);

        progressDialog = new ProgressDialog(this);

        passwordPopup = new PasswordPopup(this, this);
    }

    @Override
    public void onBackPressed() {

    }

}