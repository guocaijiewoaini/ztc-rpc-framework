package github.ztc.test;

import github.ztc.transport.socket.SocketRpcServer;

public class testProvider {
    public static void main(String[] args) {
        SocketRpcServer server =new SocketRpcServer("127.0.0.1",8889);
        HelloServiceInterface helloService =new HelloService();
        server.publishService(helloService,HelloServiceInterface.class);
        //上一行代码开启socket监听 阻塞
    }
}
