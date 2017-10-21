package whitelife.win.socketlibrary.con;


import android.content.Context;
import android.content.Intent;

import java.io.File;

import whitelife.win.socketlibrary.callback.CallbackSet;
import whitelife.win.socketlibrary.callback.MessageCallback;
import whitelife.win.socketlibrary.callback.OrderType;
import whitelife.win.socketlibrary.callback.SocketCallback;
import whitelife.win.socketlibrary.model.ServiceOrder;
import whitelife.win.socketlibrary.service.SocketService;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public class SocketConnect {

    private static volatile SocketConnect sInstance;

    private SocketHelper mSocketHelper;

    private Context mContext;

    private SocketConnect(Context context){
        mSocketHelper=SocketHelper.getInstance();
        this.mContext=context;
        context.getApplicationContext().startService(new Intent(context, SocketService.class));
    }

    public static void init(Context context){
        if(sInstance==null){
            synchronized (SocketConnect.class){
                if(sInstance==null){
                    sInstance=new SocketConnect(context);
                }
            }
        }
    }

    public static SocketConnect get(){
        return sInstance;
    }

    public SocketConnect setSocketCallback(SocketCallback mSocketCallback) {
        mSocketHelper.setSocketCallback(mSocketCallback);
        return this;
    }


    public SocketConnect setDefaultMessageCallback(MessageCallback callback) {
        mSocketHelper.setDefaultCallback(callback);
        return this;
    }


    public void connect(){
        Intent intent= ServiceOrder.createIntent(OrderType.CONNECT,null);
        startService(intent);
    }

    private void startService(Intent intent){
        intent.setClassName(mContext.getPackageName(),SocketService.class.getName());
        mContext.getApplicationContext().startService(intent);
    }

    public void sendTextMessage(String message){
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_TEXT_MESSAGE,message);
        startService(intent);
    }


    public void sendSyncTextMessage(String message, MessageCallback callback){
        CallbackSet.get().addCallback(CallbackSet.decodeMessageId(message),callback);
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_TEXT_MESSAGE,message);
        startService(intent);
    }


    public void sendVoiceMessage(File file){
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_VOICE_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }

    public void sendSyncVoiceMessage(File file, MessageCallback callback){
        CallbackSet.get().addCallback(CallbackSet.decodeMessageId(System.currentTimeMillis()+""),callback);
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_VOICE_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }


    public void sendImageMessage(File file){
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_IMAGE_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }

    public void sendSyncImageMessage(File file,MessageCallback callback){
        CallbackSet.get().addCallback(CallbackSet.decodeMessageId(System.currentTimeMillis()+""),callback);
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_IMAGE_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }

    public void sendVideoMessage(File file){
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_VIDEO_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }

    public void sendSyncVideoMessage(File file,MessageCallback callback){
        CallbackSet.get().addCallback(CallbackSet.decodeMessageId(System.currentTimeMillis()+""),callback);
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_VIDEO_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }

    public void sendFileMessage(File file){
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_FILE_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }

    public void sendSyncFileMessage(File file,MessageCallback callback){
        CallbackSet.get().addCallback(CallbackSet.decodeMessageId(System.currentTimeMillis()+""),callback);
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_FILE_MESSAGE,file.getAbsolutePath());
        startService(intent);
    }







    public void sendHeardMessage(){
        Intent intent= ServiceOrder.createIntent(OrderType.SEND_HEART_MESSAGE,"");
        startService(intent);
    }


    public void closeConnect(){
        Intent intent= ServiceOrder.createIntent(OrderType.CLOSE_CONNECTION,"");
        startService(intent);
    }

}
