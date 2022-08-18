package com.ardayucesan.gezderi_uretim.ui.controlpanel.printer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.ardayucesan.gezderi_uretim.ui.controlpanel.dialog_alerts.ProgressDialog;

import java.util.HashMap;

public class UsbHandler {
    private static final String TAG = "com.android.example.USB_PERMISSION";
    private final String ACTION_USB_PERMISSION = TAG;
    protected UsbDevice device = null;

    ProgressDialog progressDialog;

    Context context;

    public UsbHandler(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        getUSBDevice();
    }

    public UsbDevice getDevice() {
        return this.device;
    }

    public boolean getUSBDevice() {
        device = (UsbDevice) ((Activity) context).getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);

        if (device == null) {
            UsbManager manager = (UsbManager) context.getApplicationContext().getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
            for (String key : deviceList.keySet()) {
                device = deviceList.get(key);
                if (device.getVendorId() == 5732) {
                    break;
                }
//                Log.d("grandroid", device.getDeviceName() + ", vendorID=" + device.getVendorId());
            }
        }

        if (device != null) {
            UsbManager manager = (UsbManager) context.getApplicationContext().getSystemService(Context.USB_SERVICE);

            if (!manager.hasPermission(device)) {//權限判斷.
                //Notice:
                //If it is executed here, then the interface will open fail, because the device is no permission.
                //When you append the "android.hardware.usb.action.USB_DEVICE_ATTACHED" request in the XXXManifest.xml file,
                //then the system will ask for permission each time you plug in the USB.
                final PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                manager.requestPermission(device, mPermissionIntent);
                return true;
            }
        }

        return false;
    }
}
