package whitelife.win.socketlibrary.con;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import whitelife.win.socketlibrary.callback.CallbackSet;
import whitelife.win.socketlibrary.callback.MessageCallback;
import whitelife.win.socketlibrary.callback.MessageType;
import whitelife.win.socketlibrary.callback.SocketCallback;
import whitelife.win.socketlibrary.config.SocketConfig;
import whitelife.win.socketlibrary.exception.SocketException;
import whitelife.win.socketlibrary.message.SocketFileMessage;
import whitelife.win.socketlibrary.message.SocketImageMessage;
import whitelife.win.socketlibrary.message.SocketMessage;
import whitelife.win.socketlibrary.message.SocketTextMessage;
import whitelife.win.socketlibrary.message.SocketVideoMessage;
import whitelife.win.socketlibrary.message.SocketVoiceMessage;
import whitelife.win.socketlibrary.utils.ByteUtil;

/**
 * 负责socket的连接传输
 * Created by wuzefeng on 2017/10/13.
 */

public class SocketHelper {

    private static volatile SocketHelper sInstance;

    private Socket mSocket;

    private SocketCallback mSocketCallback;

    private ConnectThread mConnectThread;

    private SocketHelper(){}

    private MessageCallback defaultCallback;

    public static SocketHelper getInstance(){
        if(sInstance==null){
            synchronized (SocketHelper.class){
                if(sInstance==null){
                    sInstance=new SocketHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * 连接socket
     */
    public void connect(){
        if(mConnectThread==null){
            mConnectThread=new ConnectThread();
            mConnectThread.setName("连接线程");
            mConnectThread.start();
        }
    }

    public MessageCallback getDefaultCallback() {
        return defaultCallback;
    }

    public void setDefaultCallback(MessageCallback defaultCallback) {
        this.defaultCallback = defaultCallback;
    }

    public void setSocketCallback(SocketCallback mSocketCallback) {
        this.mSocketCallback = mSocketCallback;
    }

    public void connectSuccess(){
        SocketMessageSender.init(mSocket);
        SocketMessageReceiver.init(mSocket);
        SocketHeartSender.init();
        if(mSocketCallback!=null){
            mSocketCallback.connected();
        }
    }

    private void connectFailure(IOException e){
        if(mSocketCallback!=null){
            mSocketCallback.connectError(new SocketException(e.getMessage()));
        }
        mSocket=null;
    }

    public void closeConnect(){
        try {
            if(mSocket!=null) {
                mSocket.close();
            }
            mSocket=null;
            mConnectThread=null;
            if(SocketMessageReceiver.get()!=null) {
                SocketMessageReceiver.get().closeThread();
            }
            if(SocketMessageSender.get()!=null) {
                SocketMessageSender.get().closeThread();
            }
            if(SocketHeartSender.get()!=null) {
                SocketHeartSender.get().closeThread();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(int messageId,String message){
        if(mSocket!=null) {
            SocketTextMessage socketMessage = new SocketTextMessage();
            socketMessage.setMessageId(messageId);
            socketMessage.setData(message.getBytes());
            socketMessage.setText(message);
            SocketMessageSender.get().addMessage(socketMessage);
        }
    }

    public void sendVoiceMessage(int messageId,String filePath){
        if(mSocket!=null) {
            SocketVoiceMessage socketMessage = new SocketVoiceMessage();
            socketMessage.setMessageId(messageId);
            try {
                socketMessage.setData(ByteUtil.fileToByte(filePath));
            } catch (IOException e) {
                MessageCallback callback=CallbackSet.get().getCallback(messageId);
                if(callback!=null){
                    callback.requestError(socketMessage,new SocketException(e.getMessage()));
                }else{
                    throw new SocketException(e.getMessage());
                }
            }
            socketMessage.setText(filePath.substring(filePath.lastIndexOf("/")+1));
            SocketMessageSender.get().addMessage(socketMessage);
        }
    }

    public void sendVideoMessage(int messageId,String filePath){
        if(mSocket!=null) {
            SocketVideoMessage socketMessage = new SocketVideoMessage();
            socketMessage.setMessageId(messageId);
            try {
                socketMessage.setData(ByteUtil.fileToByte(filePath));
            } catch (IOException e) {
                MessageCallback callback=CallbackSet.get().getCallback(messageId);
                if(callback!=null){
                    callback.requestError(socketMessage,new SocketException(e.getMessage()));
                }else{
                    throw new SocketException(e.getMessage());
                }
            }
            socketMessage.setText(filePath.substring(filePath.lastIndexOf("/")+1));
            SocketMessageSender.get().addMessage(socketMessage);
        }
    }

    public void sendImageMessage(int messageId,String filePath){
        if(mSocket!=null) {
            SocketImageMessage socketMessage = new SocketImageMessage();
            socketMessage.setMessageId(messageId);
            try {
                socketMessage.setData(ByteUtil.fileToByte(filePath));
            } catch (IOException e) {
                MessageCallback callback=CallbackSet.get().getCallback(messageId);
                if(callback!=null){
                    callback.requestError(socketMessage,new SocketException(e.getMessage()));
                }else{
                    throw new SocketException(e.getMessage());
                }
            }
            socketMessage.setText(filePath.substring(filePath.lastIndexOf("/")+1));
            SocketMessageSender.get().addMessage(socketMessage);
        }
    }

    public void sendFileMessage(int messageId,String filePath){
        if(mSocket!=null) {
            SocketFileMessage socketMessage = new SocketFileMessage();
            socketMessage.setMessageId(messageId);
            try {
                socketMessage.setData(ByteUtil.fileToByte(filePath));
            } catch (IOException e) {
                MessageCallback callback=CallbackSet.get().getCallback(messageId);
                if(callback!=null){
                    callback.requestError(socketMessage,new SocketException(e.getMessage()));
                }else{
                    SocketHelper.getInstance().getDefaultCallback().requestError(socketMessage,new SocketException(e.getMessage()));
                }
            }
            socketMessage.setText(filePath.substring(filePath.lastIndexOf("/")+1));
            SocketMessageSender.get().addMessage(socketMessage);
        }
    }

    public void sendHeardMessage(int messageId){
        if(mSocket!=null) {
            SocketTextMessage socketMessage = new SocketTextMessage();
            socketMessage.setMessageId(messageId);
            socketMessage.setData("这是一个心跳".getBytes());
            socketMessage.setMessageType(MessageType.MH);
            socketMessage.setText("这是一个心跳");
            SocketMessageSender.get().addMessage(socketMessage);
        }
    }

    public void disConnected(){
        if(mSocketCallback!=null){
            mSocketCallback.disconnected();
        }
    }

    public SocketCallback getSocketCallback() {
        return mSocketCallback;
    }

    public void sendMessageError(SocketMessage message, SocketException e){
        if(message!=null){
            MessageCallback callback= CallbackSet.get().getCallback(message.getMessageId());
            if(callback!=null){
                callback.requestError(message, e);
            }
        }
    }

    private class ConnectThread extends Thread{

        @Override
        public void run() {
            //未初始化或未连接
            if(mSocket==null){
                mSocket=new Socket();
            }
            if(!mSocket.isConnected()){
                SocketAddress socketAddress=new InetSocketAddress(SocketConfig.get().getIp(),SocketConfig.get().getPort());
                try {
                    mSocket.connect(socketAddress);
                    connectSuccess();
                } catch (IOException e) {
                    e.printStackTrace();
                    connectFailure(e);
                    mSocket=null;
                }
            }else{
                if(mSocketCallback!=null){
                    mSocketCallback.connectError(new SocketException("不能连接多次"));
                }
            }
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    private Handler mHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            SocketMessage message= (SocketMessage) msg.obj;
            if(message!=null) {
                MessageCallback callback = CallbackSet.get().getCallback(message.getMessageId());
                if (callback != null) {
                    callback.response(message.getMessageId(),message);
                }else if(SocketHelper.getInstance().getDefaultCallback()!=null){
                    SocketHelper.getInstance().getDefaultCallback().response(message.getMessageId(),message);
                }
            }
        }
    };

}
