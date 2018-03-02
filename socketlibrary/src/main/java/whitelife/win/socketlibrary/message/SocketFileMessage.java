package whitelife.win.socketlibrary.message;


import whitelife.win.socketlibrary.SocketDataProtos;
import whitelife.win.socketlibrary.callback.MessageType;

/**
 * Created by wuzefeng on 2017/10/17.
 */

public class SocketFileMessage extends SocketMessage{

    public SocketFileMessage() {
        setMessageType(MessageType.MF);
    }

    public SocketFileMessage(SocketDataProtos.SocketData socketData) {
        this();
        setSocketData(socketData);
    }

}
