package github.ztc.loadBalance;

import java.util.List;

public interface LoadBalance {

    //负责均衡策略选择服务地址，对于某一次服务的选择 ，就是在一堆地址中找到一个作为服务的地址
    String selectServiceAdress(List<String> serviceAdresses);
}
