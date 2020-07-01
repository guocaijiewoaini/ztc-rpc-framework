package github.ztc.provider;
/**
 * @title: ServiceProvider
 * @description: rpc提供服务接口
 * @author: ztc
 * @date: 2020/06/29 07:59 19:59
 * @version: V1.0
*/
public interface ServiceProvider {
    /**
     *@description 在向注册中心发布服务的时候，会将服务执行对象通过这个方法添加到容器中
     *@params  [service, serviceClass]
     *@date  2020/07/01 02:31
     */
    <T> void addServiceProvider(T service,Class<T> serviceClass);

    /**
     *@description 获取执行的对象
     *@params  [serviceName]
     *@date  2020/07/01 02:32
     */
    Object getServiceProvider(String serviceName);
    
}
