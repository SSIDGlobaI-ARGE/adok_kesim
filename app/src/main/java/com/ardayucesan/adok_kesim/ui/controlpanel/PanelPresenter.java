package com.ardayucesan.adok_kesim.ui.controlpanel;


import static android.util.Log.d;
import static java.util.Arrays.asList;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ardayucesan.adok_kesim.common.Constants;
import com.ardayucesan.adok_kesim.common.Helper;
import com.ardayucesan.adok_kesim.common.MyChronometer;
import com.ardayucesan.adok_kesim.common.PinHelper;
import com.ardayucesan.adok_kesim.data.network.model.BarcodeHolder;
import com.ardayucesan.adok_kesim.data.network.model.fault.Fault;
import com.ardayucesan.adok_kesim.data.network.model.ticket.single_ticket_data.Ticket;
import com.ardayucesan.adok_kesim.data.network.model.user.Operator;
import com.ardayucesan.adok_kesim.data.network.model.workorder.Order;
import com.ardayucesan.adok_kesim.data.network.model.workorder.WorkHolder;
import com.ardayucesan.adok_kesim.data.network.repository.IPanelRepository;
import com.ardayucesan.adok_kesim.ui.controlpanel.dialog_alerts.ProgressDialog;
import com.ardayucesan.adok_kesim.ui.controlpanel.popups.UserPopup;
import com.ardayucesan.adok_kesim.ui.controlpanel.printer.StandartPrintable;
import com.ardayucesan.adok_kesim.ui.controlpanel.printer.UsbHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * this class is presenter for panel activity.
 *
 * @author ardayucesan
 * @version 1.9
 */

public class PanelPresenter implements _PanelContract.Presenter {

    //TAG for log messages
    public static final String TAG = "__PanelPresenter";
    //disposableCollector for cancelling running disposables ->encoder service
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    //Current machine status
    String productionStatus;
    //instance holder of the view
    _PanelContract.View mView;
    //retrofit instance -> make it dependency injection for furthermore
    IPanelRepository iPanelRepository;
    //models
    Order order;
    Operator user;
    //encoder variables
    String response;
    String currentState;
    String command;
    String oldCommand = "";
    //encoder values
    float totalQuantity = 0;
    int stretch = 0;
    //current cut value
    float length = 0;
    //total cut value at workorder
    float totalLength = 0;
    //old length holder for current speed
    float oldLength = 0;
    float oldLengthFast = 0;
    float currentDisplacement = 0;
    ArrayList<String> shiftList = new ArrayList<>();
    SimpleDateFormat formatter = new SimpleDateFormat("hhmm");
    int retryShift = 10;
    float avgSpeedByRoll = 0;
    ArrayList<Float> speedList;
    int oldState = 0;

    MyChronometer chronometer;
    Boolean IS_PRINT;
    StandartPrintable standartPrintable;
    //Usb handler for printer device
    UsbHandler usbHandler;
    //Dialog instances
    ProgressDialog progressDialog;
    Context context;

    UserPopup userPopup;

    /**
     * c_test is a encoder library programmed by arday
     */

    static {
        System.loadLibrary("c_test");
    }

    public PanelPresenter(_PanelContract.View view, Context context) {
        this.mView = view;
        this.context = context;
        chronometer = new MyChronometer(context);

        productionStatus = Constants.STATUS_ACTIVE_CODE;

        standartPrintable = new StandartPrintable(context);
        usbHandler = new UsbHandler(context);
        progressDialog = new ProgressDialog(context);

        userPopup = new UserPopup(context, this);

        iPanelRepository = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(IPanelRepository.class);
    }

    public void start() {
        PinHelper.execCommand("echo 100 > /sys/class/gpio/export");
        PinHelper.execCommand("echo 99 > /sys/class/gpio/export");
        mView.init();
//        getMachineName();
        startEncoder();
//        counter.start();
    }

