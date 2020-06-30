package github.ztc.registryCenter.impl;

import github.ztc.registryCenter.ServiceRegistry;
import github.ztc.utils.ZkManager;

import java.net.InetSocketAddress;

public class ServiceRegistryImpl implements ServiceRegistry {

    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        //inetSocketAddress.toString()的格式是 /127.0.0.1:8080
        ZkManager.createPersistentNode(serviceName+inetSocketAddress.toString());
    }
}
