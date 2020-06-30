package github.ztc.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class ThreadPoolConfig {

    /**
     * 线程池的默认参数
     */

    private static final int CORE_POOL_SIZE =10;
    private static final int MAX_POOL_SIZE =100;
    private static final int KEEP_ALIVE_TIME =1;
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;
    private static final int BLOCK_QUEUE_CAPACITY=100;

    private int corePoolSize =CORE_POOL_SIZE;
    private int maxPoolSize =MAX_POOL_SIZE;
    private int keepAliveTime =KEEP_ALIVE_TIME;
    private TimeUnit timeUnit =TIME_UNIT;
    private int blockQueueCapacity=BLOCK_QUEUE_CAPACITY;
    private BlockingQueue<Runnable> queue =new ArrayBlockingQueue<>(BLOCK_QUEUE_CAPACITY);


}
