package com.savemebeta;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class restartCO extends Service implements OnTouchListener {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        stopService(new Intent(this, CO.class));
        startService(new Intent(this, CO.class));
        stopSelf();
        stopSelf();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this, "REQUEST event", 0).show();
        return false;
    }
}
