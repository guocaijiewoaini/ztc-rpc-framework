package github.ztc.registryCenter.impl;

import github.ztc.loadBalance.LoadBalance;
import github.ztc.loadBalance.impl.RandomLoadBalance;
import github.ztc.registryCenter.ServiceDiscovery;
import github.ztc.utils.ZkManager;

import java.net.InetSocketAddress;
import java.util.List;

public class ServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance =new RandomLoadBalance();

    /**
     *@description 查找服务返回服务的ip
     *@params  [ServiceName]
     *@date  2020/06/30 03:54
     */
    @Override
    public InetSocketAddress lookUpService(String ServiceName) {
        List<String> serviceAddresses = ZkManager.getChildrenNodes(ServiceName);
        String address = loadBalance.selectServiceAdress(serviceAddresses);

        //adress形如 127.0.0.1:8080  对它进行分割 构建InetSocketAddress对象
        String[] addressArray =address.split(":");
        return new InetSocketAddress(addressArray[0],Integer.parseInt(addressArray[1]));
    }
}
