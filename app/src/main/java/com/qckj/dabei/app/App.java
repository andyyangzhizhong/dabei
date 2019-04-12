package com.qckj.dabei.app;

import android.app.Application;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.util.Config;
import android.widget.Toast;

import com.qckj.dabei.manager.SettingManager;
import com.qckj.dabei.manager.cache.CacheManager;
import com.qckj.dabei.manager.home.HomeDataManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.mine.UserManager;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ManagerInject;
import com.qckj.dabei.view.dialog.LoadingProgressDialog;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用入口
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class App extends Application {

    public static App instance;
    private static Handler handler = new Handler();

    // 所有manager
    private HashMap<String, BaseManager> managers = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initManager();
        UMConfigure.init(this, "5be9420af1f556dfcb000194", "umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        PlatformConfig.setWeixin("wxf8cc09cfbaf41350", "c4eb1d1acb40f8fbfc55b64e00ec883c");
        PlatformConfig.setQQZone("101507083", "2da9daeb773bdb04590db94c67d53976");
    }

    /**
     * 初始化manager
     */
    private void initManager() {
        List<BaseManager> managerList = new ArrayList<>();
        registerManager(managerList);
        for (BaseManager baseManager : managerList) {
            injectManager(baseManager);
            baseManager.onManagerCreate(this);
            Class<? extends BaseManager> baseManagerClass = baseManager.getClass();
            String name = baseManagerClass.getName();
            managers.put(name, baseManager);
        }

        for (Map.Entry<String, BaseManager> entry : managers.entrySet()) {
            entry.getValue().onAllManagerCreated();
        }
    }

    public static App getInstance() {
        return instance;
    }

    public static Handler getHandler() {
        return handler;
    }

    public <V extends BaseManager> V getManager(Class<V> cls) {
        return (V) managers.get(cls.getName());
    }

    /**
     * 注解manager
     *
     * @param object 需要注解的object
     */
    public void injectManager(Object object) {
        Class<?> aClass = object.getClass();

        while (aClass != BaseManager.class && aClass != Object.class) {
            Field[] declaredFields = aClass.getDeclaredFields();
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                        // 忽略掉static 和 final 修饰的变量
                        continue;
                    }

                    if (!field.isAnnotationPresent(Manager.class)) {
                        continue;
                    }

                    Class<?> type = field.getType();
                    if (!BaseManager.class.isAssignableFrom(type)) {
                        throw new RuntimeException("@Manager 注解只能应用到BaseManager的子类");
                    }

                    BaseManager baseManager = getManager((Class<? extends BaseManager>) type);

                    if (baseManager == null) {
                        throw new RuntimeException(type.getSimpleName() + " 管理类还未初始化！");
                    }

                    if (!field.isAccessible())
                        field.setAccessible(true);

                    try {
                        field.set(object, baseManager);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            aClass = aClass.getSuperclass();
        }
    }

    private void registerManager(List<BaseManager> managerList) {
        managerList.add(new CacheManager());
        managerList.add(new SettingManager());
        managerList.add(new GaoDeLocationManager());
        managerList.add(new HomeDataManager());
        managerList.add(new UserManager());
    }

}
