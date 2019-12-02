package com.yonyou.einvoice.demo.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class ThreadPoolTaskConfig {

  @Bean
  public Executor executor(){
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //此方法返回可用处理器的虚拟机的最大数量; 不小于1
    int core = Runtime.getRuntime().availableProcessors();
    executor.setCorePoolSize(core);
    executor.setMaxPoolSize(core*2 + 1);
    //除核心线程外的线程存活时间
    executor.setKeepAliveSeconds(10*60);
    // 优雅停机
    executor.setWaitForTasksToCompleteOnShutdown(true);
    //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
    executor.setQueueCapacity(40);
    //线程名称前缀
    executor.setThreadNamePrefix("invoice-thread-executor");
    //设置拒绝策略
    // ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
    //ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
    //ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
    //ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    return executor;
  }
}
