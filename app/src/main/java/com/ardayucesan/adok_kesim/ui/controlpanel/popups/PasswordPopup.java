package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.common.Constants;
import com.ardayucesan.adok_kesim.ui.controlpanel._PanelContract;

public class PasswordPopup {

    private static final int width = 600;
    private static final int height = 200;
    //components
    Button btnEnter;
    EditText etAdminPassword;

    private AdminMenuPopup adminMenuPopup;
    private PopupWindow popupWindowPw;
    private View popupViewPw;

    private final Context context;
    private final _PanelContract.View mView;

    public PasswordPopup(Context context, _PanelContract.View mView) {
        this.context = context;

        adminMenuPopup = new AdminMenuPopup(context, mView);
        this.mView = mView;
    }

    public void showPopUp() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupViewPw = inflater.inflate(R.layout.popup_password, null);

        btnEnter = popupViewPw.findViewById(R.id.btnEnter);
        etAdminPassword = popupViewPw.findViewById(R.id.etAdminPassword);

        popupWindowPw = new PopupWindow(popupViewPw, width, height, true);
        popupWindowPw.setElevation(20);

        popupWindowPw.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        btnEnter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAdminPassword.getText().toString().equals(Constants.ADMIN_PW)) {
                    hidePopup();
                    adminMenuPopup.showPopUp();
                } else {
                    Toast.makeText(context, "Yanlış Şifre Girdiniz.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void hidePopup() {
        popupWindowPw.dismiss();
    }
}
