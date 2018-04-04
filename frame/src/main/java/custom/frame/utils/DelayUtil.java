package custom.frame.utils;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class DelayUtil {
    private Timer timer;
    private TimerTask task;
    private boolean isDelaing = false;// 是否在延时中
    private boolean isCancle = false;//是否取消

    public OnDelayListener listener = null;

    public DelayUtil() {
        timer = new Timer();
    }

    public void delay(long delay) {
        handler.postDelayed(runnable, delay);
        isDelaing = true;
        isCancle = false;
    }

    public void delay(long delay, OnDelayListener listener) {
        this.listener = listener;
        isDelaing = true;
        isCancle = false;
        handler.postDelayed(runnable, delay);

    }

    public void delayByThread(long delay, OnDelayListener listener) {
        this.listener = listener;
        isDelaing = true;
        isCancle = false;
        startThread(delay);
    }

    private void startThread(long delay) {
        DelayThread thread = new DelayThread();
        thread.setDelay(delay);
        thread.start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (listener != null && !isCancle) {
                listener.onDelayFinish();
            }
        }

        ;
    };

    class DelayThread extends Thread {
        long delay = 0;

        @Override
        public void run() {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (listener != null) {
                listener.onDealing();
            }
            Message message = handler.obtainMessage();
            handler.sendMessage(message);
            super.run();
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (listener != null) {
                listener.onDealing();
            }
            handler.sendEmptyMessage(0);
        }
    };

    public void cancle() {
        isCancle = true;
    }

    public boolean isDelaing() {
        return isDelaing;
    }

    public void setListener(OnDelayListener listener) {
        this.listener = listener;
    }

    public interface OnDelayListener {
        void onDelayFinish();

        void onDealing();
    }
}
