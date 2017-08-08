package com.rdm.notificacompromisso.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.view.ActionNotificationActivity;
import com.rdm.notificacompromisso.view.MainActivity;

/**
 * Created by Robson Da Motta on 06/08/2017.
 */

public class NotificationCompromisso {

    private Context mContext;

    public NotificationCompromisso(Context context){
        mContext = context;
    }

    public NotificationCompat.Builder buildCompromisso(Compromisso compromisso){
        int idNotification = (int) compromisso.getIdentificador();

        TaskStackBuilder stackBuilderActionConfirm = TaskStackBuilder.create(mContext);
        stackBuilderActionConfirm.addParentStack(ActionNotificationActivity.class);
        stackBuilderActionConfirm.addNextIntent(getActionIntentConfirm(idNotification));

        TaskStackBuilder stackBuilderActionCancel = TaskStackBuilder.create(mContext);
        stackBuilderActionCancel.addParentStack(ActionNotificationActivity.class);
        stackBuilderActionCancel.addNextIntent(getActionIntentCancel(idNotification));

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(compromisso.getAutor())
                        .setContentText(compromisso.getDescricao())
                        .addAction(android.R.drawable.ic_input_add, mContext.getString(R.string.confirm_txt) ,getActionConfirm(stackBuilderActionConfirm))
                        .addAction(android.R.drawable.ic_menu_close_clear_cancel, mContext.getString(R.string.cancel_txt), getActionCancel(stackBuilderActionCancel));


        Intent resultIntent = new Intent(mContext, MainActivity.class);
        String action_notification_confirm = mContext.getString(R.string.action_notification_confirm);
        resultIntent.putExtra(action_notification_confirm, compromisso);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_IMMUTABLE
                );
        mBuilder.setContentIntent(resultPendingIntent);

        return mBuilder;
    }

    protected Intent getActionIntentConfirm(long id) {
        int idNotification = (int) id;
        String action_notification_confirm = mContext.getString(R.string.action_notification_confirm);
        Intent confirmNotificationIntent = new Intent(mContext, ActionNotificationActivity.class);
        confirmNotificationIntent.putExtra(action_notification_confirm, action_notification_confirm);
        confirmNotificationIntent.putExtra(mContext.getString(R.string.id_notification_value), idNotification);

        return confirmNotificationIntent;
    }

    protected Intent getActionIntentCancel(long id) {
        int idNotification = (int) id;
        Intent cancelNotificationIntent = new Intent(mContext, ActionNotificationActivity.class);
        cancelNotificationIntent.putExtra(mContext.getString(R.string.id_notification_value), idNotification);

        return cancelNotificationIntent;
    }

    protected PendingIntent getActionConfirm(TaskStackBuilder stackBuilderAction) {
        PendingIntent actionConfirmPendingIntent =
                stackBuilderAction.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        return actionConfirmPendingIntent;
    }

    protected PendingIntent getActionCancel(TaskStackBuilder stackBuilderAction) {
        PendingIntent actionCancelPendingIntent =
                stackBuilderAction.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        return actionCancelPendingIntent;
    }

}
