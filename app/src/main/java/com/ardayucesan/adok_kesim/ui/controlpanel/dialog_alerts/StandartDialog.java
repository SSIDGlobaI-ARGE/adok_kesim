package com.ardayucesan.adok_kesim.ui.controlpanel.dialog_alerts;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.ardayucesan.adok_kesim.R;

public class StandartDialog {

    Context context;

    public StandartDialog(Context context) {
        this.context = context;
    }

    public void showDialog(String message) {
        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.dialog_theme);

        builder.setTitle("Uyarı !");
        builder.setMessage("message !");

        builder.setCancelable(true);

        builder
                .setPositiveButton(
                        "Tamam",
                        (dialog, which) -> {

                        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

}