package whitelife.win.socketlibrary.message;


import com.alibaba.fastjson.JSON;

import whitelife.win.socketlibrary.SocketDataProtos;
import whitelife.win.socketlibrary.callback.MessageType;

/**
 * Created by wuzefeng on 2017/10/17.
 */

public class SocketImageMessage extends SocketMessage{

    public SocketImageMessage() {
        setMessageType(MessageType.MP);
    }
    public SocketImageMessage(SocketDataProtos.SocketData socketData) {
        this();
        setSocketData(socketData);
    }

}
