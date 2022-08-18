package com.ardayucesan.gezderi_uretim.common;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Chronometer;

public class MyChronometer extends Chronometer {

    public int msElapsed;
    public boolean isRunning = false;

    public MyChronometer(Context context) {
        super(context);
    }

    public int getMsElapsed() {
        return msElapsed;
    }

    public void setMsElapsed(int ms) {
        setBase(getBase() - ms);
        msElapsed  = ms;
    }

    @Override
    public void start() {
        super.start();
        setBase(SystemClock.elapsedRealtime() - msElapsed);
        isRunning = true;
    }

    @Override
    public void stop() {
        super.stop();
        if(isRunning) {
            msElapsed = (int)(SystemClock.elapsedRealtime() - this.getBase());
        }
        isRunning = false;
    }
}