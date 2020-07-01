package github.ztc.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import github.ztc.config.ThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @title: ThreadPoolManager
 * @description: 负责provider端的服务调用执行的线程池的管理，不同的服务对应一个不同的线程池
 * @author: ztc
 * @date: 2020/06/30 07:33 19:33
 * @version: V1.0
*/
@Slf4j
public class ThreadPoolManager {

    private static Map<String, ExecutorService> threadPools =new ConcurrentHashMap<>();

    public static ExecutorService createThreadPoolIfAbsent(String threadNamePrefix){
        ThreadPoolConfig config =new ThreadPoolConfig();
        return createThreadPoolIfAbsent(config,threadNamePrefix,false);//默认false,不是daemon线程
    }
    private static ExecutorService createThreadPoolIfAbsent(ThreadPoolConfig config,String threadNamePrefix,Boolean daemon){
        ExecutorService threadPool =threadPools.computeIfAbsent(threadNamePrefix,k->createThreadPool(config,threadNamePrefix,daemon));
        //拿到线程池后，检查它的状态
        if(threadPool.isShutdown()||threadPool.isTerminated()){
            threadPools.remove(threadNamePrefix);
            threadPool =createThreadPool(config, threadNamePrefix, daemon);
            threadPools.put(threadNamePrefix,threadPool);
        }
        return threadPool;

    }

    private static ExecutorService createThreadPool(ThreadPoolConfig config,String threadNamePrefix,Boolean daemon){
        ThreadFactory threadFactory =createThreadFactory(threadNamePrefix,daemon);//创建threadFactory 设置线程名字和是否后台线程
        return new ThreadPoolExecutor(
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getKeepAliveTime(),
                config.getTimeUnit(),
                config.getQueue(),
                threadFactory
        );
    }


    private static ThreadFactory createThreadFactory(String threadNamePrefix,Boolean daemon){
        if(threadNamePrefix!=null){
            if(daemon!=null){
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix+"-%d")
                        .setDaemon(daemon).build();
            }
            else{
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix+"-%d")
                        .build();
            }
        }
        return Executors.defaultThreadFactory();
    }

    public static void shutDownAllThreadPool(){
        log.info("call shutDownAllThreadPool method");
        threadPools.entrySet().parallelStream().forEach(entry->
        {
            ExecutorService threadPool =entry.getValue();
            threadPool.shutdown();
            log.info("shutdown threadPool [{}] [{}]",entry.getKey(),threadPool.isTerminated());
            try {
                threadPool.awaitTermination(10,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("Thread pool never terminated");
                threadPool.shutdownNow();
            }
        });
    }
}
