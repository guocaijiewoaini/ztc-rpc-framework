package github.ztc.loadBalance.impl;

import github.ztc.loadBalance.LoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {

    /**
     *@description 随机返回集合中的一个地址作为服务地址
     *@params  [serviceAdresses]
     *@return  java.lang.String
     *@date  2020/06/30 11:39
     */
    @Override
    public String selectServiceAdress(List<String> serviceAdresses) {
        if(serviceAdresses.size()==1) return serviceAdresses.get(0);
        Random random =new Random();
        return serviceAdresses.get(random.nextInt(serviceAdresses.size()));
    }
}
