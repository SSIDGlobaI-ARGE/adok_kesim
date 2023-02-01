package com.ardayucesan.adok_kesim.ui.controlpanel.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ardayucesan.adok_kesim.ui.controlpanel.PanelPresenter;
import com.argox.sdk.barcodeprinter.BarcodePrinter;
import com.argox.sdk.barcodeprinter.BarcodePrinterGeneralException;
import com.argox.sdk.barcodeprinter.BarcodePrinterIllegalArgumentException;
import com.argox.sdk.barcodeprinter.connection.usb.USBConnection;
import com.argox.sdk.barcodeprinter.emulation.pplb.PPLB;
import com.argox.sdk.barcodeprinter.emulation.pplb.PPLBMediaType;
import com.argox.sdk.barcodeprinter.emulation.pplb.PPLBPrintMode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

public class StandartPrintable {

    Context context;
    UsbHandler usbHandler;

    public StandartPrintable(Context context) {
        this.context = context;
        usbHandler = new UsbHandler(this.context);
    }

    public void printBarcodeLabel(String imageUrl, PanelPresenter presenter) {

        if (usbHandler.getUSBDevice()) {
            return;
        }
        BarcodePrinter<USBConnection, PPLB> printer = new BarcodePrinter<USBConnection, PPLB>();

        try {
//            ArrayList<PrintTypeHolder> ticketGraphics = new ArrayList<>();
//            String baslik = "txt0";
//            String urun_adi = "txt1";
//            String renk_no = "txt2";
//            String musteri_firma = "txt3";
//            String musteri_renk_adi = "txt4";
//            String tarih = "txt5";
//            String is_emri = "txt6";
//            String spy = "txt7";
//            String siparis = "txt8";
//            String metraj = "txt9";
//            String rota = "txt10";
//            String kaplama = "txt11";
//            String empirme = "txt12";
//            String kimya = "txt13";
//            String kesim = "txt14";
//            String delme = "txt15";
//            String test = "txt16";
            printer.setConnection(new USBConnection(context, usbHandler.getDevice()));
            printer.setEmulation(new PPLB());

            printer.getConnection().open();

//            Collections.addAll(ticketGraphics,
//                    //rePrintablesHolder
//                    new PrintTypeHolder("text", Typeface.DEFAULT, 250, 20, 10, true, false, false, false, baslik, "TEST")
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 250, 20, 10, true, false, false, false, baslik, ticket.getMachineName())
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 130, 25, false, false, false, false, musteri_firma, ticket.getSirketAdi()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 160, 25, false, false, false, false, urun_adi, ticket.getUrunAdi()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 190, 25, false, false, false, false, renk_no, ticket.getRenkNo()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 220, 25, true, false, false, false, is_emri, ticket.getIsEmri()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 250, 25, false, false, false, false, tarih, ticket.getTarih()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 280, 25, false, false, false, false, spy, ticket.getSpyKod()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 310, 25, false, false, false, false, siparis, ticket.getSiparisNo()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 50, 340, 25, true, false, false, false, metraj, ticket.getMetraj()),
////                    //barcode
////                    new PrintTypeHolder("barcode", 180, 410, PPLBOrient.Clockwise_0_Degrees, PPLBBarCodeType.Code_128_Mode_A, 3, 6, 70, true, ticket.getBarkod()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 510, 180, 20, false, false, false, false, rota, "ROTA"),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 470, 210, 25, false, false, false, false, kimya, ticket.getP0()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 470, 240, 25, false, false, false, false, kaplama, ticket.getP1()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 470, 270, 25, false, false, false, false, empirme, ticket.getP2()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 470, 300, 25, false, false, false, false, delme, ticket.getP3()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 470, 330, 25, false, false, false, false, kesim, ticket.getP4()),
////                    new PrintTypeHolder("text", Typeface.DEFAULT, 470, 360, 25, false, false, false, false, test, ticket.getP5())
//            );

            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            imageView.setImageBitmap(resource);
                            try {
                                Log.d("PRİNTER", "onResourceReady: here before");
                                printer.getEmulation().getGraphicsUtil().storeGraphic(resource, "graphic");
                                printer.getEmulation().getGraphicsUtil().printStoreGraphic(20, 20, "graphic");
                                Log.d("PRİNTER", "onResourceReady: after ");

                            } catch (BarcodePrinterIllegalArgumentException e) {
                                throw new RuntimeException(e);
                            } catch (BarcodePrinterGeneralException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

            //call methods that you want.
            //setting.
            printer.getEmulation().getSetUtil().setHardwareOption(PPLBMediaType.Direct_Thermal_Media, PPLBPrintMode.Tear_Off, 0);
            printer.getEmulation().getSetUtil().setOrientation(false);
            printer.getEmulation().getSetUtil().setClearImageBuffer();
            printer.getEmulation().getSetUtil().setBackfeed(true, 30);

//            for (int i = 0; i < ticketGraphics.size(); i++) {
//                PrintTypeHolder obj = ticketGraphics.get(i);
//                if (obj.type.equals("text")) {
//                    if (obj.data == null) {
////                        Toast.makeText(ControlPanel.this, "İLETİŞİM SORUNU OLUŞTU ÖNCEKİ ETİKETİ YAZDIRA TIKLAYARAK YENİDEN DENEYİN.", Toast.LENGTH_LONG).show();
//                        return;
//                    } else {
//                        printer.getEmulation().getTextUtil().storeTextGraphic(obj.typeface, obj.size, obj.useBold, obj.useItalic, obj.useItalic, obj.useStrikeOut, obj.imgName, obj.data);
//                        printer.getEmulation().getGraphicsUtil().printStoreGraphic(obj.x, obj.y, obj.imgName);
//                    }
//                } else if (obj.type.equals("normal-text")) {
//                    if (obj.data == null) {
////                        Toast.makeText(ControlPanel.this, "İLETİŞİM SORUNU OLUŞTU ÖNCEKİ ETİKETİ YAZDIRA TIKLAYARAK YENİDEN DENEYİN.", Toast.LENGTH_LONG).show();
//
//                        return;
//                    } else {
//
//                        printer.getEmulation().getTextUtil().printText(obj.x, obj.y, obj.pplb_orient, obj.pplb_font, obj.size, obj.size, obj.reverse_image, obj.data.getBytes("cp857"));
//                    }
//
//                }
//                if (obj.type.equals("barcode")) {
//                    if (obj.data == null) {
////                        Toast.makeText(ControlPanel.this, "İLETİŞİM SORUNU OLUŞTU ÖNCEKİ ETİKETİ YAZDIRA TIKLAYARAK YENİDEN DENEYİN.", Toast.LENGTH_LONG).show();
//
//                        return;
//                    } else {
//
//                        printer.getEmulation().getBarcodeUtil().printOneDBarcode(obj.x, obj.y, obj.pplb_orient, obj.pplb_barcode_type, obj.narrow, obj.wide, obj.height, true, obj.data.getBytes());
//                    }
//                }
//            }

            //spy kodu
            //set print conditions.
            printer.getEmulation().getSetUtil().setPrintOut(1, 1);
            printer.getEmulation().printOut();
            printer.getConnection().close();

        } catch (Exception ex) {
            try {
                printer.getConnection().close();
            } catch (Exception e) {
            } finally {
//                Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
                Log.e("argox_demo", null, ex);
            }
        }
    }
}
