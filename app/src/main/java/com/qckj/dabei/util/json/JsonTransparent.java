package com.qckj.dabei.util.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 结构中不需要JSON序列化的字段
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JsonTransparent {
}
