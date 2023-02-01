package com.ardayucesan.adok_kesim.ui.controlpanel.printer;


import android.graphics.Typeface;

import com.argox.sdk.barcodeprinter.emulation.pplb.PPLBBarCodeType;
import com.argox.sdk.barcodeprinter.emulation.pplb.PPLBFont;
import com.argox.sdk.barcodeprinter.emulation.pplb.PPLBOrient;

public class PrintTypeHolder {
    public int x;
    public int y;
    public int size;
    //for barcode
    public int narrow;
    public int wide;
    public int height;
    Boolean readable;
    //    public byte[] data_print;
    public String data;
    public String type;
    public Typeface typeface;
    public Boolean reverse_image;
    public PPLBBarCodeType pplb_barcode_type;
    public PPLBOrient pplb_orient;
    public PPLBFont pplb_font;
    public Boolean useBold;
    public Boolean useItalic;
    public Boolean useUnderLine;
    public Boolean useStrikeOut;
    public String imgName;


    public PrintTypeHolder(String type, int x, int y, PPLBOrient pplb_orient, PPLBFont pplb_font, int size, Boolean reverse_image, String data) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.pplb_orient = pplb_orient;
        this.pplb_font = pplb_font;
        this.size = size;
        this.reverse_image = reverse_image;
        this.data = data;
    }

    public PrintTypeHolder(String type, int x, int y, PPLBOrient pplb_orient, PPLBBarCodeType pplb_barcode_type, int narrow, int wide, int height, Boolean readable, String data) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.pplb_orient = pplb_orient;
        this.pplb_barcode_type = pplb_barcode_type;
        this.narrow = narrow;
        this.wide = wide;
        this.height= height;
        this.data = data;
    }
    public PrintTypeHolder(String type, Typeface typeface, int x, int y, int size, Boolean useBold, Boolean useItalic, Boolean useUnderLine, Boolean useStrikeOut, String imgName, String data) {
        this.type = type;
        this.typeface = typeface;
        this.x = x;
        this.y = y;
        this.size = size;
        this.useBold = useBold;
        this.useItalic = useItalic;
        this.useUnderLine = useUnderLine;
        this.useStrikeOut = useStrikeOut;
        this.imgName = imgName;
        this.data = data;
    }
}

