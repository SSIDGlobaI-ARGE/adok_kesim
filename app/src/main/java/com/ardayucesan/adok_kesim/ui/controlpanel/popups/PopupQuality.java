package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;

public class PopupQuality {
    private static final int width = 700;
    private static final int height = 700;
    final PanelPresenter panelPresenter;
    final Context context;
    private PopupWindow popupWindow;

    public PopupQuality(Context context, PanelPresenter panelPresenter) {
        this.context = context;
        this.panelPresenter = panelPresenter;
    }

    public void showPopUp() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_quality, null);

        Button quality1K = popupView.findViewById(R.id.btnQuality1K);
        Button quality2K = popupView.findViewById(R.id.btnPrint2K);
        ImageView btnQuitQuality = popupView.findViewById(R.id.btnQuitQuality);
//        ImageView imgHolder = popupView.findViewById(R.id.imgViewHolder);

//        Glide.with(context).load("http://78.186.71.81/images/generate/1300000309.png").diskCacheStrategy(DiskCacheStrategy.NONE )
//                .skipMemoryCache(true).into(imgHolder);

        quality1K.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panelPresenter.saveToMemory("1K","0");
            }
        });

        quality2K.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panelPresenter.fetchProductFaults("2K");
//                PopupFault popupFault = new PopupFault(context,panelPresenter);
//                popupFault.show
            }
        });


        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setElevation(20);

        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        btnQuitQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

    public void hidePopup(){
        popupWindow.dismiss();
    }

}
