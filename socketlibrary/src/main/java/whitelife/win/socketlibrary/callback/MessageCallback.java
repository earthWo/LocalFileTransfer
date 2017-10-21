package whitelife.win.socketlibrary.callback;

import whitelife.win.socketlibrary.exception.SocketException;
import whitelife.win.socketlibrary.message.SocketMessage;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public interface MessageCallback {


    void response(int messageId,SocketMessage message);

    void requestError(SocketMessage message, SocketException e);



}
