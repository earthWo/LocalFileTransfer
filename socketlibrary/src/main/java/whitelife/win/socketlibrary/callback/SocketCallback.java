package whitelife.win.socketlibrary.callback;

import whitelife.win.socketlibrary.exception.SocketException;

/**
 * Created by wuzefeng on 2017/10/13.
 */

public interface SocketCallback {

    void connected();

    void connectError(SocketException e);

    void disconnected();


}
