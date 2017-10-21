package whitelife.win.socketlibrary.con;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public class SocketMessageReceiver {

    private static volatile SocketMessageReceiver sInstance;

    private Socket mSocket;

    private ReceiverThread mReceiverThread;


    private SocketMessageReceiver(Socket mSocket) {
        this.mSocket = mSocket;
        mReceiveQueue=new LinkedBlockingQueue<>();
        mReceiverThread=new ReceiverThread();
        mReceiverThread.setName("接收线程");
        mReceiverThread.start();
    }

    public void setSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }

    private LinkedBlockingQueue<byte[]> mReceiveQueue;

    public  LinkedBlockingQueue<byte[]> getReceiveDeque() {
        if(mReceiveQueue==null){
            mReceiveQueue= new LinkedBlockingQueue<>();
        }
        return mReceiveQueue;
    }

    public static void init(Socket mSocket){
        if(sInstance==null){
            synchronized (SocketMessageReceiver.class){
                if(sInstance==null){
                    sInstance=new SocketMessageReceiver(mSocket);
                }
            }
        }else{
            sInstance.setSocket(mSocket);
        }
    }


    public void closeThread(){
        if(SocketDataParse.get()!=null)
            SocketDataParse.get().closeThread();
        if(mReceiveQueue!=null)
            mReceiveQueue.clear();
        mReceiveQueue=null;
        mReceiverThread=null;
        sInstance=null;
    }



    public static SocketMessageReceiver get(){
        return sInstance;
    }


    private class ReceiverThread extends Thread{

        @Override
        public void run() {
            try {
                SocketDataParse.init();
                InputStream inputStream = mSocket.getInputStream();
                while(true){
                    byte[] buffer=new byte[256];
                    int len;
                    if(mSocket!=null&&(len=inputStream.read(buffer))!=-1) {
                        mReceiveQueue.put(Arrays.copyOf(buffer, len));
                        if(SocketHeartSender.get()!=null)
                            SocketHeartSender.get().updateReceiveTime();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}

