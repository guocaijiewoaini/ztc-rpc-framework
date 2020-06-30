package github.ztc.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: ZkManager
 * @description: 对zookeeper的节点进行管理，添加或者清空服务节点
 * @author: ztc
 * @date: 2020/06/30 03:52 15:52
 * @version: V1.0
*/
@Slf4j
public class ZkManager {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 5;
    private static final String CONNECT_STRING ="127.0.0.1:2181";
    private static final String REGISTRY_ROOT_PATH="/ztc-rpc";
    private static Map<String,List<String>> serviceAddressMap =new ConcurrentHashMap<>();
    private static Set<String> registeredPathSet =ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient ;

    static {
        //获取zookeeper连接
        zkClient =getZkClient();
    }


    /**
     *@description 创建持久化节点
     *@params  [path]
     *@date  2020/06/30 02:13
     */
    public static void createPersistentNode(String serviceAndAddress){
        String path =REGISTRY_ROOT_PATH+"/"+serviceAndAddress;
        try {
            if(registeredPathSet.contains(path)||zkClient.checkExists().forPath(path)!=null){
                log.info("节点已存在:[{}]",path);
            }
            else{
//                zkClient.create().forPath(path);//默认生成的节点数据为空
                //默认生成的节点数据为当前访问的ip地址
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("节点创建成功:[{}]",path);
            }
            registeredPathSet.add(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *@description 获取所有提供服务的生产者的地址
     *              函数被调用时，会把地址缓存到MAP中，并且注册事件
     *@params  [ServiceName]
     *@date  2020/06/30 02:16
     */
    public static List<String> getChildrenNodes(String serviceName){
        if(serviceAddressMap.containsKey(serviceName)){
            return serviceAddressMap.get(serviceName);
        }
        String path =REGISTRY_ROOT_PATH+"/"+serviceName;
        List<String> childrenNodes=null;
        try {
             childrenNodes= zkClient.getChildren().forPath(path);
             serviceAddressMap.put(serviceName,childrenNodes);
             registerWatcher(serviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childrenNodes;
    }

    /**
     *@description 清空注册中心的服务数据
     *@params  []
     *@date  2020/06/30 03:21
     */
    public static void clearRegistry(){
        registeredPathSet.stream().parallel().forEach((p)-> {
            try {
                zkClient.delete().forPath(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.info("服务端所有注册的服务被清空:[{}]",registeredPathSet.toString());
    }

    private static void registerWatcher(String serviceName){
        String path =REGISTRY_ROOT_PATH+"/"+serviceName;
        PathChildrenCache cache =new PathChildrenCache(zkClient,path,true);
        PathChildrenCacheListener listener =new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                List<String> serviceAddresses = curatorFramework.getChildren().forPath(path);
                //这里是当服务节点数量发生变化时，更新map
                serviceAddressMap.put(serviceName,serviceAddresses);
                System.out.println("节点发生了变化");
            }
        };
        cache.getListenable().addListener(listener);
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static CuratorFramework getZkClient(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES))
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

    public static void main(String[] args) throws Exception {
        InetSocketAddress inetSocketAddress =new InetSocketAddress("127.0.0.1",8888);
        ZkManager.createPersistentNode("/ztc-rpc/helloService"+inetSocketAddress.toString());
        ZkManager.getChildrenNodes("helloService");
        Thread.sleep(2000);
        InetSocketAddress inetSocketAddress1 =new InetSocketAddress("127.1.1.1",8888);
        ZkManager.createPersistentNode("/ztc-rpc/helloService"+inetSocketAddress1.toString());
        Thread.sleep(5000);
        System.out.println(serviceAddressMap.get("helloService").size());
    }


}
