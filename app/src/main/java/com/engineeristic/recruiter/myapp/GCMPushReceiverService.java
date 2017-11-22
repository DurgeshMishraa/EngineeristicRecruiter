package com.engineeristic.recruiter.myapp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.chat.ChatConstants;
import com.engineeristic.recruiter.chat.ChatManager;
import com.engineeristic.recruiter.chat.ChatPreferences;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GCMPushReceiverService extends FirebaseMessagingService {

    private static ArrayList<String> cEventList = new ArrayList<String>();
    public static ArrayList<String> eventChat = new ArrayList<String>();
    public static HashMap<String,ArrayList<String>> hashMapCandidatelist = new HashMap<>();
    public static final String PUSH_NOTIFICATION = "pushNotification";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            try {
                if (remoteMessage.getNotification() != null) {
                    handleNotification(remoteMessage.getNotification().getBody());
                }
               // ChatPreferences.getChatSharedInstance().setContextPreference(getApplicationContext());
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.e("GCM PUSHRECEIVERSERVICE", "json: " + json);
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("GCM PUSHRECEIVERSERVICE", "Exception: " + e.getMessage());
            }
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
    private void handleNotification(String message) {
        if (!isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }
    public void playNotificationSound() {
        try {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDataMessage(JSONObject bundle) {
        try {
        JSONObject data = bundle.optJSONObject("data");
        String type = data.optString("type");
        //String type = data.optString("data");
        //String title = data.optString("messageTitle");
         String title = data.optString("title");
        String message = data.optString("message");
        //String data = bundle.getString("data");
        String senderId = data.optString("senderid");
        String appliedId =  data.optString("appliedId");
        String jobId = data.optString("jobid");
        String jobName = data.optString("jobname");
        String dataChannel = data.optString("datachannel");
        String followUp = data.optString("followup");
        if(data != null
                && title != null
                && !title.equals("")
                && message != null
                && !message.equals(""))
        {
            showPushNotification(getApplicationContext(), message, title, type,senderId, appliedId, followUp, jobId, jobName,dataChannel, senderId);
        }
        }
         catch (Exception e) {
            Log.e("GCMPUSHRECEIVERSERVICE2", "Exception: " + e.getMessage());
        }
    }
    public void showPushNotification(Context context, String msgText, String title, String data, String senderId, String appliedId, String follUp, String jobId, String jobName, String dataChannel, String chatId)
    {
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long when = System.currentTimeMillis();
        Constant.USER_COOKIE = MySharedPreference.getKeyValue(Constant.KEY_USER_COOKIE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            if(!Constant.USER_COOKIE.equals("")){
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 0, 0,0, 0, 0 })
                        .setSound(uri)
                        //.setColor(Color.TRANSPARENT)
                        .setSmallIcon(R.drawable.smallicon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msgText))
                        .setContentText(msgText);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                switch(data){
                    case "5": // For Chat Notification
                        if(dataChannel != null
                                && !dataChannel.equals("")
                                && !dataChannel.equals("0")
                                && chatId != null
                                && !chatId.equals("")
                                && !chatId.equals("0")
                                && uri != null
                                && !uri.equals("")){
                            AccountUtils.trackEvents("MyChat", "rtViewChatMessage",
                                    "Origin= "+"PushNotification"+" ,UserId= "+senderId+" ,JobId= "+jobId, null);
                            ChatManager.getInstance().initializePubNub();
                            ChatPreferences.getChatSharedInstance().setContextPreference(getApplication().getApplicationContext());
                            setLargeDataNotification(context, msgText, title, data, "", dataChannel, chatId, when, uri);
                        }
                        break;
                    case "7": // For User Candidates List
                        if(jobId != null
                                && !jobId.equals("")
                                && !jobId.equals("0")
                                && jobName != null
                                && !jobName.equals("")){
                            //Intent resultIntent02 = new Intent(this, CandidateListActivity.class);
                            AccountUtils.trackEvents("NewCandidateListActivity", "rtViewBulkAppliedCandidate",
                                    "Origin= "+"PushNotification"+" ,UserId= "+senderId+" ,JobId= "+jobId, null);
                            Intent resultIntent02 = new Intent(this, NewCandidateListActivity.class);
                            resultIntent02.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle bundle07 = new Bundle();
                            bundle07.putString(Constant.KEY_JOB_ID, jobId);
                            bundle07.putString(Constant.KEY_DATA_TYPE, data);
                            bundle07.putString(Constant.KEY_JOB_TITLE, jobId + ": " + jobName);
                            bundle07.putBoolean(Constant.KEY_NOTIFICATION_LANDS, true);
                            resultIntent02.putExtras(bundle07);
                            // Adds the back stack for the Intent (but not the Intent itself)
                            //stackBuilder.addParentStack(CandidateListActivity.class);
                            stackBuilder.addParentStack(NewCandidateListActivity.class);
                            // Adds the Intent that starts the Activity to the top of the stack
                            stackBuilder.addNextIntent(resultIntent02);
                            PendingIntent pendingIntent02 = stackBuilder.getPendingIntent((int)when, PendingIntent.FLAG_UPDATE_CURRENT);
                            //PendingIntent.getActivity(context,(int) when, resultIntent02, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pendingIntent02);
                            // mId allows you to update the notification later on.
                            NotificationCompat.InboxStyle inboxStyle07 = new NotificationCompat.InboxStyle();
                            if(hashMapCandidatelist!=null && hashMapCandidatelist.get(jobId)!=null && hashMapCandidatelist.get(jobId).size()>0)
                            {
                                Utility.cancelNotificationID(Integer.parseInt(jobId));
                                cEventList=new ArrayList<String>();
                                cEventList=hashMapCandidatelist.get(jobId);
                            }
                            else
                            {
                                cEventList=new ArrayList<String>();
                            }
                            String[] str=msgText.split("~");
                            for (int i = 0; i < str.length; i++) {
                                cEventList.add(msgText);
                            }
                            //cEventList.add(msgText);
                            inboxStyle07.setBigContentTitle(title);
                            for (int i = cEventList.size()-1; i>-1; i--){
                                if(i > cEventList.size()-6){
                                    inboxStyle07.addLine(cEventList.get(i));
                                }
                                else{
                                    inboxStyle07.addLine("Total Applied : "+cEventList.size());
                                    break;
                                }
                            }
                            hashMapCandidatelist.put(jobId, cEventList);
                            //MySharedPreference.setCandidateAppliedList(hashMap);
                            mBuilder.setNumber(cEventList.size());
                            mBuilder.setStyle(inboxStyle07);
                            mNotificationManager.notify(Integer.parseInt(jobId), mBuilder.build());
                        }
                        break;
                    case "9": // For User Candidates details
                        if(appliedId != null
                                && !appliedId.equals("")
                                && !appliedId.equals("0")
                                && follUp != null
                                && !follUp.equals("")
                                && jobId != null
                                && !jobId.equals("")
                                && !jobId.equals("0")
                                && jobName != null
                                && !jobName.equals("")){

                            if(hashMapCandidatelist!=null && hashMapCandidatelist.get(jobId)!=null && hashMapCandidatelist.get(jobId).size()>0)
                            {
                                showPushNotification(context,msgText, title,"7",senderId, appliedId, follUp, jobId, jobName,dataChannel, chatId);
                            }
                            else
                            {
                                AccountUtils.trackEvents("NewCandidateDetailsActivity", "rtViewAppliedCandidate",
                                        "Origin= "+"PushNotification"+" ,UserId= "+senderId+" ,JobId= "+jobId, null);
                                cEventList=new ArrayList<String>();
                                cEventList.add(msgText);
                                hashMapCandidatelist.put(jobId, cEventList);
                                Intent resultIntent04 = new Intent(this, NewCandidateDetailsActivity.class);
                                //Intent resultIntent04 = new Intent(this, ProfileResumeActivity.class);
                                //resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Bundle bundle09 = new Bundle();
                                bundle09.putString(Constant.KEY_VIEW_ID, appliedId);
                                bundle09.putString(Constant.KEY_FOLLOW_UP, follUp);
                                bundle09.putString(Constant.KEY_JOB_TITLE, jobId + ": " + jobName);
                                bundle09.putString(Constant.KEY_JOB_ID, jobId);
                                resultIntent04.putExtras(bundle09);
                                // Adds the back stack for the Intent (but not the Intent itself)
                                //stackBuilder.addParentStack(ProfileResumeActivity.class);
                                stackBuilder.addParentStack(NewCandidateDetailsActivity.class);
                                // Adds the Intent that starts the Activity to the top of the stack
                                stackBuilder.addNextIntent(resultIntent04);
                                PendingIntent pendingIntent04 = stackBuilder.getPendingIntent((int) when, PendingIntent.FLAG_UPDATE_CURRENT);
                                mBuilder.setContentIntent(pendingIntent04);
                                // mId allows you to update the notification later on.
                                //mNotificationManager.notify((int) when, mBuilder.build());
                                mNotificationManager.notify(Integer.parseInt(jobId), mBuilder.build());
                            }
                        }
                        break;
                }
                }
            else{
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setAutoCancel(true)
                                .setVibrate(new long[] {0, 0,0, 0, 0})
                                .setSound(uri)
                                //.setColor(Color.TRANSPARENT)
                                .setSmallIcon(R.drawable.smallicon)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                                .setContentTitle(title)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(msgText))
                                .setContentText(msgText);
                Intent resultIntent = new Intent(this, SplashActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(SplashActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(context,(int) when, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.
                mNotificationManager.notify((int) when, mBuilder.build());	// Cokkies is null so notification will not generate
            }
        }
        else
        {
            if(!Constant.USER_COOKIE.equals("")){
                NotificationManager notificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                @SuppressWarnings("deprecation")
                Notification notification = new Notification(R.drawable.app_icon, msgText, when);
                switch(data){
                    case "5":
                        if(dataChannel != null
                                && !dataChannel.equals("")
                                && !dataChannel.equals("0")
                                && chatId != null
                                && !chatId.equals("")
                                && !chatId.equals("0")
                                && uri != null
                                && !uri.equals("")){
                            AccountUtils.trackEvents("MyChat", "rtViewChatMessage",
                                    "Origin= "+"PushNotification"+" ,UserId= "+senderId+" ,JobId= "+jobId, null);
                            ChatManager.getInstance().initializePubNub();
                            setLargeDataNotification(context, msgText, title, data, "", dataChannel, chatId, when, uri);
                        }
                        break;
                    case "7": // For User Candidates List
                        if(jobId != null
                                && !jobId.equals("")
                                && !jobId.equals("0")
                                && jobName != null
                                && !jobName.equals("")){
                           // Intent intent07 = new Intent(this, CandidateListActivity.class);
                            AccountUtils.trackEvents("NewCandidateListActivity", "rtViewBulkAppliedCandidate",
                                    "Origin= "+"PushNotification"+" ,UserId= "+senderId+" ,JobId= "+jobId, null);
                            Intent intent07 = new Intent(this, NewCandidateListActivity.class);
                            Bundle bundle07 = new Bundle();
                            bundle07.putString(Constant.KEY_JOB_ID, jobId);
                            bundle07.putString(Constant.KEY_DATA_TYPE, data);
                            bundle07.putString(Constant.KEY_JOB_TITLE, jobId + ": " + jobName);
                            bundle07.putBoolean(Constant.KEY_NOTIFICATION_LANDS, true);
                            intent07.putExtras(bundle07);
                            intent07.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pIntent07 =
                                    PendingIntent.getActivity(context,(int) when, intent07, PendingIntent.FLAG_UPDATE_CURRENT);
                            try {
                                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                                deprecatedMethod.invoke(notification, context, title, msgText+data, intent07);
                            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                                    | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            notification.flags |= Notification.FLAG_AUTO_CANCEL;
                            notification.defaults |= Notification.DEFAULT_SOUND;
                            notification.defaults |= Notification.DEFAULT_VIBRATE;
                            notificationManager.notify((int) when, notification);
                        }
                        break;

                    case "9": // For User Candidates List
                        if(appliedId != null
                                && !appliedId.equals("")
                                && !appliedId.equals("0")
                                && follUp != null
                                && !follUp.equals("")
                                && jobId != null
                                && !jobId.equals("")
                                && !jobId.equals("0")
                                && jobName != null
                                && !jobName.equals("")){
                            AccountUtils.trackEvents("NewCandidateDetailsActivity", "rtViewAppliedCandidate",
                                    "Origin= "+"PushNotification"+" ,UserId= "+senderId+" ,JobId= "+jobId, null);
                            Intent intent09 = new Intent(this, NewCandidateDetailsActivity.class);
                            //Intent intent09 = new Intent(this, ProfileResumeActivity.class);
                            //resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle bundle09 = new Bundle();
                            bundle09.putString(Constant.KEY_VIEW_ID, appliedId);
                            bundle09.putString(Constant.KEY_FOLLOW_UP, follUp);
                            bundle09.putString(Constant.KEY_JOB_TITLE, jobId + ": " + jobName);
                            bundle09.putString(Constant.KEY_JOB_ID, jobId);

                            intent09.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pIntent09 =
                                    PendingIntent.getActivity(context,(int) when, intent09, PendingIntent.FLAG_UPDATE_CURRENT);
                            try {
                                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                                deprecatedMethod.invoke(notification, context, title, msgText+data, intent09);
                            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                                    | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            notification.flags |= Notification.FLAG_AUTO_CANCEL;
                            notification.defaults |= Notification.DEFAULT_SOUND;
                            notification.defaults |= Notification.DEFAULT_VIBRATE;
                            notificationManager.notify((int) when, notification);
                        }
                        break;
                }
            }
            else{
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                @SuppressWarnings("deprecation")
                Notification notification = new Notification(R.drawable.app_icon, msgText, when);
                Intent notificationIntent = new Intent(context, SplashActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent intent =  PendingIntent.getActivity(context,(int) when, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                    deprecatedMethod.invoke(notification, context, title, msgText+data, notificationIntent);
                } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    e.printStackTrace();
                }
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                notificationManager.notify((int) when, notification);
            }
        }
    }

    private void setLargeDataNotification(Context context, String msgText,String title,String data,String imageUrl,String dataChannel,String chatId,long when,Uri uri)
    {
        if(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.PUSH_NOTIFICATION_ON).equalsIgnoreCase("0") )
        {
            return;
        }
        setchatCountofNotifications(dataChannel);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setVibrate(new long[] { 0, 0,0, 0, 0 })
                        .setSound(uri)
                        //.setColor(Color.TRANSPARENT)
                        .setSmallIcon(R.drawable.smallicon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msgText))
                        .setContentText(msgText);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        eventChat.add(msgText);
        //parseData(msgText);
        inboxStyle.setBigContentTitle(title);
        for (int i=eventChat.size()-1; i>-1; i--) {
			/*for (int i=0; i<event.size(); i++) {*/
            if(i>eventChat.size()-6)
            {
                inboxStyle.addLine(eventChat.get(i));
            }
            else
            {
                inboxStyle.addLine("Total unread messages  :: "+ChatManager.getInstance().setCompleteChatCount());
                break;
            }
        }
        mBuilder.setNumber(ChatManager.getInstance().setCompleteChatCount());
        mBuilder.setStyle(inboxStyle);
        Intent resultIntent=null;
        if(Constant.USER_COOKIE==null&&data.equalsIgnoreCase("5"))
        {
            resultIntent = new Intent(this, SplashActivity.class);
        }
        else
        {
            resultIntent = new Intent(this, JobsListActivity.class);
        }
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("title", "chat");
        bundle.putString("chatId", chatId);
        bundle.putString("channelname",dataChannel);
        resultIntent.putExtras(bundle);
        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(this);
        //Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(JobsListActivity.class);
        //Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context,(int) when, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(9999, mBuilder.build());
    }

    private void setchatCountofNotifications(String channel)
    {
        try {
            if(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.PUSH_NOTIFICATION_ON).equalsIgnoreCase("0") )
            {
                return;
            }
            ChatManager.getInstance().chatCount=ChatPreferences.getChatSharedInstance().
                    getChatPreferenceChatCount(ChatConstants.CHAT_COUNT);
            if(ChatManager.getInstance().chatCount!=null && channel!=null)
            {
                if(ChatManager.getInstance().chatCount.get(channel)!=null )

                {
                    int count=ChatManager.getInstance().chatCount .get(channel)+1;
                    ChatManager.getInstance().chatCount .put(channel, count);
                    ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT,
                            ChatManager.getInstance().chatCount);
                }
                else
                {
                    ChatManager.getInstance().chatCount.put(channel,1);
                    ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT, ChatManager.getInstance().chatCount);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
