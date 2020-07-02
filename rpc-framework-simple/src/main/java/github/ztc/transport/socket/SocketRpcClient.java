package github.ztc.transport.socket;

import github.ztc.dto.RpcRequest;
import github.ztc.registryCenter.ServiceDiscovery;
import github.ztc.registryCenter.impl.ServiceDiscoveryImpl;
import github.ztc.transport.ClientTransport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketRpcClient implements ClientTransport {
    private ServiceDiscovery serviceDiscovery;
    //传入服务发现接口类对象
    public SocketRpcClient(){
        this.serviceDiscovery=new ServiceDiscoveryImpl();
    }
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        //去注册中心找rpcRequest中的接口名字对应的服务
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookUpService(rpcRequest.getInterfaceName());
        //注册中心根据服务找到对应的socketAdress  ( ip地址和端口)返回
//        InetSocketAddress inetSocketAddress =new InetSocketAddress("",1000);
        try(Socket socket =new Socket()){
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);//发送rpc请求
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object rpcResponse = objectInputStream.readObject();
            return rpcResponse;//返回响应
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
