package com.qckj.dabei.app.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.qckj.dabei.util.AppData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 对Handle封装
 *
 * @author zengdexing
 */
public class AppHandler extends Handler {
    private HandlerMessageListener mOnHandlerMessageListene;
    private Set<Integer> msgWhatSet = new HashSet<>();

    public AppHandler(HandlerMessageListener onHandlerMessageListener) {
        super(Looper.getMainLooper());
        this.mOnHandlerMessageListene = onHandlerMessageListener;
    }

    /**
     * 注册全局消息,注意:全局消息ID不能重复,否则会出现问题,</br>
     * 调用该方法后记得调用{@link #unregistMessage(int)} 或者一次性调用 {@link #unregistMessages()} 注销全局消息
     *
     * @param msgWhat
     */
    @SuppressWarnings("deprecation")
    public void registMessage(int msgWhat) {
        if (msgWhatSet.add(msgWhat)) {
            AppData.addHandler(msgWhat, this);
        }
    }

    /**
     * 注销全局消息
     *
     * @param msgWhat
     */
    @SuppressWarnings("deprecation")
    public void unregistMessage(int msgWhat) {
        if (msgWhatSet.remove(msgWhat)) {
            AppData.delHandler(msgWhat);
        }
    }

    /**
     * 注销所有注册过的全局消息
     */
    public void unregistMessages() {
        for (Iterator<Integer> iterator = msgWhatSet.iterator(); iterator.hasNext(); ) {
            Integer id = iterator.next();
            AppData.delHandler(id);
        }
        msgWhatSet.clear();
    }

    /**
     * 注册HandlerMessageListener
     *
     * @see HandlerMessageListener
     */
    public void setHandlerMessageListener(HandlerMessageListener onHandlerMessageListener) {
        mOnHandlerMessageListene = onHandlerMessageListener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (mOnHandlerMessageListene != null) {
            mOnHandlerMessageListene.handleMessage(msg);
        }
    }

    public interface HandlerMessageListener {
        void handleMessage(Message msg);
    }
}
