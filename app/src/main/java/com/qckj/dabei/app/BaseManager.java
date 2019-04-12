package com.qckj.dabei.app;

import android.os.Handler;

import com.qckj.dabei.util.log.Logger;

/**
 * 管理器基类，所有管理器都必须继承自本类
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public abstract class BaseManager {

    // 全局handler
    protected static final Handler gHandler = new Handler();
    /** 日志打印， logger.d("a = %d", 3); */
    protected Logger logger = new Logger(getClass().getSimpleName());

    /** 管理器被初始化的回调，初始化整个管理器 */
    public abstract void onManagerCreate(App app);

    /** 所有管理器都初始化后执行 */
    public void onAllManagerCreated() {
    }

    /** 获得应用程序实例 */
    public App getApplication() {
        return App.getInstance();
    }

    /**
     * 获得管理器
     *
     * @param manager 管理器类型
     * @param <M>     管理器Class
     * @return 管理器
     */
    public <M extends BaseManager> M getManager(Class<M> manager) {
        return App.getInstance().getManager(manager);
    }

   /* public int getUid() {
        return getManager(PesUserManager.class).getUid();
    }*/

    /**
     * 应用进入了后台
     */
    public void onEnterBackground() {
        // 进入后台
    }

    /**
     * 应用重新进入了前台，在onEnterBackground回调之后，应用重新进入前台使用
     */
    public void onReenterForeground() {
        // 重新进入前台
    }

}
