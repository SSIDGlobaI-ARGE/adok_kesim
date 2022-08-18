package com.ardayucesan.gezderi_uretim.ui.controlpanel;


import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ardayucesan.gezderi_uretim.common.Constants;
import com.ardayucesan.gezderi_uretim.common.Helper;
import com.ardayucesan.gezderi_uretim.common.MyChronometer;
import com.ardayucesan.gezderi_uretim.common.PinHelper;
import com.ardayucesan.gezderi_uretim.data.network.model.GlobalEvent;
import com.ardayucesan.gezderi_uretim.data.network.model.fault.Fault;
import com.ardayucesan.gezderi_uretim.data.network.model.ticket.single_ticket_data.Ticket;
import com.ardayucesan.gezderi_uretim.data.network.model.ticket.single_ticket_data.TicketResponse;
import com.ardayucesan.gezderi_uretim.data.network.model.ticket.ticket_list_data.TicketListResponse;
import com.ardayucesan.gezderi_uretim.data.network.model.user.Operator;
import com.ardayucesan.gezderi_uretim.data.network.model.workorder.Order;
import com.ardayucesan.gezderi_uretim.data.network.repository.IGlobalRepository;
import com.ardayucesan.gezderi_uretim.data.network.repository.IPanelRepository;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.dialog_alerts.ProgressDialog;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.popups.OrderPopup;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.popups.UserPopup;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.printer.StandartPrintable;
import com.ardayucesan.gezderi_uretim.ui.controlpanel.printer.UsbHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
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
 * this class is presenter for panel activity
 *
 * @author ardayucesan
 * @version 1.9
 */

@SuppressWarnings("ALL")
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
    //GlobalUrl
    private String GLOBAL_URL;
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

    //speed and distance by avg roll speed
    float avgSpeedByRoll = 0;

    ArrayList<Float> speedList;

    //    float totalDisplacement = 0;
    int oldState = 0;

    MyChronometer chronometer;

    Boolean IS_PRINT;

    //    Instant workOrderStart;
//    Instant workOrderEnd;
//    Duration timeElapsed;
    // some time passes
    //    float averageSpeed;
