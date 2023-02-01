package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.common.Constants;
import com.ardayucesan.adok_kesim.common.Helper;
import com.ardayucesan.adok_kesim.data.network.model.user.Operator;
import com.ardayucesan.adok_kesim.data.network.repository.ILoginRepository;
import com.ardayucesan.adok_kesim.ui.controlpanel._PanelContract;
import com.ardayucesan.adok_kesim.ui.controlpanel._PanelContract.Presenter;
import com.google.gson.JsonObject;

import java.net.ConnectException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserPopup {

    private static final String TAG = "__UserPopup";

    private static final int width = 1250;
    private static final int height = 320;

    //components
    private ProgressBar progressBar;
    private EditText etCardId;
    TextView tvLogin;
    TextView tvLoginError;

    private PopupWindow popupWindowUser;
    private View popupViewUser;
    ILoginRepository iLoginRepository;
    //construct params
    private final Context context;
    private final _PanelContract.Presenter panelPresenter;

    public UserPopup(Context context, Presenter panelPresenter) {
        this.context = context;
        this.panelPresenter = panelPresenter;

        iLoginRepository = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ILoginRepository.class);
    }

    public void showPopUp() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupViewUser = inflater.inflate(R.layout.popup_user, null);

        popupWindowUser = new PopupWindow(popupViewUser, width, height, true);
        popupWindowUser.setElevation(20);

        popupWindowUser.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.BOTTOM, 0, 30);

        init();

        etCardId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardRead();
            }
        });
    }
    public void hidePopup(){
        popupWindowUser.dismiss();
    }

    public void init() {
        //view components
        progressBar = popupViewUser.findViewById(R.id.progressBar);
        etCardId = popupViewUser.findViewById(R.id.etPassword);
        tvLogin = popupViewUser.findViewById(R.id.tvLogin);
        tvLoginError = popupViewUser.findViewById(R.id.tvErrorLogin);
    }

    public void fetchUser(String cardID) {

        try{
            iLoginRepository.getUser(setLoginRequestBody(cardID))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<JsonObject>() {

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            showProgress();
                        }

                        @Override
                        public void onNext(@NonNull JsonObject response) {
                            JsonObject responseData = response.getAsJsonObject();

                            if (responseData.get("success").getAsBoolean()) {
//                            mView.clearErrorText();

                                JsonObject userData = responseData.get("data").getAsJsonObject().get("rows").getAsJsonArray().get(0).getAsJsonObject();

                                Operator user = new Operator(
                                        userData.get("id").getAsString(),
                                        userData.get("name").getAsString(),
                                        userData.get("title").getAsString(),
                                        userData.get("auth").getAsString(),
                                        userData.get("token").getAsString(),
                                        userData.get("point_name").getAsString(),
                                        userData.get("point_id").getAsString()
                                );

                                panelPresenter.setUser(user);
                                hideProgress();
                                showDefaultLoginText();
                                hidePopup();
                            } else {

                                showError(responseData.get("message").getAsString());

                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                // your code here, and if you have to refresh UI put this code:
                                                ((Activity) context).runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        //your code
                                                        hideProgress();
                                                        showDefaultLoginText();
                                                        clearErrorText();
                                                    }
                                                });
                                            }
                                        }, 2000
                                );
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                            if (e instanceof ConnectException) {
                                showDialogAlert(Constants.CHECK_ETH);
                            }

                            showNetworkError(e.toString());
                            showDefaultLoginText();
                            hideProgress();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (NullPointerException e){
            Toast.makeText(context,"Hata İnterneti Kontrol Edin",Toast.LENGTH_SHORT).show();
        }


    }
    public JsonObject setLoginRequestBody(String cardId) {
        String shaKey = Helper.bin2hex(Helper.getHash(Helper.AccesMac() + cardId + Constants.SALT));
        try {
            JsonObject reqBody = new JsonObject();
            reqBody.addProperty("mac", Helper.AccesMac());
            reqBody.addProperty("card_id", cardId);
            reqBody.addProperty("key", shaKey);
            return reqBody;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        tvLogin.setText("Lütfen bekleyin...");
    }

    public void hideProgress() {
        if (progressBar != null && progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showError(String s) {
        tvLoginError.setText(s);

    }

    public void showNetworkError(String s) {
        Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();
    }

    public void showDefaultLoginText() {
        tvLogin.setText(context.getResources().getText(R.string.giri_yapmak_in_kart_okutunuz));
    }


    public void onCardRead() {
        fetchUser(etCardId.getText().toString());
        etCardId.setText("");

    }

    public void showDialogAlert(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_theme);

        builder.setTitle("UYARI !");
        builder.setMessage(s);
        builder.setCancelable(true);
        builder.setPositiveButton("Tamam",(dialog, which) -> {});

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    public void clearErrorText() {
        tvLoginError.setText("");
    }

    public void onBackPressed() {

    }
}

