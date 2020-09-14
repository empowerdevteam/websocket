package com.homecontrol;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;




import java.util.Timer;
import java.util.TimerTask;
import static com.homecontrol.CordovaWebsocketPlugin.webSockets;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WebsocketService extends Service {
    private static final String TAG = "CordovaWebsocketService";
   
    public static final long INTERVAL=10000;//variable to execute services every 10 second
    private Handler mHandler=new Handler(); // run on another Thread to avoid crash
    private Timer mTimer=null;
    private String webScoket = null;
   
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        webScoket = intent.getStringExtra("webSocketId");
        Log.i(TAG,""+webScoket);

        if(mTimer!=null)
            mTimer.cancel();
        else
            mTimer=new Timer(); // recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(),0,INTERVAL);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast at every 10 second


                    //Toast.makeText(getApplicationContext(), "websocket"+webScoket, Toast.LENGTH_SHORT).show();
                    for(CordovaWebsocketPlugin.WebSocketAdvanced ws : webSockets.values()) {

                        // ws.close(1000, "Disconnect");
                         try {
                             if (ws.socketStatus == SocketStatus.FAILURE) {
                               // ws.close(1000,"Connection closed");
                                // Toast.makeText(getApplicationContext(), "websocket" + ws.webSocketId + "Failure Detected,Trying to reconnect", Toast.LENGTH_SHORT).show();
                                // ws.webSocket = ws.client.newWebSocket(ws.request, ws);
                                 // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
                                // ws.client.dispatcher().executorService().shutdown();
                                // ws.socketStatus = SocketStatus.CONNECTED;
                             } else {
                               //  Toast.makeText(getApplicationContext(), "websocket" + "" + ws.webSocketId, Toast.LENGTH_SHORT).show();
 
                             }
                         }catch (Exception e){
                            // Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
                             //Log.i("Exception",e.getMessage());
                         }
 
                     }
                }
            });
        }
    }



}
