package whitelife.win.socketlibrary.con;

import android.os.SystemClock;

/**
 * 心跳
 * Created by wuzefeng on 2017/10/17.
 */

public class SocketHeartSender {

    private static volatile SocketHeartSender instance;

    private HeartThread mHeardThread;

    private long lastTime;

    private static final int SLEEP_TIME=10000;


    public static void init(){
        if(instance==null){
            synchronized (SocketHeartSender.class){
                if(instance==null){
                    instance=new SocketHeartSender();
                }
            }
        }
    }


    public static SocketHeartSender get(){
        return instance;
    }


    private SocketHeartSender(){
        mHeardThread=new HeartThread();
        mHeardThread.start();
        updateReceiveTime();
    }

    public void updateReceiveTime(){
        lastTime=SystemClock.elapsedRealtime();
    }


    private class HeartThread extends Thread{

        @Override
        public void run() {

            try {
                while (true){
                    Thread.sleep(SLEEP_TIME);
                    if(SystemClock.elapsedRealtime()>lastTime+1000*60){//离上次收到时间大于1分钟，认为断开
//                        SocketHelper.getInstance().disConnected();
                    }else if(SystemClock.elapsedRealtime()>lastTime+8000){//离上次收到时间大于8秒，发送心跳
//                        SocketConnect.get().sendHeardMessage();
                    }
                }
            } catch (InterruptedException e) {

            }
        }
    }


    public void closeThread(){
        if(mHeardThread!=null)
            mHeardThread.interrupt();
        mHeardThread=null;
        instance=null;
    }


}
