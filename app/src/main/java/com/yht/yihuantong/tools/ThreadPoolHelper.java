package com.yht.yihuantong.tools;

import com.yht.yihuantong.utils.LogUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * 线程池帮助类
 *
 * @author dundun
 */
public class ThreadPoolHelper {

    private static Resource resource;

    public synchronized static Resource getInstance() {
        if (resource == null) {
            resource = new Resource();
        }
        return resource;
    }

    public static class Resource {

        private static final String TAG = "ThreadPoolHelper";
        private static final int threadNum = 5;
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
            executorSingle = Executors.newSingleThreadExecutor();
            executorCached = Executors.newCachedThreadPool();
            executorFixed = Executors.newFixedThreadPool(threadNum);
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
                executorSingle = Executors.newSingleThreadExecutor();
            }
            try {
                executorSingle.execute(r);
            } catch (Exception e) {
                if (e instanceof RejectedExecutionException) {
                    execInSingle(r);
                }
            }
        }

        public void execInCached(Runnable r) {
            if (executorCached == null || executorCached.isShutdown() || executorCached.isTerminated()) {
                executorCached = Executors.newCachedThreadPool();
            }
            try {
                executorCached.execute(r);
            } catch (Exception e) {
                if (e instanceof RejectedExecutionException) {
                    execInCached(r);
                }
            }
        }

        public <V> Future<V> submitInCached(Callable<V> c) {
            if (executorCached == null || executorCached.isShutdown() || executorCached.isTerminated()) {
                executorCached = Executors.newCachedThreadPool();
            }
            try {
                return executorCached.submit(c);
            } catch (Exception e) {
                LogUtils.e(TAG, "ThreadPoolHelper submitInCached Exception", e);
            }
            return null;
        }

        public void execInFixed(Runnable r) {
            if (executorFixed == null || executorFixed.isShutdown() || executorFixed.isTerminated()) {
                executorFixed = Executors.newFixedThreadPool(threadNum);
            }
            try {
                executorFixed.execute(r);
            } catch (Exception e) {
                if (e instanceof RejectedExecutionException) {
                    execInFixed(r);
                }
            }
        }
    }
}
