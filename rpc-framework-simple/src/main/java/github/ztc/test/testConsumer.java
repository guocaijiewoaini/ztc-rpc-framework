package github.ztc.test;

import github.ztc.proxy.RpcClientProxy;
import github.ztc.transport.socket.SocketRpcClient;

import java.lang.reflect.Proxy;

public class testConsumer {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy(new SocketRpcClient());
        HelloService helloService = proxy.getProxyInstance(HelloService.class);
        helloService.diaoYong();
    }
}
