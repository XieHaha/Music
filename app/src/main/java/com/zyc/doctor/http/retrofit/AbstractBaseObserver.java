package com.zyc.doctor.http.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.http.listener.ResponseListener;
import com.zyc.doctor.ui.dialog.LoadingDialog;

import io.reactivex.disposables.Disposable;

/**
 * Observer加入加载框
 *
 * @param <T>
 * @author dundun
 */
public class AbstractBaseObserver<T> extends AbstractDataObserver<T> {
    private boolean mShowDialog;
    private LoadingDialog loadingDialog;
    private Context mContext;
    private Disposable d;

    public AbstractBaseObserver(Context context, Boolean showDialog, Tasks task, ResponseListener listener) {
        mContext = context;
        mShowDialog = showDialog;
        setParams(task, listener);
    }

    public AbstractBaseObserver(Context context, Tasks task, ResponseListener listener) {
        this(context, false, task, listener);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "未连接网络", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        }
        else {
            if (loadingDialog == null && mShowDialog == true) {
                loadingDialog = new LoadingDialog(mContext);
                loadingDialog.show();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onError(e);
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onComplete();
    }

    public void hidDialog() {
        if (loadingDialog != null && mShowDialog == true) { loadingDialog.dismiss(); }
        loadingDialog = null;
    }

    /**
     * 是否有网络连接，不管是wifi还是数据流量
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        boolean available = info.isAvailable();
        return available;
    }
}

