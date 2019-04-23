package com.zyc.doctor.api;

import com.zyc.doctor.utils.LogUtils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程池帮助类
 *
 * @author dundun
 */
public class ThreadPoolHelper {
    private static final String TAG = "ThreadPoolHelper";
    private static Resource resource;

    public synchronized static Resource getInstance() {
        if (resource == null) {
            resource = new Resource();
        }
        return resource;
    }

    public static class Resource {
        private static final int THREAD_NUM = 5;
        private ExecutorService executorSingle;
        private ExecutorService executorCached;
        private ExecutorService executorFixed;

        public Resource() {
            initThreadPool();
        }

        public static void clearup() {
            if (resource != null) {
                resource.closeThreadPool();
            }
            resource = null;
        }

        private void initThreadPool() {
            executorSingle = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern(
                    "yht-thread-pool-%d").daemon(true).build());
            executorCached = new ScheduledThreadPoolExecutor(0, new BasicThreadFactory.Builder().namingPattern(
                    "yht-thread-pool-%d").daemon(true).build());
            executorFixed = new ScheduledThreadPoolExecutor(THREAD_NUM, new BasicThreadFactory.Builder().namingPattern(
                    "yht-thread-pool-%d").daemon(true).build());
        }

        /**
         * 关闭线程池
         */
        public void closeThreadPool() {
            executorSingle.shutdown();
            executorCached.shutdown();
            executorFixed.shutdown();
        }

        public void execInSingle(Runnable r) {
            if (executorSingle == null || executorSingle.isShutdown() || executorSingle.isTerminated()) {
                executorSingle = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern(
                        "yht-thread-pool-%d").daemon(true).build());
            }
            try {
                executorSingle.execute(r);
            }
            catch (Exception e) {
                if (e instanceof RejectedExecutionException) {
                    execInSingle(r);
                }
                LogUtils.w(TAG, "Exception error!", e);
            }
        }

        public void execInCached(Runnable r) {
            if (executorCached == null || executorCached.isShutdown() || executorCached.isTerminated()) {
                executorCached = new ScheduledThreadPoolExecutor(0, new BasicThreadFactory.Builder().namingPattern(
                        "yht-thread-pool-%d").daemon(true).build());
            }
            try {
                executorCached.execute(r);
            }
            catch (Exception e) {
                if (e instanceof RejectedExecutionException) {
                    execInCached(r);
                }
                LogUtils.w(TAG, "Exception error!", e);
            }
        }

        public <V> Future<V> submitInCached(Callable<V> c) {
            if (executorCached == null || executorCached.isShutdown() || executorCached.isTerminated()) {
                executorCached = new ScheduledThreadPoolExecutor(0, new BasicThreadFactory.Builder().namingPattern(
                        "yht-thread-pool-%d").daemon(true).build());
            }
            try {
                return executorCached.submit(c);
            }
            catch (Exception e) {
                LogUtils.e(TAG, "ThreadPoolHelper submitInCached Exception", e);
            }
            return null;
        }

        public void execInFixed(Runnable r) {
            if (executorFixed == null || executorFixed.isShutdown() || executorFixed.isTerminated()) {
                executorFixed = new ScheduledThreadPoolExecutor(THREAD_NUM,
                                                                new BasicThreadFactory.Builder().namingPattern(
                                                                        "yht-thread-pool-%d").daemon(true).build());
            }
            try {
                executorFixed.execute(r);
            }
            catch (Exception e) {
                if (e instanceof RejectedExecutionException) {
                    execInFixed(r);
                }
                LogUtils.w(TAG, "Exception error!", e);
            }
        }
    }
}
