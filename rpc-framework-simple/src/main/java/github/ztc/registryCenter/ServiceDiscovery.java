package github.ztc.registryCenter;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {

    //根据服务名返回地址
    InetSocketAddress lookUpService(String ServiceName);
}
