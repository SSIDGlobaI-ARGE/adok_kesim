package com.ardayucesan.adok_kesim.ui.controlpanel.popups;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ardayucesan.adok_kesim.R;
import com.ardayucesan.adok_kesim.common.Constants;
import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;
import com.ardayucesan.adok_kesim.ui.controlpanel.printer.StandartPrintable;
import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import io.reactivex.annotations.NonNull;

public class PopupPrintPreview {
    private static final int width = 700;
    private static final int height = 700;
    final PanelPresenter panelPresenter;
    final Context context;
    private PopupWindow popupWindow;

    public PopupPrintPreview(Context context, PanelPresenter panelPresenter) {
        this.context = context;
        this.panelPresenter = panelPresenter;
    }

    public void showPopUp(String barcode) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_barcode_preview, null);

        Button btnPrint = popupView.findViewById(R.id.btnPrintBarcode);
        ImageView btnQuitPreview = popupView.findViewById(R.id.btnQuitPreview);
        ImageView imgHolder = popupView.findViewById(R.id.imgViewHolder);
        ImageView imgError = popupView.findViewById(R.id.imgViewPreviewError);
        TextView imgTextError = popupView.findViewById(R.id.tvPreviewError);
        ProgressBar pbarBarcode = popupView.findViewById(R.id.pbarBarcode);

//        imgHolder.setImageResource(R.drawable.exclamation);

//        Resources res = context.getResources(); // need this to fetch the drawable
//        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.exclamation);
//        imgHolder.setImageBitmap(bmp);
        String errUrl = "https://stackoverflow.com/questions/36339402/glide-image-loading-timeout-increase";
        String testUrl = "https://www.adok.com.tr/images/Adok_logo.png";
        String url = Constants.IMAGE_URL + barcode + ".png";

        Log.d("__PREVİEW", "showPopUp: image url : " + url);

        Glide.with(context)
                .load(url)
                .timeout(10000)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        imgHolder.setBackground(context.getResources().getDrawable(R.drawable.exclamation));
                        pbarBarcode.setVisibility(View.INVISIBLE);
                        imgError.setVisibility(View.VISIBLE);
                        imgTextError.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pbarBarcode.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgHolder);

        popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setElevation(20);

        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StandartPrintable standartPrintable = new StandartPrintable(context);
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            imageView.setImageBitmap(resource);
                                Log.d("PRİNTER", "onResourceReady: here before");
//                                    printer.getEmulation().getGraphicsUtil().storeGraphic(resource, "graphic");
//                                    printer.getEmulation().getGraphicsUtil().printStoreGraphic(100, 20, "graphic");
                                Log.d("PRİNTER", "onResourceReady: after ");
                                standartPrintable.printBarcodeLabel(resource, panelPresenter);

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        });
        btnQuitPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public void hidePopup() {
        popupWindow.dismiss();
    }

}
