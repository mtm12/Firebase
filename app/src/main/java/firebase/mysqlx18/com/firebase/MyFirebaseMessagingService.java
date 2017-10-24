package firebase.mysqlx18.com.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Date;

/**
 * Created by marct_000 on 10/23/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Target mTarget;
    Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        final BitmapDrawable drawable = null;
        final String url_image = remoteMessage.getData().get("img_url");


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                //Bitmap bitmap = drawable.getBitmap();
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                Picasso.with(getApplicationContext())
                        .load(url_image)
                        .resize(512,256)
                        .into(mTarget);
            }
        });

    }
}
