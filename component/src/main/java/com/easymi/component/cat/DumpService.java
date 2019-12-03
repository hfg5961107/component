package com.easymi.component.cat;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import com.easymi.component.utils.Log;
import android.widget.Toast;


import com.easymi.component.app.ActManager;

import java.io.File;
import java.util.Locale;

public class DumpService extends Service {

    private DumpObserver memObserver;
    private DumpObserver pagemapObserver;

    /**
     * 监听文件读写和打开事件.
     */
    private OnFileListener listener = new OnFileListener() {
        @Override
        public void onEvent(int event, @Nullable String path) {
            switch (event) {
                case FileObserver.ACCESS:
                case FileObserver.OPEN:
                    ActManager.getInstance().finishAllActivity();
                    System.exit(0);
                    break;
            }
        }
    };

    public DumpService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = Process.myPid();
        String memPath = String.format(Locale.CHINESE, "/proc/%d/mem", pid);
        String pagemapPath = String.format(Locale.CHINESE, "/proc/%d/pagemap", pid);

        File memFile = new File(memPath);
        File pagemapFile = new File(pagemapPath);

        if (memFile.exists()) {
            memObserver = new DumpObserver(memPath);
            memObserver.setOnFileListener(listener);
            memObserver.startWatching();
        }

        if (pagemapFile.exists()) {
            pagemapObserver = new DumpObserver(pagemapPath);
            pagemapObserver.setOnFileListener(listener);
            pagemapObserver.startWatching();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (memObserver != null) {
            memObserver.stopWatching();
        }
        if (pagemapObserver != null) {
            pagemapObserver.stopWatching();
        }
        super.onDestroy();
    }
}
