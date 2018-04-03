package custom.frame.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by luozi on 2015/12/25.
 */
public interface ActivityInterface {
    /**
     * 得到布局的id
     */
    int getLayoutID();

    /**
     * 得到布局的view
     */
    View getLayoutView();

    /**
     * setContentView调用前调用
     */
    void befordCreateView(@NonNull Bundle savedInstanceState);

    /**
     * 初始化界面view，调用顺序为1
     * 因为注解，弃用
     */
    void initView(@NonNull Bundle savedInstanceState);

    /**
     * 初始化类，调用顺序为2
     */
    void initObject(@NonNull Bundle savedInstanceState);

    /**
     * 初始化数据，调用顺序为3
     */
    void initData(@NonNull Bundle savedInstanceState);

    /**
     * 得到布局的id，调用顺序为4
     */
    void initListener();
}
