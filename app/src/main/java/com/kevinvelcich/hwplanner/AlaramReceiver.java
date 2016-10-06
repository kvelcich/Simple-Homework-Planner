package com.kevinvelcich.hwplanner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.kevinvelcich.hwplanner.Activity.MainActivity;
import com.kevinvelcich.hwplanner.Database.DatabaseAssignment;

public class AlaramReceiver extends BroadcastReceiver {

    int MID = 0;
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //if(There are assignments due tomorrow)
        DatabaseAssignment db = new DatabaseAssignment(context, null, null, 1);
        if (db.isAssignmentDue()) {
            //this can be any int


//Building the Notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Simple Planner")
                    .setContentText("You have assignments due tomorrow")
                    .setVibrate(new long[]{500, 750, 500, 750})
                    .setLights(Color.CYAN, 1500, 1500)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

            /*long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Simple Planner")
                    .setContentText("You have assignments due tomorrow").setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{500, 1000, 500, 1000} )
                    .setLights(Color.CYAN, 1500, 1500);
            notificationManager.notify(MID, mNotifyBuilder.build());
            MID++; */
        }

    }

}