//    float passedMinute;
//    long orderStartTime = 0;
//    long millis=0;
    //printable classes
    StandartPrintable standartPrintable;
    //Usb handler for printer device
    UsbHandler usbHandler;
    //Dialog instances
    ProgressDialog progressDialog;
    Context context;

    OrderPopup orderPopup;
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

        orderPopup = new OrderPopup(context, this);
        userPopup = new UserPopup(context, this);

    }

    public void start() {
        PinHelper.execCommand("echo 100 > /sys/class/gpio/export");
        PinHelper.execCommand("echo 99 > /sys/class/gpio/export");

        mView.init();
        startEncoder();
        calculateAverage();
//        counter.start();
        getMachineName();
    }

    @Override
    public void postDeleteQuantity() {

        if (userController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "quality", "value", "fault", "production_id", "barcode", "roll_speed");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), "SS", String.valueOf(length), "0", order.getProductionId(), "", String.valueOf(avgSpeedByRoll));

        iPanelRepository.postQuantity(Helper.setJsonRequestBody(params, values))
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

        iPanelRepository.getPreviousTicketList(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<TicketListResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull TicketListResponse ticketListResponse) {

                        if (ticketListResponse.getSuccess()) {
                            mView.loadPreviousTicketList(ticketListResponse.getBarcodeList());
                            mView.hideProgress();
                        } else {//v 1.3 te yok yeni eklendi
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
                        mView.showPreviousTicketsPopup();
                    }

                });
    }

    @Override
    public void fetchFaults(Boolean isForced) {

        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken());

        iPanelRepository.getFaultList(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonResponse) {
                        List<Fault> faultList = new ArrayList<>();

                        JsonObject data = jsonResponse.get("data").getAsJsonObject();
                        JsonArray rows = data.get("rows").getAsJsonArray();

                        for (int i = 0; i < data.get("count").getAsInt(); i++) {

                            JsonObject faultJson = rows.get(i).getAsJsonObject();

                            if (faultJson.get("type").getAsString().equals("stop")) {

                                Fault fault = new Fault(
                                        faultJson.get("id").getAsString(),
                                        faultJson.get("desc").getAsString(),
                                        faultJson.get("type").getAsString()
                                );
                                faultList.add(fault);
                            }
                        }
                        Log.d(TAG, "onNext: here");

                        mView.loadFaultList(faultList);
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

                        mView.showFaultsPopup(isForced);
                    }
                });
    }

    @Override
    public void printTicket(Ticket ticket) {
        standartPrintable.PrintLotTicket(ticket);
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

    @Override
    public void fetchTicketData(String upBarcode) {

        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "production_id", "quality", "cuts", "barcode");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), order.getProductionId(), "1K", order.getCutIds1K().toString(), upBarcode);

        JsonObject responseTest = Helper.setJsonRequestBody(params, values);
        Log.d(TAG, "testResponse fetchTicketData: " + responseTest.toString());

        iPanelRepository.getTicketData(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<TicketResponse>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                    }

                    @Override
                    public void onNext(@NonNull TicketResponse ticketResponse) {

                        if (ticketResponse.getSuccess()) {
                            Ticket ticket = ticketResponse.getData().getRows().get(0);
                            Log.d(TAG, "onNext: ticket : " + ticketResponse.getData().getRows().get(0).getMetraj());
                            printTicket(ticket);
                            order.deleteCutIds1K();
                        } else {
                            progressDialog.startDialog(ticketResponse.getMessage());

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

    @Override
    public void getMachineName() {
        List<String> params = Arrays.asList("mac");
        List<String> values = Arrays.asList(Helper.AccesMac());

        IGlobalRepository iGlobalRepository = new Retrofit.Builder()
                .baseUrl(Constants.TYPE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(IGlobalRepository.class);

        JsonObject responseTest = Helper.setJsonRequestBody(params, values);
        Log.d(TAG, "testResponse getMachineName: " + responseTest.toString());
        iGlobalRepository.getMachineName(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showProgress();
                        Log.d(TAG, "testResponse getMachineName: start");

                    }

                    @Override
                    public void onNext(@NonNull JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
                            mView.setMachineName(jsonObject.get("data").getAsJsonObject().get("name").getAsString());
                            Log.d(TAG, "onNext: machine name " + jsonObject.get("data").getAsJsonObject().get("name").getAsString());

                            iPanelRepository = new Retrofit.Builder()
                                    .baseUrl(jsonObject.get("data").getAsJsonObject().get("url").getAsString())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build().create(IPanelRepository.class);

                            IS_PRINT = jsonObject.get("data").getAsJsonObject().get("printer").getAsBoolean();
                            Log.d(TAG, "handleEvent: isPrint " + IS_PRINT);

                            EventBus.getDefault().postSticky(new GlobalEvent(jsonObject.get("data").getAsJsonObject().get("url").getAsString(), jsonObject.get("data").getAsJsonObject().get("printer").getAsBoolean()));

                        } else {
                            mView.showErrorToast("Tanımlanmamış cihaz", "Bu cihaz sistemde kayıtlı gözükmüyor");
                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof ConnectException) {
                            mView.showErrorToast("İnternet Bağlantısı Yok", "Server ile iletişim kurulamadı tekrar deneyin.");
                        }
                        Log.d(TAG, "onError: çöküyoru mlan : " + e.getLocalizedMessage());
                        mView.hideProgress();

                        compositeDisposable.add(Observable.interval(5, TimeUnit.MINUTES)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                            getMachineName();
                                        }
                                ));
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                        Log.d(TAG, "onComplete: completed globalrepo");
                    }
                });
    }

    @Override
    public void postQuantityAndPrint() {
        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        if (IS_PRINT) {
            if (usbHandler.getUSBDevice()) {
                return;
            }
            if (usbHandler.getDevice().getVendorId() != 5732) {
                progressDialog.startDialog("Kabloyu kontrol edin.");
                Log.d(TAG, "postQuantityAndPrint: Usb Kablosunu kontrol edin ve bekleyin. ");
                return;
            }
        }


        float average = 0;
        if (speedList != null) {

            int itemNumber = 0;
            for (float speed : speedList) {
                itemNumber++;
                Log.d(TAG, "postQuantityAndPrint: item number : " + itemNumber);
                Log.d(TAG, "postQuantityAndPrint: speed : " + speed);
                average += speed;
                Log.d(TAG, "postQuantityAndPrint: average speed: " + average);
            }

            average = average / speedList.size();
            Log.d(TAG, "postQuantityAndPrint: final average speed is : " + average);
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "quality", "value", "fault", "production_id", "barcode", "speed");

        List<String> values = Arrays.asList(
                Helper.AccesMac(),
                user.getId(),
                user.getToken(),
                "1K",
                String.valueOf(length),
                "0",
                order.getProductionId(),
                "",
                String.valueOf(average)
        );

        JsonObject responseTest = Helper.setJsonRequestBody(params, values);
        Log.d(TAG, "testResponse postquantity: " + responseTest.toString());

        iPanelRepository.postQuantity(Helper.setJsonRequestBody(params, values))
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

                            order.setCutQuantity(jsonObject.get("data").getAsJsonObject().get("kesim_miktari").getAsString());
                            order.setCutIds1K(new ArrayList<>(Arrays.asList(jsonObject.get("data").getAsJsonObject().get("cut_id").getAsString())));
                            Log.d(TAG, "onNext: total cut" + jsonObject.get("data").getAsJsonObject().get("kesim_miktari"));
                            fetchTicketData("");
                            mView.setTotalCut(jsonObject.get("data").getAsJsonObject().get("kesim_miktari").getAsString());
                            mView.showSuccessToast("İşlem Başarılı", "İşlem Gerçekleştirildi.");
                            deleteQuantity();

                        } else {
                            progressDialog.startDialog(jsonObject.get("message").getAsString());
                            mView.showErrorToast("İşlem Başarısız", "Yapmak istediğiniz işlem başarısız oldu tekrar deneyiniz..");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: error occurred in postquantity");
                        if (e instanceof ConnectException) {
//                            progressDialog.startDialog(Constants.CONNECTION_ERR);
                            mView.showErrorToast("İşlem Başarısız", Constants.CONNECTION_ERR);

                        }
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                });
    }

