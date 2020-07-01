package github.ztc.transport.socket;

import github.ztc.dto.RpcRequest;
import github.ztc.dto.RpcResponse;
import github.ztc.provider.ServiceProvider;
import github.ztc.provider.ServiceProviderImpl;
import github.ztc.provider.SocketRpcHandler;
import github.ztc.utils.SingletonManger;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * @title: SocketRpcHandler
 * @description: 处理rpc请求的socket，执行方法调用，结果封装返回。
 * @author: ztc
 * @date: 2020/07/01 02:13 14:13
 * @version: V1.0
*/
@Slf4j
public class SocketRpcHandlerRunnable implements Runnable {

    private Socket socket;
    private SocketRpcHandler socketRpcHandler;

    SocketRpcHandlerRunnable(Socket socket){
        this.socket=socket;
        this.socketRpcHandler = SingletonManger.getSingletonInstance(SocketRpcHandler.class);
    }
    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = socketRpcHandler.handle(rpcRequest);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(RpcResponse.success(result,rpcRequest.getRequestId()));
            objectOutputStream.flush();

            objectInputStream.close();
            objectOutputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception",e);
            e.printStackTrace();
        }
    }
}
