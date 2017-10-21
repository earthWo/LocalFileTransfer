## ZFLocalFileTransfer

基于自己开发的socket连接库，开发的一款本地传输的软件

服务端地址：https://github.com/earthWo/ZFLocalFileTransferService

### 使用方法：

#### 初始化：

```
 SocketConnect.init();
 SocketConnect.get().setSocketCallback(callback).connect();
```

```
 private SocketCallback callback=new SocketCallback(){
        
        @Override
        public void startService(String ip) {
        }

        @Override
        public void connected(Connecter connector) {
        }

        @Override
        public void connectError(SocketException se) {
        }

        @Override
        public void disconnected(Connecter connector) {
        }

        @Override
        public void receiveMessage(SocketMessage sm, Connecter connector) {
        }
       
    };
```

#### 发送相应的消息：

```
SocketConnect.get().sendTextMessage(socketId,"消息内容");                         SocketConnect.get().sendFileMessage(connector.getSocketId(),request.getFilePath());
```

#### 目前支持的消息类型：

1.文本消息 2.图片消息 3.音频消息 4.视频消息 5.文件消息

#### 暂时存在的问题

传输的速度比较慢，而且能够传输的文件较小，这些会在后续版本中解决。

### License

BSD, part MIT and Apache 2.0. See the [LICENSE](https://github.com/bumptech/glide/blob/master/LICENSE) file for details.

## 





