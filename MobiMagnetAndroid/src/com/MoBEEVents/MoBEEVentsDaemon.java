package com.MoBEEVents;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MoBEEVentsDaemon extends Service {
    private NotificationManager mNM;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	MoBEEVentsDaemon getService() {
            return MoBEEVentsDaemon.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MobiMagnetDaemon", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        //mNM.cancel(R.string.mobimagnet_daemon_started);

        // Tell the user we stopped.
        //Toast.makeText(this, R.string.mobimagnet_daemon_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
      //  CharSequence text = getText(R.string.mobimagnet_daemon_started);

        // Set the icon, scrolling text and timestamp
      //  Notification notification = new Notification(R.drawable.icon, text,
        //        System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        /*PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
               new Intent(this, LocalServiceActivities.Controller.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.mobimagnet_daemon_label),
                       text, contentIntent);
*/
        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
    //    mNM.notify(R.string.mobimagnet_daemon_started, notification);
    }
}