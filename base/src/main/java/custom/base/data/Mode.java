package custom.base.data;

/**
 * Created by luozi on 2015/12/29.
 * app的运行模式
 */
public interface Mode {
    /**
     * 开发模式，此模式影响后台的输出，及前台显示
     */
    enum Launch {
        /**
         * 生产模式
         */
        RELEASE,
        /**
         * 调试模式
         */
        DEBUG
    }

    enum Control {
        /**
         * 可调整模式
         */
        ADJUST_ABLE,
        /**
         * 不可调整模式、最终模式
         */
        RELEASE
    }
}
