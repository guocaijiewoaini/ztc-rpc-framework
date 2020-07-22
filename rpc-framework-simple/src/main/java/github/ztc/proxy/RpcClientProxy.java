package github.ztc.proxy;

import github.ztc.dto.RpcRequest;
import github.ztc.dto.RpcResponse;
import github.ztc.transport.ClientTransport;
import github.ztc.transport.netty.client.NettyRpcClient;
import github.ztc.transport.socket.SocketRpcClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private ClientTransport clientTransport;

    public RpcClientProxy(ClientTransport clientTransport){
        this.clientTransport=clientTransport;
    }

    /**
     *@description 通过某个类的代理对象
     *@params  [clazz]
     *@return  T
     *@date  2020/06/30 10:19
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyInstance(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoke method:[{}]",method.getName());
        RpcRequest req = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString()).build();
        RpcResponse rpcResponse =null;
        //传输方式 socket 或者netty
        if(clientTransport instanceof SocketRpcClient){
//            CompletableFuture<RpcResponse> completableFuture
//                    =(CompletableFuture<RpcResponse>)clientTransport.sendRpcRequest(req);
//            rpcResponse =(RpcResponse) completableFuture.get();
            rpcResponse = (RpcResponse) clientTransport.sendRpcRequest(req);
        }
        if(clientTransport instanceof NettyRpcClient){

        }

        return rpcResponse.getData();
    }
}
