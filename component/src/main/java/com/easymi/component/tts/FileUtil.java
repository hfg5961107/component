package com.easymi.component.tts;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import com.easymi.component.utils.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fujiayi on 2017/5/19.
 */

public class FileUtil {

    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    public static String createTmpDir(Context context) {
        String sampleDir = "baiduTTS";
        String tmpDir = Environment.getExternalStorageDirectory().toString() + "/" + sampleDir;
        FileUtil.makeDir(tmpDir);
        return tmpDir;
    }

    public static boolean fileCanRead(String filename) {
        File f = new File(filename);
        return f.canRead();
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    public static void copyFromAssets(AssetManager assets, String source, String dest)
            throws IOException {
        File file = new File(dest);
        if (!file.exists()) {
            Log.e("XApp", "copyFromAssets  !exists");
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
        }
        checkFiles(assets, source, dest, file);
    }

    private static void checkFiles(AssetManager assets, String source, String dest, File file) throws IOException {
        Log.e("XApp", "copyFromAssets  exists");
        if (TextUtils.equals(source, "bd_etts_text.dat")) {
            if (file.length() != 7561916) {
                Log.e("XApp", "copyFromAssets  del 1");
                file.delete();
                copyFromAssets(assets, source, dest);
            }
        } else if (TextUtils.equals(source, "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat")) {
            if (file.length() != 304972) {
                Log.e("XApp", "copyFromAssets  del 2");
                file.delete();
                copyFromAssets(assets, source, dest);
            }
        }
    }
}
