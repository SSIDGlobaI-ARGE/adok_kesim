package com.ardayucesan.gezderi_uretim.ui.controlpanel.dialog_alerts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ardayucesan.gezderi_uretim.R;

public class ProgressDialog {
    Context context;
    AlertDialog dialog;
    ProgressBar progressBar;
    View dialogView;

    private Handler handler = new Handler();

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public void startDialog(String message) {
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        dialogView = inflater.inflate(R.layout.custom_dialog, null);
        TextView tvDialogMessage = dialogView.findViewById(R.id.tvDialogMessage);
        tvDialogMessage.setText(message);
        builder.setView(dialogView);

        builder.setCancelable(true);
        dialog = builder.create();
//        dialog.getWindow().setLayout(600, 200);


        dialog.show();

        dialog.getWindow().getAttributes().width  = 800;
        dialog.getWindow().getAttributes().height = 250;
        dialog.getWindow().setAttributes(dialog.getWindow().getAttributes());
        countProgressBar();
    }

    public void hideDialog() {
        dialog.dismiss();
    }

    int pStatus = 0;
    TextView percentage;
    void countProgressBar() {
        Resources res = dialogView.getResources();
        Drawable drawable = dialogView.getResources().getDrawable(R.drawable.circular,context.getTheme());
        progressBar = (ProgressBar) dialogView.findViewById(R.id.circularProgressbar);
        progressBar.setProgress(0);   // Main Progress
        progressBar.setSecondaryProgress(100); // Secondary Progress
        progressBar.setMax(100); // Maximum Progress
        progressBar.setProgressDrawable(drawable);

        percentage = (TextView) dialogView.findViewById(R.id.tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 100) {
                    pStatus += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            progressBar.setProgress(pStatus);
                            percentage.setText(pStatus + "%");
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(25); //thread will take approx 3 seconds to finish,change its value according to your needs
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                hideDialog();
                pStatus = 0;
            }
        }).start();
    }
}

