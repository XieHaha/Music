package com.yht.yihuantong.api.notify;

import android.support.annotation.NonNull;

import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.utils.LogUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dundun
 */
public class NotifyChangeListenerManager {

    private static NotifyChangeListenerServer notifyChangeListenerServer;

    public synchronized static NotifyChangeListenerServer getInstance() {
        if (notifyChangeListenerServer == null) {

            notifyChangeListenerServer = new NotifyChangeListenerServer();
        }
        return notifyChangeListenerServer;
    }

    public static class NotifyChangeListenerServer implements INotifyChangeListenerServer {
        private final String TAG = "notify-";
        /**
         * 患者添加状态
         */
        private List<IChange<String>> mPatientStatusChangeListeners = new CopyOnWriteArrayList<>();
        /**
         * 医生添加状态
         */
        private List<IChange<String>> mDoctorStatusChangeListeners = new CopyOnWriteArrayList<>();
        /**
         * 医生认证
         */
        private List<IChange<Integer>> mDoctorAuthStatusChangeListeners = new CopyOnWriteArrayList<>();
        /**
         * 转诊申请
         */
        private List<IChange<String>> mDoctorTransferPatientListeners = new CopyOnWriteArrayList<>();
        /**
         * 最近联系人
         */
        private List<IChange<String>> mRecentContactChangeListener = new CopyOnWriteArrayList<>();
        /**
         * 服务包订单
         */
        private List<IChange<String>> mOrderStatusChangeListener = new CopyOnWriteArrayList<>();

        @Override
        public void registerPatientStatusChangeListener(@NonNull IChange<String> listener,
                                                        @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mPatientStatusChangeListeners.add(listener);
            } else {
                mPatientStatusChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorStatusChangeListener(@NonNull IChange<String> listener,
                                                       @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mDoctorStatusChangeListeners.add(listener);
            } else {
                mDoctorStatusChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorTransferPatientListener(@NonNull IChange<String> listener,
                                                          @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mDoctorTransferPatientListeners.add(listener);
            } else {
                mDoctorTransferPatientListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorAuthStatusChangeListener(@NonNull IChange<Integer> listener,
                                                           @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mDoctorAuthStatusChangeListeners.add(listener);
            } else {
                mDoctorAuthStatusChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerRecentContactChangeListener(@NonNull IChange<String> listener,
                                                        @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mRecentContactChangeListener.add(listener);
            } else {
                mRecentContactChangeListener.remove(listener);
            }
        }

        @Override
        public void registerOrderStatusChangeListener(@NonNull IChange<String> listener,
                                                      @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mOrderStatusChangeListener.add(listener);
            } else {
                mOrderStatusChangeListener.remove(listener);
            }
        }

        /**
         * 患者添加
         *
         * @param data
         */
        public void notifyPatientStatusChange(final String data) {
            synchronized (mPatientStatusChangeListeners) {
                for (int i = 0, size = mPatientStatusChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mPatientStatusChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        LogUtils.w(TAG, "notifyStatusChange error", e);
                    }
                }
            }
        }

        /**
         * 医生添加
         *
         * @param data
         */
        public void notifyDoctorStatusChange(final String data) {
            synchronized (mDoctorStatusChangeListeners) {
                for (int i = 0, size = mDoctorStatusChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mDoctorStatusChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        LogUtils.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 转诊申请
         *
         * @param data
         */
        public void notifyDoctorTransferPatient(final String data) {
            synchronized (mDoctorTransferPatientListeners) {
                for (int i = 0, size = mDoctorTransferPatientListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mDoctorTransferPatientListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        LogUtils.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 医生认证状态
         *
         * @param data
         */
        public void notifyDoctorAuthStatus(final Integer data) {
            synchronized (mDoctorAuthStatusChangeListeners) {
                for (int i = 0, size = mDoctorAuthStatusChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<Integer> change = mDoctorAuthStatusChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        LogUtils.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 最近联系人
         *
         * @param data
         */
        public void notifyRecentContactChange(final String data) {
            synchronized (mRecentContactChangeListener) {
                for (int i = 0, size = mRecentContactChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mRecentContactChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        LogUtils.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 服务包订单
         *
         * @param data
         */
        public void notifyOrderStatusChange(final String data) {
            synchronized (mOrderStatusChangeListener) {
                for (int i = 0, size = mOrderStatusChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mOrderStatusChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    } catch (Exception e) {
                        LogUtils.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }
    }

}
