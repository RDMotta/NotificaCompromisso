package com.rdm.notificacompromisso.app.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.rdm.notificacompromisso.app.R;
import com.rdm.notificacompromisso.app.view.ActionNotificationActivity;

/**
 * Created by Robson Da Motta on 06/08/2017.
 */

public class NotificationCompromisso {

    private Context mContext;

    public NotificationCompromisso(Context context){
        mContext = context;
    }

    public NotificationCompat.Builder buildCompromisso(Compromisso compromisso){

        TaskStackBuilder stackBuilderActionConfirm = TaskStackBuilder.create(mContext);
        stackBuilderActionConfirm.addParentStack(ActionNotificationActivity.class);
        stackBuilderActionConfirm.addNextIntent(getActionIntentConfirm(compromisso));

        TaskStackBuilder stackBuilderActionCancel = TaskStackBuilder.create(mContext);
        stackBuilderActionCancel.addParentStack(ActionNotificationActivity.class);
        stackBuilderActionCancel.addNextIntent(getActionIntentCancel(compromisso));

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(compromisso.getAutor())
                        .setContentText(compromisso.getDescricao())
                        .addAction(R.drawable.ic_human_greeting, mContext.getString(R.string.confirm_txt) ,getActionConfirm(stackBuilderActionConfirm))
                        .addAction(R.drawable.ic_bell_off, mContext.getString(R.string.cancel_txt), getActionCancel(stackBuilderActionCancel));


        Intent resultIntent = new Intent(mContext, ActionNotificationActivity.class);
        String action_notification_show = mContext.getString(R.string.action_notification_show);

        resultIntent.putExtra(action_notification_show, compromisso);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(ActionNotificationActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_IMMUTABLE
                );
        mBuilder.setContentIntent(resultPendingIntent);

        return mBuilder;
    }

    protected Intent getActionIntentConfirm(Compromisso compromisso) {
        int idNotification = (int) compromisso.getIdentificador();

        String action_notification_confirm = mContext.getString(R.string.action_notification_confirm);
        String action_evento_notification = mContext.getString(R.string.action_evento_notification);
        Intent confirmNotificationIntent = new Intent(mContext, ActionNotificationActivity.class);

        confirmNotificationIntent.putExtra(action_evento_notification, compromisso);
        confirmNotificationIntent.putExtra(action_notification_confirm, action_notification_confirm);
        confirmNotificationIntent.putExtra(action_notification_confirm, idNotification);

        return confirmNotificationIntent;
    }

    protected Intent getActionIntentCancel(Compromisso compromisso) {
        int idNotification = (int) compromisso.getIdentificador();

        String action_evento_notification = mContext.getString(R.string.action_evento_notification);
        String id_notification_value = mContext.getString(R.string.id_notification_value);

        Intent cancelNotificationIntent = new Intent(mContext, ActionNotificationActivity.class);
        cancelNotificationIntent.putExtra(action_evento_notification, compromisso);
        cancelNotificationIntent.putExtra(id_notification_value, idNotification);

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
