package github.ztc.test;

import github.ztc.proxy.RpcClientProxy;
import github.ztc.transport.socket.SocketRpcClient;

import java.lang.reflect.Proxy;

public class testConsumer {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy(new SocketRpcClient());
        HelloServiceInterface helloService = proxy.getProxyInstance(HelloServiceInterface.class);
        String s = (String)helloService.diaoYong();
        System.out.println(s);
    }
}
