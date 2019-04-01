package com.zyc.doctor.http.listener;

import com.zyc.doctor.http.Tasks;

/**
 * 网络任务队列变动监听
 * Created by luozi on 2016/1/15.
 */
@Deprecated
public interface TasksListener {
    /**
     * 添加任务
     *
     * @param task 任务单例编号
     */
    void addTask(Tasks task);

    /**
     * 取消任务
     *
     * @param task 任务单例编号
     */
    void cancelTask(Tasks task);

    /**
     * 结束任务
     *
     * @param task 任务单例编号
     */
    void successTask(Tasks task);

    /**
     * 任务错误
     *
     * @param task 任务单例编号
     */
    void errorTask(Tasks task);

}
