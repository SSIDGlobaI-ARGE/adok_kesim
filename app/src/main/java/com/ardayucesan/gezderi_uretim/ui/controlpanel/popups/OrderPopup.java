package com.ardayucesan.gezderi_uretim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.ardayucesan.gezderi_uretim.R;
import com.ardayucesan.gezderi_uretim.common.Constants;
import com.ardayucesan.gezderi_uretim.common.Helper;
import com.ardayucesan.gezderi_uretim.data.network.model.GlobalEvent;
import com.ardayucesan.gezderi_uretim.data.network.model.user.Operator;
import com.ardayucesan.gezderi_uretim.data.network.model.workorder.Order;
import com.ardayucesan.gezderi_uretim.data.network.repository.IOrderRepository;
import com.ardayucesan.gezderi_uretim.ui.controlpanel._PanelContract;
import com.ardayucesan.gezderi_uretim.ui.controlpanel._PanelContract.Presenter;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderPopup {

    private static final int width = 1250;
    private static final int height = 320;
    //components
    EditText etOrderCode;
    ProgressBar progressBarOrder;
    TextView tvUser;
    TextView tvMachine;
    Button btnQuit;
    ImageView textDelete;
    TextView tvError;

    Retrofit retrofit;

    private long buttonClicktime = 0;

    private PopupWindow popupWindowOrder;
    private View popupViewOrder;

    private IOrderRepository iOrderRepository;

    private final Context context;
    private final _PanelContract.Presenter panelPresenter;

    private Operator user;

    Instant workOrderStart;
    Instant workOrderEnd;

    public OrderPopup(Context context, Presenter panelPresenter) {
        this.context = context;
        this.panelPresenter = panelPresenter;

        EventBus.getDefault().register(this);
//        iOrderRepository = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build().create(IOrderRepository.class);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void handleEvent(GlobalEvent event) {
//        this.GLOBAL_URL = url;
        iOrderRepository = new Retrofit.Builder()
                .baseUrl(event.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(IOrderRepository.class);

        retrofit = new Retrofit.Builder()
                .baseUrl(event.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Log.d("__OrderPopup", "handleEvent: url" + event.url);
    }

    public void showPopUp(Operator user) {
        this.user = user;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupViewOrder = inflater.inflate(R.layout.popup_order, null);

        popupWindowOrder = new PopupWindow(popupViewOrder, width, height, true);
        popupWindowOrder.setElevation(20);

        popupWindowOrder.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.BOTTOM, 0, 30);

        init();

        textDelete.setOnClickListener(view -> clearOrderCode());

    }

    public void hidePopup() {
        popupWindowOrder.dismiss();
    }

    public void init() {
        showKeyboard();
        etOrderCode = popupViewOrder.findViewById(R.id.etOrderCode);
        progressBarOrder = popupViewOrder.findViewById(R.id.progressBarOrder);
        tvError = popupViewOrder.findViewById(R.id.tvError);
//        btnQuit = popupViewOrder.findViewById(R.id.btnQuit);
        textDelete = popupViewOrder.findViewById(R.id.textDelete);

//        setStarterTexts(userName,machineName);

        etOrderCode.requestFocus();

        onBarcodeRead();
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(popupViewOrder.getWindowToken(), 0);
    }

    private void onBarcodeRead() {
        etOrderCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() < 6) {
                    tvError.setVisibility(View.GONE);
                }
                if (s.length() == 6) {
                    fetchWorkOrderId(s.toString());
                    hideKeyboard();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void fetchWorkOrderId(String value) {
        List<String> params = Arrays.asList("mac", "user_id", "token", "value");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), value);


        IOrderRepository api = retrofit.create(IOrderRepository.class);

        Call<JsonObject> call = api.getWorkOrderId(Helper.setJsonRequestBody(params, values));

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.body().get("success").getAsBoolean()) {

                        String workOrderId = response.body().getAsJsonObject("data").getAsJsonArray("rows").get(0).getAsJsonObject().get("workorder_id").getAsString();

                        fetchWorkOrder(workOrderId);

                    } else {
                        showError();
                        showErrorToast(response.body().get("message").getAsString());
                    }
                }
                clearOrderCode();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showErrorToast(t.toString() + "@fetchWorkOrderID");
                if (t instanceof ConnectException) {
                    showErrorToast(Constants.CHECK_ETH);
                }
                clearOrderCode();
            }

        });
    }

    public void fetchWorkOrder(String workOrderId) {

        List<String> params = Arrays.asList("mac", "user_id", "token", "workorder_id");
        List<String> values = Arrays.asList(Helper.AccesMac(), user.getId(), user.getToken(), workOrderId);

//        IOrderRepository iOrderRepository = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build().create(IOrderRepository.class);

        iOrderRepository.startWorkOrder(Helper.setJsonRequestBody(params, values))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showProgress();
                    }

                    @Override
                    public void onNext(@NonNull JsonObject orderResponse) {
                        JsonObject data = orderResponse.get("data").getAsJsonObject();
                        if (orderResponse.get("success").getAsBoolean()) {
                            try {
                                Order order = new Order(
                                        data.get("production_id").getAsString(),
                                        data.get("workorder_code").getAsString(),
                                        data.get("workorder_qty").getAsString(),
                                        data.get("product_name").getAsString(),
                                        data.get("customer_name").getAsString(),
                                        data.get("rulo").getAsInt(),
                                        data.get("product_color").getAsString(),
                                        data.get("kesim_miktari").getAsString(),
                                        data.get("K_miktar").getAsFloat(),
                                        new ArrayList<>(Arrays.asList(data.get("K_cuts").getAsString().split(","))),
                                        data.get("uretim_hizi").getAsString(),
                                        data.get("uretim_kalinlik").getAsString(),
                                        data.get("uretim_gramaj").getAsString(),
                                        data.get("uretim_aciklama").getAsString()
                                );
                                //agzd2021.
                                panelPresenter.setOrder(order);
                                hidePopup();
                            } catch (Exception e) {
                                e.printStackTrace();
                                showDialogAlert("Hata Oluştu : " + e.getMessage());
                                clearOrderCode();
                            }
                            hideProgress();
//                            cutIds1K = Arrays.asList(data.get("K"))

                        } else {
                            hideProgress();
                            showError();
                            showErrorToast(orderResponse.get("message").getAsString());
                            clearOrderCode();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showErrorToast(e.toString() + "@fetchWorkOrder");
                        hideProgress();
                        Log.d("NETWORK_CALLS_ORDER", "onError: " + e.toString());
                        if (e instanceof ConnectException) {
                            showDialogAlert(Constants.CHECK_ETH);
                        }
                        clearOrderCode();
                    }

                    @Override
                    public void onComplete() {

                        hideProgress();

                    }
                });
    }

    public void showProgress() {
        progressBarOrder.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBarOrder.setVisibility(View.GONE);
    }

    public void showError() {
        tvError.setVisibility(View.VISIBLE);
    }

    public void showErrorToast(String s) {
        Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
    }

    public void clearOrderCode() {
        etOrderCode.setText("");
    }

    public void showDialogAlert(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_theme);

        builder.setTitle("UYARI !");
        builder.setMessage(s);
        builder.setCancelable(true);
        builder.setPositiveButton("Tamam", (dialog, which) -> {
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}
