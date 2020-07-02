package github.ztc.transport.socket;

import github.ztc.provider.ServiceProvider;
import github.ztc.provider.ServiceProviderImpl;
import github.ztc.registryCenter.ServiceRegistry;
import github.ztc.registryCenter.impl.ServiceRegistryImpl;
import github.ztc.utils.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Slf4j
public class SocketRpcServer {

    private final String host;
    private final ExecutorService threadPool;
    private final int port;
    private final ServiceRegistry serviceRegistry =new ServiceRegistryImpl();
    private final ServiceProvider serviceProvider =new ServiceProviderImpl();
    public SocketRpcServer(String host,int port){
        this.host=host;
        this.port = port;
        this.threadPool = ThreadPoolManager.createThreadPoolIfAbsent(new InetSocketAddress(host, port).toString());
    }
//    @SuppressWarnings("uncheck")
    public <T> void publishService(T service,Class<T> serviceClass ){

        //注册服务
        serviceRegistry.registerService(serviceClass.getCanonicalName(),new InetSocketAddress(host, port));
        //将对象添加到容器中
        serviceProvider.addServiceProvider(service,serviceClass);
        //线程池 监听rpcRequest 有请求的时候就调用执行
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            ThreadPoolManager.shutDownAllThreadPool();
        }));//注册钩子
        start();
    }
    private void start(){
        try(ServerSocket server =new ServerSocket()) {
            server.bind(new InetSocketAddress(host, port));
            Socket socket;
            while ((socket =server.accept())!=null){

                //socket传入执行
                threadPool.execute(new SocketRpcHandlerRunnable(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
