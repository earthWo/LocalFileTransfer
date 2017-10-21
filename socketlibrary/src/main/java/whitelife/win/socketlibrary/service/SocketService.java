package whitelife.win.socketlibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import whitelife.win.socketlibrary.callback.CallbackSet;
import whitelife.win.socketlibrary.callback.OrderType;
import whitelife.win.socketlibrary.con.SocketHelper;
import whitelife.win.socketlibrary.model.ServiceOrder;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public class SocketService extends Service {


    private SocketHelper mSocketHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        mSocketHelper=SocketHelper.getInstance();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        ServiceOrder serviceOrder=ServiceOrder.createOrder(intent);
        if(serviceOrder!=null)
        switch (serviceOrder.getOrder()){
            case OrderType.CONNECT:
                mSocketHelper.connect();
                break;
            case OrderType.SEND_TEXT_MESSAGE:
                mSocketHelper.sendTextMessage(serviceOrder.getMid(),serviceOrder.getData());
                break;
            case OrderType.CLOSE_CONNECTION:
                mSocketHelper.closeConnect();
                break;
            case OrderType.SEND_HEART_MESSAGE:
                mSocketHelper.sendHeardMessage(serviceOrder.getMid());
                break;
            case OrderType.SEND_VOICE_MESSAGE:
                mSocketHelper.sendVoiceMessage(serviceOrder.getMid(),serviceOrder.getData());
                break;
            case OrderType.SEND_VIDEO_MESSAGE:
                mSocketHelper.sendVideoMessage(serviceOrder.getMid(),serviceOrder.getData());
                break;
            case OrderType.SEND_IMAGE_MESSAGE:
                mSocketHelper.sendImageMessage(serviceOrder.getMid(),serviceOrder.getData());
                break;
            case OrderType.SEND_FILE_MESSAGE:
                mSocketHelper.sendFileMessage(serviceOrder.getMid(),serviceOrder.getData());
                break;
        }



        return super.onStartCommand(intent, flags, startId);
    }






}
