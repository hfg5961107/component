package com.easymi.component.cat;

import android.os.FileObserver;
import android.support.annotation.Nullable;

/**
 * Created by yinxin on 2018/3/26.
 */

final public class DumpObserver extends FileObserver {

    private OnFileListener mFileListener;

    DumpObserver(String path) {
        super(path, FileObserver.OPEN | FileObserver.ACCESS);
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        if (mFileListener != null) {
            mFileListener.onEvent(event, path);
        }
    }

    /**
     * 设置文件监听事件.
     *
     * @param fileListener fileListener
     */
    public void setOnFileListener(OnFileListener fileListener) {
        this.mFileListener = fileListener;
    }

}
