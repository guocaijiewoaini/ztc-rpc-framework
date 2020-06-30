package github.ztc.transport.socket;

import github.ztc.registryCenter.ServiceRegistry;
import github.ztc.registryCenter.impl.ServiceRegistryImpl;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

@Slf4j
public class SocketRpcServer {

    private final String host;
//    private final ExecutorService threadPool;
    private final int ip;
    private final ServiceRegistry serviceRegistry =new ServiceRegistryImpl();

    SocketRpcServer(String host,int ip){
        this.host=host;
        this.ip=ip;
//        this.threadPool =new ThreadPoolExecutor()
    }

    public <T> void publishService(T service,Class<T> serviceClass ){

        //注册服务
        serviceRegistry.registerService(serviceClass.getCanonicalName(),new InetSocketAddress(host,ip));
        //线程池 监听rpcRequest 有请求的时候就调用执行
    }
}
