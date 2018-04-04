package com.wuai.company.task.concurrent;

import com.wuai.company.enums.SceneSelEnum;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 总线程
 * Created by Ness on 2017/6/6.
 */
@Component
public class Executors implements DisposableBean {

    public static final EnumSet<SceneSelEnum> allCategory = EnumSet.allOf(SceneSelEnum.class);

    private static AtomicInteger count = new AtomicInteger();

    private EnumMap<SceneSelEnum, ExecutorService> executors;


    public ExecutorService getInstance(SceneSelEnum sceneSelEnum) {

        if (executors == null) {
            executors = new EnumMap<>(SceneSelEnum.class);
            for (Iterator<SceneSelEnum> iterator = allCategory.iterator(); iterator.hasNext(); ) {
                executors.put(iterator.next(), java.util.concurrent.Executors.newSingleThreadExecutor(r -> {
                    Thread thread = new Thread(r);
                    thread.setName("wuai-scene-task-factory" + count.getAndIncrement());
                    if (thread.isDaemon()) {
                        thread.setDaemon(Boolean.FALSE);
                    }
                    thread.setPriority(Thread.NORM_PRIORITY);

                    return thread;
                }));
            }

        }

        return executors.get(sceneSelEnum);


    }

    public static ExecutorService getCache() {


        return java.util.concurrent.Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() >> 1, r -> {
            Thread thread = new Thread(r);
            thread.setName("wuai-cache-task-factory" + count.getAndIncrement());
            if (thread.isDaemon()) {
                thread.setDaemon(Boolean.FALSE);
            }
            thread.setPriority(Thread.NORM_PRIORITY);

            return thread;
        });

    }


    @Override
    public void destroy() throws Exception {
        if (executors != null && !executors.isEmpty()) {
            ExecutorService executor;
            for (Iterator<ExecutorService> iterator = executors.values().iterator(); iterator.hasNext(); ) {
                executor = iterator.next();
                if (executor != null && !executor.isShutdown()) {
                    executor.shutdown();
                }
            }
        }
    }
}
