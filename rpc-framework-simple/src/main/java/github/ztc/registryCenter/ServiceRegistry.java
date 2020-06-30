package github.ztc.registryCenter;

import java.net.InetSocketAddress;

public interface ServiceRegistry {

    /**
     *@description 注册服务 指定服务类和注册服务的地址
     *@params  [serviceName, inetSocketAddress]
     *@date  2020/06/30 05:44
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