//    private void stopTimer() {
//        orderStartTime = 0;
//        totalDisplacement = 0;
//        passedMinute = 0;
//
//    }
//
//    private void startTimer() {
//        orderStartTime = System.currentTimeMillis();
//    }

    @Override
    public void postProductionStatus(int faultId, String faultName, String status) {
        if (userController() == false) {
            return;
        }
        if (orderController() == false) {
            return;
        }

        List<String> params = Arrays.asList("mac", "user_id", "token", "fault", "status", "production_id");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), String.valueOf(faultId), status, order.getProductionId());

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
                            if (status.equals(Constants.STATUS_FAULT_CODE) || status.equals(Constants.STATUS_FORCED_FAULT_CODE)) {
                                mView.updateViewFault(faultName);
                                productionStatus = Constants.STATUS_FAULT_CODE;
                                counter.cancel();
                            } else {
                                mView.updateViewActive(Constants.STATUS_ACTIVE_TEXT);
                                productionStatus = Constants.STATUS_ACTIVE_CODE;
                                counter.start();
                            }
                        } else {
                            progressDialog.startDialog(jsonObject.get("message").getAsString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: error ocurred in postquantity");
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
    public void setUser(Operator user) {
        this.user = user;
        mView.setUserName(user.getName());
    }

    @Override
    public void setOrder(Order order) {

        this.order = order;
        Log.d(TAG, "setOrder: " + Float.valueOf(removeLastCharOptional(order.getCutQuantity())));
        totalLength = getCutQuantity() + length;
        mView.setWorkOrderStatus(Constants.STATUS_ACTIVE_TEXT);
        mView.updateViews(order.getProductName(), order.getProductColor(), order.getWorkorderCode(), order.getWorkorderQty(), order.getCutQuantity(), order.getSpeed(), order.getThickness(), order.getWeight(), order.getDescription());
        mView.changeOrderButtonText(true);

    }

    @Override
    public void startOrderPopup() {
        if (userController() == false) {
            return;
        }
        orderPopup.showPopUp(user);
    }

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
        this.user = null;
        mView.clearUserName();
    }

    @Override
    public void clearTotalLength() {
        totalLength = getCutQuantity() + length;
        mView.updateTotalQuantity(totalLength);
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
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> mView.updateQuantity(length)));
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> mView.updateTotalQuantity(totalLength)));
    }

//    public void calculateSpeed() {
//        Log.d(TAG, "calculateSpeed: cağrildim");
//        currentDisplacement = (length - oldLength);
//        currentDisplacement = currentDisplacement / 5;
////        totalDisplacement += displacement;
//        oldLength = length;
////        Log.d(TAG, "calculateSpeed: displ" + displacement / 100);
////        mView.setSpeed(currentDisplacement * 60);
//        Log.d(TAG, "calculateSpeed: currentdisp " + currentDisplacement * 60);
//    }

    float speed = 0;

    public void calculateAverage() {
        speedList = new ArrayList<>(60);

        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(aLong -> {

                    if (oldLengthFast == 0) {
                        oldLengthFast = length;
                        return;
                    }

                    speed = (length - oldLengthFast);
                    oldLengthFast = length;

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setSpeed(speed);
                        }
                    });

                    if (speedList.size() == 60) {
                        speedList.remove(0);
                        speedList.add(speed);
                    } else {
                        speedList.add(speed);
                    }
                }));
    }

    public float getCutQuantity() {
        if (order.getCutQuantity() == "0") {
            return 0;
        }
        return Float.valueOf(removeLastCharOptional(order.getCutQuantity())) * 100;
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
                    length += 0.25;
                    totalLength += 0.25;
                    oldCommand = command;
//                    Log.d(TAG, "getLengthFromJNI: " + command);
                    break;
                case "2":
                    length -= 0.25;
                    totalLength -= 0.25;
                    oldCommand = command;
//                    Log.d(TAG, "getLengthFromJNI: " + command);

                    break;
                case "3":
//                    Log.d("LENGTH-F", "KACIRDIM " + currentState + " - " + String.valueOf(oldState));

                    if (oldCommand.equals("1")) {
//                        Log.d("LENGTH-F", "+0.50  ");

                        length += 0.50;
                        totalLength += 0.50;
                    } else if (oldCommand.equals("2")) {
                        length -= 0.50;
                        totalLength -= 0.50;
                    }
                    break;
            }


            if (oldState != Integer.parseInt(currentState)) {
                if (length <= stretch) {
                    length = stretch;
                }
                if (totalLength <= stretch) {
                    totalLength = stretch;
                }
                if (order != null) {

                    if (totalLength <= getCutQuantity()) {
                        totalLength = getCutQuantity();
                    }
                }
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

            postProductionStatus(0, "Zorunlu Duruş", "13");

            fetchFaults(true);

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

}
