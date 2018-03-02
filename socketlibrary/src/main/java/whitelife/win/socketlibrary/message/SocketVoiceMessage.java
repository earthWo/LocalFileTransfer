package whitelife.win.socketlibrary.message;

import whitelife.win.socketlibrary.SocketDataProtos;
import whitelife.win.socketlibrary.callback.MessageType;

/**
 * Created by wuzefeng on 2017/10/17.
 */

public class SocketVoiceMessage extends SocketMessage{

    public SocketVoiceMessage() {
        setMessageType(MessageType.MV);
    }

    public SocketVoiceMessage(SocketDataProtos.SocketData socketData) {
        this();
        setSocketData(socketData);
    }
}
