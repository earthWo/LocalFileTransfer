package whitelife.win.socketlibrary.con;

import com.google.protobuf.ByteString;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import whitelife.win.socketlibrary.SocketDataProtos;
import whitelife.win.socketlibrary.callback.MessageType;
import whitelife.win.socketlibrary.callback.SocketDataType;
import whitelife.win.socketlibrary.exception.SocketException;
import whitelife.win.socketlibrary.message.SocketMessage;
import whitelife.win.socketlibrary.utils.ByteUtil;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public class SocketMessageSender {

    private static volatile SocketMessageSender sInstance;

    private BlockingDeque<SocketMessage>mMessageDeuqe;

    private SendThread mThread;

    private Socket mSocket;

    private SocketMessageSender(Socket mSocket) {
        this.mSocket=mSocket;
        mMessageDeuqe=new LinkedBlockingDeque<>();
        mThread=new SendThread();
        mThread.setName("发送线程");
        mThread.start();
    }

    public static void init(Socket mSocket){
        if(sInstance==null){
            synchronized (SocketMessageSender.class){
                if(sInstance==null){
                    sInstance=new SocketMessageSender(mSocket);
                }
            }
        }else{
            sInstance.setSocket(mSocket);
        }
    }

    public static SocketMessageSender get(){
        return sInstance;
    }

    public void setSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }

    public void addMessage(SocketMessage socketMessage){
        try {
            mMessageDeuqe.putLast(socketMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class SendThread extends Thread{

        @Override
        public void run() {
            SocketMessage message=null;
            while(true){
                try {
                    message=mMessageDeuqe.takeFirst();
                    if(message.getMessageType().equals(MessageType.MC)) {
                        break;
                    }
                    OutputStream  outputStream = mSocket.getOutputStream();
                    byte[][] ms = getBytesByMessage(message);
                    for (byte[] m : ms) {
                        outputStream.write(m);
                    }
                    outputStream.flush();
                } catch (InterruptedException | IOException e) {
                    SocketHelper.getInstance().sendMessageError(message, new SocketException(e.getMessage()));
                }
            }
        }
    }


    private byte[][] getBytesByMessage(SocketMessage message){
        byte[][]ms;


        SocketDataProtos.SocketData socketData=SocketDataProtos.SocketData.newBuilder()
                .setMessageId(message.getMessageId())
                .setMessageType(message.getMessageType())
                .setText(message.getText())
                .setData(ByteString.copyFrom(message.getData()))
                .build();
        byte[]messageByte=socketData.toByteArray();
        int messageLength=messageByte.length;
        //普通消息
        if(message.getMessageType()!= SocketDataType.MH){
            //大于1000分包发送
            if(messageLength>=256){

                byte[][]msb=ByteUtil.splitBytes(messageByte,256);
                ms=new byte[msb.length][];

                for(int i=0;i<msb.length;i++){
                    if(i==msb.length-1){
                        ms[i]= ByteUtil.unitByteArray(ByteUtil.unitByteArray(SocketDataType.ME.getBytes(),ByteUtil.intToByteArray(msb[i].length)),msb[i]);
                    }else{
                        ms[i]= ByteUtil.unitByteArray(ByteUtil.unitByteArray(SocketDataType.ML.getBytes(),ByteUtil.intToByteArray(msb[i].length)),msb[i]);
                    }
                }
            }else{
                ms=new byte[1][];
                ms[0]= ByteUtil.unitByteArray(ByteUtil.unitByteArray(SocketDataType.MG.getBytes(),ByteUtil.intToByteArray(messageLength)),messageByte);
                return ms;
            }
        }else{//心跳消息

            ms=new byte[1][];
            ms[0]= ByteUtil.unitByteArray(ByteUtil.unitByteArray(SocketDataType.MH.getBytes(),ByteUtil.intToByteArray(messageLength)),messageByte);
            return ms;
        }
        return ms;
    }

    public void closeThread(){
        mThread=null;
        sInstance=null;
    }





}
