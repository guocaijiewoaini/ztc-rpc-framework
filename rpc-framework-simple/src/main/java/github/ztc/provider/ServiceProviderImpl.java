package github.ztc.provider;

import github.ztc.exception.RpcException;
import github.ztc.rpcEnum.RpcErrorMessageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: ServiceProviderImpl
 * @description: 提供服务的容器，保存各个服务类的对象
 * @author: ztc
 * @date: 2020/07/01 02:36 14:36
 * @version: V1.0
*/
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private static Map<String,Object> serviceMap =new ConcurrentHashMap<>();

    private static Set<String>  registeredService =ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName =serviceClass.getCanonicalName();
        if(registeredService.contains(serviceName)) return;
        serviceMap.put(serviceName,service);
        log.info("Add service: {} and interfaces:{}", serviceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getServiceProvider(String serviceName) {
         Object service = serviceMap.get(serviceName);
         if(service==null){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
         }
         return service;
    }
}
