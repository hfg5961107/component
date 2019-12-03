package com.easymi.component.cat;

import android.support.annotation.Nullable;

/**
 * Created by yinxin on 2018/3/26.
 */

public interface OnFileListener {

    /**
     * 文件状态改变监听回调{@link android.os.FileObserver#onEvent(int, String)}.
     */
    void onEvent(int event, @Nullable String path);
}
