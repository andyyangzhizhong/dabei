package com.qckj.dabei.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qckj.dabei.R;

/**
 * Glide使用 工具类
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class GlideUtil {

    public static void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    public static void displayImage(Context context, String path, ImageView imageView, int error) {
        RequestOptions options = new RequestOptions().error(error);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

    public static void displayImage(Context context, String path, ImageView imageView, int placeId, int error) {
        RequestOptions options = new RequestOptions().error(error).placeholder(placeId);
        Glide.with(context).load(path).apply(options).into(imageView);
    }

}