    @Override
    public void postDeleteQuantity() {

        if (userController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "quality", "value", "fault", "production_id", "barcode", "roll_speed");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), "SS", String.valueOf(length), "0", order.getProductionId(), "", String.valueOf(avgSpeedByRoll));

        iPanelRepository.postMemory(Helper.setJsonRequestBody(params, values))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
                            deleteQuantity();
                        } else {
                            progressDialog.startDialog(jsonObject.get("message").getAsString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }


    @Override
    public void simpleDeleteQuantitynTotalLength() {
        totalLength -= length;
        length = 0;
        chronometer.setMsElapsed(0);
        Log.d(TAG, "simpleDeleteQuantity: chronometer started elaptes time : " + chronometer.msElapsed);
    }

    public void deleteQuantity() {
        length = 0;
        oldLengthFast = length;
    }

    @Override
    public void fetchPreviousBarcodesList() {

        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "production_id");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), order.getProductionId());

        iPanelRepository.getPreviousTicketList(user.getPoint_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.hidePopupPrint();
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject ticketListResponse) {
                        d(TAG, "onNext: response prev ticketr : " + ticketListResponse);
                        if (ticketListResponse.get("success").getAsBoolean()) {
                            ArrayList<BarcodeHolder> barcodeList = new ArrayList<>();

                            JsonArray data = ticketListResponse.get("data").getAsJsonArray();
//                            JsonArray rows = data.get("rows").getAsJsonArray();

                            for (int i = 0; i < data.size(); i++) {

                                JsonObject barcodeJson = data.get(i).getAsJsonObject();

                                BarcodeHolder barcode = new BarcodeHolder(
                                        barcodeJson.get("barcode").getAsString(),
                                        barcodeJson.get("name").getAsString()
                                );
                                barcodeList.add(barcode);
                            }
                            mView.showPreviousTicketsPopup(barcodeList);
                            mView.hideProgress();
                        } else {//v 1.3 te yok yeni eklendi
                            mView.showErrorToast("Hata",ticketListResponse.get("message").getAsString());
                            mView.hideProgress();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();

                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }

    @Override
    public void fetchStopReasons(Boolean isForced) {

        if (!userController()) {
            return;
        }
        if (!orderController()) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "production_id");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), order.getProductionId());

        iPanelRepository.getStopList(user.getPoint_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonResponse) {
                        d(TAG, "onNext: response stop list" + jsonResponse);
                        if (jsonResponse.get("success").getAsBoolean()) {

                            ArrayList<Fault> stopReasonList = new ArrayList<>();

                            JsonObject data = jsonResponse.get("data").getAsJsonObject();
                            JsonArray rows = data.get("rows").getAsJsonArray();

                            for (int i = 0; i < data.get("count").getAsInt(); i++) {

                                JsonObject stopReasonJson = rows.get(i).getAsJsonObject();

                                Fault fault = new Fault(
                                        stopReasonJson.get("id").getAsString(),
                                        stopReasonJson.get("name").getAsString()
                                );
                                Log.d(TAG, "onNext: fault : " + fault.getFaultId());
                                stopReasonList.add(fault);
                            }

                            mView.showPopupStop(stopReasonList);
                        } else {
                            mView.showErrorToast("Hata", jsonResponse.get("message").getAsString());
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void fetchProductFaults(String quality) {

        if (!userController()) {
            return;
        }
        if (!orderController()) {
            return;
        }

//        List<String> params = Arrays.asList("mac", "user_id", "token", "production_id");
//        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), order.getProductionId());

        iPanelRepository.getFaultList(order.getProductId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonResponse) {
                        ArrayList<Fault> faultList = new ArrayList<>();

                        if (jsonResponse.get("success").getAsBoolean()) {

                            JsonObject data = jsonResponse.get("data").getAsJsonObject();
                            JsonArray rows = data.get("rows").getAsJsonArray();

                            for (int i = 0; i < data.get("count").getAsInt(); i++) {

                                JsonObject faultReasonJson = rows.get(i).getAsJsonObject();


                                Fault fault = new Fault(
                                        faultReasonJson.get("id").getAsString(),
                                        faultReasonJson.get("name").getAsString()
                                );
                                faultList.add(fault);
                            }
                            mView.showProductFaultPopup(faultList, quality);
                            mView.hideQualityPopup();
                        } else {
                            mView.showErrorToast("Hata", jsonResponse.get("message").getAsString());
                        }
                        Log.d(TAG, "onNext: here");
//                        mView.loadStopReasonList(stopReasonList);
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void printTicket(Ticket ticket) {
//        standartPrintable.PrintLotTicket(ticket);
    }

    @Override
    public void postQuitData() {
        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "production_id");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), order.getProductionId());

        JsonObject responseTest = Helper.setJsonRequestBody(params, values);
        Log.d(TAG, "testResponse fetchTicketData: " + responseTest.toString());

        iPanelRepository.postQuit(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
//                                quitFromPanel();
                            mView.showSuccessToast("İş Emri Bitirildi", "İş Emri Başarıyla Bitirildi");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                        clearOrder();
                    }
                });
    }

    public void printMemory(String quality) {

        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "point_id", "production_id", "workorder_id","quality");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), user.getPoint_id(), order.getProductionId(), order.getWorkOrderId(),quality);

        JsonObject responseTest = Helper.setJsonRequestBody(params, values);
        Log.d(TAG, "ticket response fetchTicketData: " + responseTest.toString());

        iPanelRepository.getTicketData(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject ticketResponse) {

                        d(TAG, "onNext: ticketResponse  " + ticketResponse);
                        if (ticketResponse.get("success").getAsBoolean()) {
                            mView.showSuccessToast("Barkod oluşturuldu", "");
                            mView.hidePopupPrint();
                            clearQuantity(quality);
                            mView.showBarcodePreviewPopup(ticketResponse.get("data").getAsJsonObject().get("rows").getAsJsonObject().get("barcode").getAsString());
                        } else {
                            mView.showErrorToast("Hata",ticketResponse.get("message").getAsString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: error occurred in fetchSingleTicketData" + e.toString());
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();

                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }

    public void saveToMemory(String quality, String faultCode) {
        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "point_id", "production_id", "value", "quality", "faultcode");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), user.getPoint_id(), order.getProductionId(), String.valueOf(length), quality, faultCode);

        d(TAG, "saveToMemory: memory object : " + Helper.setJsonRequestBody(params, values));

        iPanelRepository.postMemory(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        d(TAG, "onNext: respons memory save : " + jsonObject);
                        if (jsonObject.get("success").getAsBoolean()) {
                            mView.updateTotalQuantity(jsonObject.get("data").getAsJsonObject().get("kesim_miktari").getAsString());

                            mView.hideQualityPopup();
                            if (quality.equals("1K")) {
                                order.setQuantity1K(order.getQuantity1K() + length);
                                mView.update1K(order.getQuantity1K());
                                clearLength();
                            }
                            if (quality.equals("2K")) {
                                mView.hideProductFaultPopup();
                                order.setQuantity2K(order.getQuantity2K() + length);
                                mView.update2K(order.getQuantity2K());
                                clearLength();
                            }
                            mView.showSuccessToast("İşlem Başarılı", "Hafızaya alındı.");
                        } else {
//                            progressDialog.startDialog(jsonObject.get("message").getAsString());
                            mView.showErrorToast("İşlem Başarısız", jsonObject.get("message").getAsString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: error ocurred in postquantity" + e.getMessage());
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }

    public void clearLength() {
        length = 0;
        mView.updateQuantity(length);
    }

    @Override
    public void postProductionStatus(String stopcode, String stopName, String status) {
        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "stopcode", "status", "production_id", "point_id");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), stopcode, status, order.getProductionId(), user.getPoint_id());

        JsonObject responseTest = Helper.setJsonRequestBody(params, values);
        Log.d(TAG, "testResponse postFaultData: " + responseTest.toString());

        iPanelRepository.postProductionStatus(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
                            if (status.equals(Constants.STATUS_FAULT_CODE) || status.equals(Constants.STATUS_FORCED_FAULT_CODE) || status.equals(Constants.STATUS_FINISHED_CODE)) {
                                if(status.equals(Constants.STATUS_FINISHED_CODE)){
//                                    mView.updateViewFinish();
                                    d(TAG, "onNext: here order fnisihed");
                                    clearOrder();
                                    return;
                                }
                                mView.updateViewStop(stopName);
                                productionStatus = Constants.STATUS_FAULT_CODE;
//                                counter.cancel();
                            } else {
                                mView.updateViewActive(Constants.STATUS_ACTIVE_TEXT);
                                productionStatus = Constants.STATUS_ACTIVE_CODE;
//                                counter.start();
                            }
                        } else {
                            progressDialog.startDialog(jsonObject.get("message").getAsString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: error ocurred in postquantity" + e.getMessage());
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }

    public void getWorkOrders() {
        if (!userController()) {
            return;
        }

        List<String> params = asList("mac", "user_id", "token", "point_id");
        List<String> values = asList(Helper.AccesMac(), user.getId(), user.getToken(), user.getPoint_id());

        d(TAG, "getWorkOrders: body object : " + Helper.setJsonRequestBody(params, values));

        iPanelRepository.getWorkOrderId(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject orderResponse) {
                        d(TAG, "onNext: get producs : " + orderResponse.toString());
                        if (orderResponse.get("success").getAsBoolean()) {
                            JsonArray orderArray = orderResponse.get("data").getAsJsonArray();
                            ArrayList<WorkHolder> orderList = new ArrayList<>();
                            for (JsonElement object : orderArray) {

                                WorkHolder holder = new WorkHolder(
                                        object.getAsJsonObject().get("workorder_id").getAsString(),
                                        object.getAsJsonObject().get("workorder_name").getAsString()
                                );

                                orderList.add(holder);
                                d(TAG, "onResponse: orderList" + orderList.get(0).getWorkOrderName());
                            }

                            mView.showOrderPopup(user, orderList);
                        } else {
                            mView.showErrorToast("Hata", orderResponse.get("message").getAsString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showErrorToast("Hata", e.toString());
                        mView.hideProgress();
                        Log.d("NETWORK_CALLS_ORDER", "onError: " + e.toString());
                        if (e instanceof ConnectException) {
                            mView.showDialogAlert(Constants.CHECK_ETH);
                        }
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }

    public void startWorkOrder(String workOrderId) {
        if (!userController()) {
            return;
        }

        d(TAG, "startWorkOrder: here");

        List<String> params = Arrays.asList("mac", "user_id", "token", "workorder_id", "point_id");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), workOrderId, user.getPoint_id());

        iPanelRepository.startWorkOrder(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject orderResponse) {
                        d(TAG, "onNext: start order response : " + orderResponse);
                        JsonObject data = orderResponse.get("data").getAsJsonObject();
                        if (orderResponse.get("success").getAsBoolean()) {
                            try {
                                Order order = new Order(
                                        data.get("production_id").getAsString(),
                                        data.get("workorder_id").getAsString(),
                                        data.get("workorder_code").getAsString(),
                                        data.get("workorder_ordernumber").getAsString(),
                                        data.get("workorder_qty").getAsString(),
                                        data.get("product_name").getAsString(),
                                        data.get("customer_name").getAsString(),
                                        data.get("totalqty").getAsString(),
                                        data.get("cuts").getAsJsonObject().get("1K").getAsFloat(),
                                        data.get("cuts").getAsJsonObject().get("2K").getAsFloat(),
                                        "",
                                        data.get("product_id").getAsString(),
                                        data.get("deadline").getAsString()
                                );
                                setOrder(order);
                                mView.hideOrderPopup();
                            } catch (Exception e) {
                                e.printStackTrace();
//                                showDialogAlert("Hata Oluştu : " + e.getMessage());
                                mView.showErrorToast("Hata Oluştu", e.getMessage());
                            }
                            mView.hideProgress();
//                            cutIds1K = Arrays.asList(data.get("K"))

                        } else {
                            mView.hideProgress();
                            mView.showErrorToast("Hata", orderResponse.get("message").getAsString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.showErrorToast("Hata", e.toString());
                        mView.hideProgress();
                        Log.d("NETWORK_CALLS_ORDER", "onError: " + e.toString());
                        if (e instanceof ConnectException) {
                            mView.showDialogAlert(Constants.CHECK_ETH);
                        }
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }

    public void getTime() {

        iPanelRepository.getTime()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: subscrib");
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
                            if (jsonObject.get("data").getAsBoolean()) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        postQuantityAndPrint(true);
                                        retryShift = 0;
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: error ocurred in postquantity" + e.getMessage());
                        if (e instanceof ConnectException) {
                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setUser(Operator user) {
        this.user = user;
        mView.setUserName(user.getName());
        mView.setMachineName(user.getPoint_name());
    }

    @Override
    public void setOrder(Order order) {

        this.order = order;
//        Log.d(TAG, "setOrder: " + Float.valueOf(removeLastCharOptional(order.getCutQuantity())));
        mView.setWorkOrderStatus(Constants.STATUS_ACTIVE_TEXT);
        mView.updateViews(order.getProductName(), order.getCustomerName(), order.getDeadline(), order.getOrderNumber(), order.getWorkorderQty(), order.getTotalCutValue(), order.getDescription(), String.format("%.2f", order.getQuantity1K() / 100) + " m", String.format("%.2f", order.getQuantity2K() / 100)+" m",order.getWorkorderCode() );
        mView.changeOrderButtonText(true);
    }

    @Override
    public void startOrderPopup() {
        // deprecated
    }

    public void openPreviewPopupPrevious(String barcode){
        mView.showBarcodePreviewPopup(barcode);
    }

//    @Override
//    public void startOrderPopup() {
//        if (userController() == false) {
//            return;
//        }
//        orderPopup.showPopUp(user);
//    }

    @Override
    public void startUserPopup() {
        userPopup.showPopUp();
    }

    @Override
    public void clearOrder() {
        this.order = null;
        totalLength = length;
        mView.clearOrderViews();
        mView.clearWorkorder();
        mView.changeOrderButtonText(false);
    }

    public void changeUser(Operator user) {
        this.user = user;
        mView.setUserName(user.getName());
    }

    public Operator getUser() {
        return this.user;
    }

    @Override
    public void clearUser() {
        if (length > 5) {
            mView.showErrorToast("Hata", "Metraj hafızaya almadan çıkış yapılamaz.");
            return;
        }
        mView.clearUserName();
    }

    public void finishWorkOrder() {
        if(length > 5){
            mView.showErrorToast("Hata","Metraj hafızaya alınmadan çıkış yapılamaz.");
            return;
        }
        if(order.getQuantity1K() != 0 || order.getQuantity2K() != 0){
            mView.showErrorToast("Hata","Hafızadaki metrajlar yazdırılmadan çıkış yapılamaz");
            return;
        }
        postProductionStatus("0","","3");
//        postQuitData();
    }

    @Override
    public void clearTotalLength() {
//        totalLength = getCutQuantity() + length;
//        mView.updateTotalQuantity(totalLength);
    }

    @Override
    public void clearQuantity(String quality) {
        if(quality.equals("1K")){
            order.setQuantity1K(0);
            mView.update1K(order.getQuantity1K());
        } else if(quality.equals("2K")){
            order.setQuantity2K(0);
            mView.update2K(order.getQuantity2K());
        }else{
            //boş
        }
    }

    @Override
    public void quitFromPanel() {
        compositeDisposable.dispose();
//        stopTimer();
        ((Activity) context).finish();
    }

    public static String removeLastCharOptional(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }

    void startEncoder() {

        compositeDisposable.add(Completable.fromAction(this::getLengthFromJNI)
                .subscribeOn(Schedulers.computation())
                .subscribe());
        compositeDisposable.add(Observable.interval(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> mView.updateQuantity(length)));
//        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(aLong -> mView.updateTotalQuantity(totalLength)));
        Log.d(TAG, "startEncoder: here aq");

//        Observable.interval(10, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        compositeDisposable.add(d);
//                        Log.d(TAG, "startEncoder: here subs aq");
//
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Log.d(TAG, "startEncoder: here next aq");
//
//                        Date currentTime = Calendar.getInstance().getTime();
//                        String a = formatter.format(currentTime);
//                        Log.d(TAG, "onNext: date " + a);
//
//                        for (String shift:shiftList)
//                        {
//                            Log.d(TAG, "onNext: shift : " + shift);
//                            Log.d(TAG, "onNext: a : " + a);
//                            if(a.equals(shift))
//                            {
//                                Log.d(TAG, "onNext: VARDİYE ZAMANI");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        mView.showErrorToast("Hata",e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete: finished");
//                    }
//                });

    }


    private void getLengthFromJNI() {

        do {
            response = stringFromJNI(oldState);
            if (response.length() == 4) {
                response = response.substring(1);
//                Log.d("RESPONSE-C", "res : " + response);
            }

            currentState = response.substring(0, 2);
            command = response.substring(2);

            if (length == 0) {
//                if (!chronometer.isRunning) {
//
//                    chronometer.setMsElapsed(0);
//                    chronometer.start();
//                    Log.d(TAG, "getLengthFromJNI: is chronometer started :" + chronometer.isRunning + " elapsed : " + chronometer.msElapsed);
//                }
            }

            switch (command) {
                case "1":
                    length += 0.0625;
                    totalLength += 0.0625;
                    oldCommand = command;
//                    signal++;
//                    Log.d(TAG, "getLengthFromJNI: " + signal);
                    break;
                case "2":
                    length -= 0.0625;
                    totalLength -= 0.0625;
                    oldCommand = command;
//                    signal++;
//                    Log.d(TAG, "getLengthFromJNI: " + signal);
//                    Log.d(TAG, "getLengthFromJNI: " + command);

                    break;
                case "3":
//                    Log.d("LENGTH-F", "KACIRDIM " + currentState + " - " + String.valueOf(oldState));

                    if (oldCommand.equals("1")) {
//                        Log.d("LENGTH-F", "+0.50  ");

                        length += 0.125;
                        totalLength += 0.125;
//                        signal +=2;
//                        Log.d(TAG, "getLengthFromJNI: " + signal);
                    } else if (oldCommand.equals("2")) {
                        length -= 0.125;
                        totalLength -= 0.125;
//                        signal -=2;
//                        Log.d(TAG, "getLengthFromJNI: " + signal);
                    }
                    break;
            }


            if (oldState != Integer.parseInt(currentState)) {
//                Log.d(TAG, "getLengthFromJNI: state : "+ currentState);
                if (length <= stretch) {
                    length = stretch;
                }
                if (totalLength <= stretch) {
                    totalLength = stretch;
                }
//                if (order != null) {
//
//                    if (totalLength <= getCutQuantity()) {
//                        totalLength = getCutQuantity();
//                    }
//                }
                if (length % 1 == 0 || length == 0) {

                    counter.cancel();

                    if (productionStatus.equals(Constants.STATUS_ACTIVE_CODE)) {

                        counter.start();

                    }
                }
            }

            oldState = Integer.parseInt(currentState);
        } while (!compositeDisposable.isDisposed());
    }

    CountDownTimer counter = new CountDownTimer(3000000, 1000) {

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
//            Toast.makeText(context, "ZORUNLU DURUŞ GİRİLMELİ . ", Toast.LENGTH_LONG).show();

//            postProductionStatus("", "0", "Zorunlu Duruş", "13");

//            fetchStopReasons(true);
        }
    };

    public Boolean userController() {
        if (user == null) {
            mView.showErrorToast("Kullanıcı Yok", "Bu işlemi yapmak için kullanıcı giriniz.");
            return false;
        }
        return true;
    }

    public Boolean orderController() {
        if (order == null) {
            mView.showErrorToast("İş Emri Yok", "Bu işlemi yapmak için iş emri giriniz.");
            return false;
        }
        return true;
    }

    public native String stringFromJNI(int oldstate);

    public void upMeter() {
        length += 100;
    }
}
