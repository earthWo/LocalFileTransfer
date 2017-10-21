package whitelife.win.socketlibrary.message;


import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import whitelife.win.socketlibrary.callback.MessageType;

/**
 * Created by wuzefeng on 2017/10/17.
 */

public class SocketTextMessage extends SocketMessage{

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public SocketTextMessage() {
        setMessageType(MessageType.MT);
    }

    @Override
    public byte[] toByteArray() {
       return JSON.toJSONBytes(this);
    }
}
