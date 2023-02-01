package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ardayucesan.adok_kesim.BuildConfig;
import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.common.Constants;
import com.ardayucesan.adok_kesim.common.Helper;
import com.ardayucesan.adok_kesim.common.tasks.ApkInstaller;
import com.ardayucesan.adok_kesim.data.network.model.GlobalEvent;
import com.ardayucesan.adok_kesim.data.network.repository.ILoginRepository;
import com.ardayucesan.adok_kesim.ui.controlpanel._PanelContract;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AdminMenuPopup {
    private static final String TAG = "__AdminPopup";

    private static final int width = 850;
    private static final int height = 650;
    //components
    Button btnTest;
    Button btnQuitApp;
    Button btnSettings;
    Button btnUpdate;
    EditText etTest;
    TextView tvIp;
    TextView tvMac;
    TextView tvVersion;

    String updateUrl;

    Retrofit retrofit;

    private PopupWindow popupWindowAdmin;
    private View popupViewAdmin;

    private final Context context;
    private final _PanelContract.View mView;

    public AdminMenuPopup(Context context, _PanelContract.View mView) {
        this.context = context;
        this.mView = mView;


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public void showPopUp() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupViewAdmin = inflater.inflate(R.layout.popup_admin, null);

        btnTest = popupViewAdmin.findViewById(R.id.btnTest);
        btnQuitApp = popupViewAdmin.findViewById(R.id.btnQuitApp);
        btnSettings = popupViewAdmin.findViewById(R.id.btnSettings);
        btnUpdate = popupViewAdmin.findViewById(R.id.btnUpdate);
        etTest = popupViewAdmin.findViewById(R.id.etTest);
        tvIp = popupViewAdmin.findViewById(R.id.tvIpAdmin);
        tvMac = popupViewAdmin.findViewById(R.id.tvMacAdmin);
        tvVersion = popupViewAdmin.findViewById(R.id.tvVersionAdmin);

        popupWindowAdmin = new PopupWindow(popupViewAdmin, width, height, true);
        popupWindowAdmin.setElevation(20);

        popupWindowAdmin.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        btnTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etTest.setVisibility(View.VISIBLE);
                etTest.requestFocus();
            }
        });
        btnSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });
        btnQuitApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
                System.exit(0);
            }
        });
        btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: pname" + context.getPackageName());
                fetchUpdate(context.getPackageName());
            }
        });


        tvIp.setText(setIp());
        tvVersion.setText(BuildConfig.VERSION_NAME);
        tvMac.setText(Helper.AccesMac());

    }

    private String setIp() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Service.CONNECTIVITY_SERVICE);
        try {
            return connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getLinkAddresses().get(1).toString();
        } catch (Exception a) {
            return "0.0.0.0";
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void handleEvent(GlobalEvent event) {
//        this.GLOBAL_URL = url;


        Log.d("__OrderPopup", "handleEvent: url" + event.url);
    }

    public void fetchUpdate(String packageName) {
        List<String> params = Arrays.asList("name");
        List<String> values = Arrays.asList(packageName);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build();
        try{

        ILoginRepository api = retrofit.create(ILoginRepository.class);
            Call<JsonObject> call = api.checkUpdates(Helper.setJsonRequestBody(params, values));

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    Log.d(TAG, "onResponse: body : " + response.body());
                    JsonObject responseObject = response.body();

                    if (responseObject.get("success").getAsBoolean()) {
                        //şimdilik kapalı kalacak kesimin versiyonu çekiyor !!!

                        if (responseObject.getAsJsonObject("data").get("version").getAsInt() > BuildConfig.VERSION_CODE) {

//                        mView.showUpdateNotification(responseObject.get("success").getAsString());
                            updateUrl = responseObject.getAsJsonObject("data").get("url").getAsString();
                            startApkDownload(context);
                        } else{
                            mView.showSuccessToast("Uygulama Güncel","Bu versiyona gelmiş bir güncelleme yok.");
                        }
                    } else {
                        mView.showErrorToast("Hata","Server ile iletişim kuruldu işlem yapılamadı. ");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    mView.showErrorToast("Hata",t.toString());
                    if (t instanceof ConnectException) {
                        mView.showDialogAlert(Constants.CHECK_ETH);
                    }
                }
            });
        }catch (NullPointerException e){
            mView.showErrorToast("Hata","İnternet Ayarlarını kontrol ediniz.");
            return;
        }


    }

    public void startApkDownload(Context context) {
        ProgressDialog progressDialog = showDownloadProgress();

        ApkInstaller downloadAndInstall = new ApkInstaller();
        downloadAndInstall.setContext(context, progressDialog);
        Log.d("UPDATER", "in downloader : " + updateUrl);
        downloadAndInstall.execute(updateUrl);

    }

    public ProgressDialog showDownloadProgress() {
        ProgressDialog mProgressDialog;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(" Güncelleme İndiriliyor Lütfen Bekleyin...");
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public void hidePopup() {
        popupWindowAdmin.dismiss();
    }
}
