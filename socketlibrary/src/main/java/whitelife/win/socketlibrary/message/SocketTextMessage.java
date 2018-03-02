package whitelife.win.socketlibrary.message;


import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import whitelife.win.socketlibrary.SocketDataProtos;
import whitelife.win.socketlibrary.callback.MessageType;

/**
 * Created by wuzefeng on 2017/10/17.
 */

public class SocketTextMessage extends SocketMessage{


    public SocketTextMessage() {
        setMessageType(MessageType.MT);
    }

    public SocketTextMessage(SocketDataProtos.SocketData socketData) {
        this();
        setSocketData(socketData);
    }

}
