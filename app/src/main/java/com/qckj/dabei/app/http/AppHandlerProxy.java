package com.qckj.dabei.app.http;

import android.os.Looper;
import android.os.Message;

import com.qckj.dabei.util.OMMap;

import java.util.List;

public class AppHandlerProxy {
    private static OMMap<Integer, AppHandler.HandlerMessageListener> m_handlerMessageListenerMap = new OMMap<Integer, AppHandler.HandlerMessageListener>();

    private static AppHandler.HandlerMessageListener handlerMessageListener = new AppHandler.HandlerMessageListener() {
        @Override
        public void handleMessage(Message msg) {
            List<AppHandler.HandlerMessageListener> handlerMessageListeners = m_handlerMessageListenerMap.get(msg.what);
            if (handlerMessageListeners != null) {
                for (AppHandler.HandlerMessageListener handlerMessageListener : handlerMessageListeners) {
                    handlerMessageListener.handleMessage(msg);
                }
            }
        }
    };
    private static AppHandler m_ipHandler = new AppHandler(handlerMessageListener);

    /** 该类不允许拥有实例 */
    private AppHandlerProxy() {

    }

    /**
     * 在UI线程中执行
     *
     * @param runnable 需要在UI中执行的Runable
     * @see #post(Runnable)
     */
    public static void runOnUIThread(Runnable runnable) {
        if (isOnUIThread()) {
            // 在主线程中调用，直接执行
            runnable.run();
        } else {
            post(runnable);
        }
    }

    /**
     * 判断当前线程是不是UI线程
     *
     * @return
     */
    public static boolean isOnUIThread() {
        boolean result;
        // 在主线程中调用
        result = Thread.currentThread() == Looper.getMainLooper().getThread();
        return result;
    }

    /**
     * 发送消息
     *
     * @param what
     */
    public static void sendEmptyMessage(int what) {
        m_ipHandler.sendEmptyMessage(what);
    }

    /**
     * obtainMessage
     *
     */
    public static Message obtainMessage() {
        return m_ipHandler.obtainMessage();
    }

    /**
     * sendEmptyMessageDelayed
     *
     * @param what
     */
    public static void sendEmptyMessageDelayed(int what, int delayMillis) {
        m_ipHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    /**
     * sendMessageDelayed
     *
     */
    public static void sendMessageDelayed(Message message, int delayMillis) {
        m_ipHandler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * 发送一个全局消息，所有注册了该消息ID的接收器都能收到该消息
     *
     */
    public static void sendMessage(Message message) {
        m_ipHandler.sendMessage(message);
    }

    /**
     * post一个代码段到UI执行
     *
     * @param runnable
     */
    public static void post(Runnable runnable) {
        m_ipHandler.post(runnable);
    }

    public static void postDelayed(Runnable runnable, int delayMillis) {
        m_ipHandler.postDelayed(runnable, delayMillis);
    }

    public static void registHandlerMessageListener(int what, AppHandler.HandlerMessageListener handlerMessageListener) {
        m_handlerMessageListenerMap.put(what, handlerMessageListener);
    }

    public static void unregistHandlerMessageListener(int what, AppHandler.HandlerMessageListener handlerMessageListener) {
        m_handlerMessageListenerMap.remove(what, handlerMessageListener);
    }
}
