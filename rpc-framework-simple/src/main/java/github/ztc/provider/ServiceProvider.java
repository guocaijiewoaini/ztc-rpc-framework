package github.ztc.provider;
/**
 * @title: ServiceProvider
 * @description: rpc提供服务接口
 * @author: ztc
 * @date: 2020/06/29 07:59 19:59
 * @version: V1.0
*/
public interface ServiceProvider {

    <T> void addServiceProvider();


    void getServiceProvider();
    
}
