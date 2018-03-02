package whitelife.win.socketlibrary.message;


import com.alibaba.fastjson.JSON;

import whitelife.win.socketlibrary.SocketDataProtos;
import whitelife.win.socketlibrary.callback.MessageType;

/**
 * Created by wuzefeng on 2017/10/17.
 */

public class SocketVideoMessage extends SocketMessage{

    public SocketVideoMessage() {
        setMessageType(MessageType.MM);
    }

    public SocketVideoMessage(SocketDataProtos.SocketData socketData) {
        this();
        setSocketData(socketData);
    }

}
