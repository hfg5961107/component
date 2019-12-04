package com.easymi.component.app;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.StringRes;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.easymi.component.BuildConfig;
import com.easymi.component.Config;
import com.easymi.component.R;
import com.easymi.component.activity.SettingActivity;
import com.easymi.component.db.SqliteHelper;
import com.easymi.component.loc.LocService;
import com.easymi.component.tts.InitConfig;
import com.easymi.component.tts.NonBlockSyntherizer;
import com.easymi.component.tts.OfflineResource;
import com.easymi.component.utils.CsEditor;
import com.easymi.component.utils.CsSharedPreferences;
import com.easymi.component.utils.Log;
import com.easymi.component.utils.PhoneUtil;
import com.easymi.component.utils.StringUtils;
import com.easymi.component.utils.SysUtil;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tech.linjiang.pandora.Pandora;
import tech.linjiang.pandora.function.IFunc;

/**
 * @author xyin
 * @date 2016/9/30
 * application 注:每启动一个新的进程就会调用application的onCreate方法(需要注意某些方法是否允许多次初始化).
 */

public class XApp extends MultiDexApplication {

    /**
     * 提示音标志位--start
     */
    public static int ON_LINE = 1;
    public static int OFF_LINE = 2;
    public static int GRAB = 3;
    public static int CANCEL = 4;
    public static int NEW_MSG = 5;
    public static int NEW_ANN = 6;
    /**
     * 提示音标志位--end
     */


    /**
     * 实例化对象
     */
    private static XApp instance;

    public NonBlockSyntherizer mSpeechSynthesizer;

    AudioManager audioManager;

    public MediaPlayer player;

    private AudioManager.OnAudioFocusChangeListener mFocusChangeListener;

    /**
     * 是否拥有焦点 通过此变量来判断player是否在正常播放
     */
    private boolean haveFoucs = false;
    private boolean isSpeeching;
    private String lastReadContent;
    private long lastReadTime;
    private boolean isInti;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isAppProcess()) {
            return;
        }
        //初始化路由
        if (BuildConfig.DEBUG) {
            ARouter.openDebug();   //非打包情况下,必须调用调用
            ARouter.openLog();

            IFunc customFunc = new IFunc() {
                @Override
                public int getIcon() {
                    return android.R.drawable.ic_menu_set_as;
                }

                @Override
                public String getName() {
                    return "环境配置";
                }

                @Override
                public boolean onClick() {
                    Intent intent = new Intent(XApp.this, SettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return false;
                }
            };

            Pandora.get().addFunction(customFunc);
        }


        ARouter.init(this);
        SqliteHelper.init(this);

        initDataBase();

        CrashReport.initCrashReport(getApplicationContext(), "28ff5239b4", true);

        int lastVersion = getMyPreferences().getInt(Config.SP_VERSION, 0);
        int current = SysUtil.getVersionCode(this);

//        if (current == 9500) {
//            ZCSetting.deleteAll();
//        }
//        getEditor().putString("getMqttTemp","");
        if (current > lastVersion) {
            getEditor().clear()
                    .putLong(Config.SP_DRIVERID, -1)
                    .putBoolean(Config.SP_ISLOGIN, false)
                    .putInt(Config.SP_VERSION, current)
                    .apply();
        }

        if (Build.VERSION.SDK_INT >= 26) {
            String channelId = getPackageName() + "/pushChannel";
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "普通消息通知栏", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
            notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
            notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }


    private void initDataBase() {
        SqliteHelper.init(this);
    }

    /**
     * 获取ApplicationContext.
     *
     * @return context
     */
    public static XApp getInstance() {
        return instance;
    }

    /**
     * 获取app的SharedPreferences.
     *
     * @return SharedPreferences对象
     */
    public static CsSharedPreferences getMyPreferences() {
        return CsSharedPreferences.getInstance();
    }

    /**
     * 获取SharedPreferences.Editor.
     *
     * @return editor对象
     */
    public static CsEditor getEditor() {
        return CsEditor.getInstance();
    }

    /**
     * 通过字符串资源id,获取字符串.
     *
     * @param resId 需要获取字符串的资源id
     * @return 返回资源id对应的字符串, 如果获取则返回null
     */
    public static String getMyString(@StringRes int resId) {
        String str = null;
        if (instance != null) {
            str = instance.getString(resId);
        }
        return str;
    }

    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");
        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource();
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        if (offlineResource != null) {
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                    offlineResource.getModelFilename());
        }
        return params;
    }

    protected OfflineResource createOfflineResource() {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
        }
        return offlineResource;
    }

    /**
     * 初始化讯飞语音
     */
    public void initBaiduTTs() {
        if (isInti) {
            return;
        }
        Log.e("XApp", "initBaiduTTs");
        isInti = true;
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new SpeechSynthesizerListener() {
            @Override
            public void onSynthesizeStart(String s) {

            }

            @Override
            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

            }

            @Override
            public void onSynthesizeFinish(String s) {

            }

            @Override
            public void onSpeechStart(String s) {
                isSpeeching = true;
            }

            @Override
            public void onSpeechProgressChanged(String s, int i) {

            }

            @Override
            public void onSpeechFinish(String s) {
                isSpeeching = false;
//                if (voiceList != null && !voiceList.isEmpty()) {
//                    syntheticVoice(voiceList.removeFirst());
//                }
                abandonFocus();
            }

            @Override
            public void onError(String s, SpeechError speechError) {

            }
        };

        Map<String, String> params = getParams();

        InitConfig initConfig = new InitConfig(Config.TTS_APP_ID, Config.TTS_APP_KEY, Config.TTS_APP_SECRET, TtsMode.MIX, params, listener);

        // 此处可以改为MySyntherizer 了解调用过程
        mSpeechSynthesizer = new NonBlockSyntherizer(this, initConfig);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mFocusChangeListener = focusChange -> {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Stop playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Lower the volume
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback or Raise it back to normal
            }
        };
    }

    /**
     * 请求语音播放焦点
     *
     * @return
     */
    public boolean requestFocus() {
        haveFoucs = true;
        if (mFocusChangeListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    audioManager.requestAudioFocus(mFocusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            //这种方式获取焦点 会将其他音量调低而不中断其他播放
        }
        return false;
    }

    /**
     * 放弃语音播放焦点
     *
     * @return
     */
    public boolean abandonFocus() {
        haveFoucs = false;
        if (mFocusChangeListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    audioManager.abandonAudioFocus(mFocusChangeListener);
        }
        return false;
    }

//    private LinkedList<String> voiceList;

    /**
     * @param text
     * @param flag 语音播放前的提示音
     */
    public void syntheticVoice(String text, int flag) {
        int resId = 0;
        if (flag == ON_LINE) {
            resId = R.raw.online_sound;
        } else if (flag == OFF_LINE) {
            resId = R.raw.offline_sound;
        } else if (flag == GRAB) {
            resId = R.raw.grab;
        } else if (flag == NEW_MSG) {
            resId = R.raw.new_msg;
        } else if (flag == CANCEL) {
            resId = R.raw.cancel_order;
        } else if (flag == NEW_ANN) {
            resId = R.raw.new_ann;
        }
        if (resId == 0) {
            return;
        }
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;//release时必须将其置位null，不然isPlaying（）会抛异常
        }
        player = MediaPlayer.create(this, resId);
        if (null != player) {
            Log.e("MediaPlayer", "MediaPlayer 创建成功");
            player.setOnCompletionListener(mediaPlayer -> {
                if (null != player) {
                    player.release();
                    player = null;//release时必须将其置位null，不然isPlaying（）会抛异常
                }
                if (StringUtils.isNotBlank(text)) {
                    syntheticVoice(text);
                } else {
                    abandonFocus();
                }
            });
            requestFocus();
            player.start();
        } else {
            Log.e("MediaPlayer", "MediaPlayer 创建失败");
        }
    }

    /**
     * 循环播放静音音频大道保活
     */
    public void playSlientMusic() {
//        if (!getMyPreferences().getBoolean(Config.SP_PLAY_CLIENT_MUSIC, true)) {
//            return;
//        }
        //有焦点时说明在正常播放音频
        if (haveFoucs) {
            return;
        }

        if (!getMyPreferences().getBoolean(Config.SP_ISLOGIN, false)) {
            return;
        }

        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            //release时必须将其置位null，不然isPlaying（）会抛异常
            player = null;
        }
        player = MediaPlayer.create(this, R.raw.silent);
        if (null != player) {
            player.setOnCompletionListener(mediaPlayer -> {
                if (null != player) {
                    player.release();
                    //release时必须将其置位null，不然isPlaying（）会抛异常
                    player = null;
                }
                Log.e("AudioFocus", "播放静音音频完成，循环播放中..");
                playSlientMusic();
            });
            player.start();
            Log.e("AudioFocus", "开始播放静音音频");
        }
    }

    /**
     * 停止播放静音音频
     */
    public void stopPlaySlientMusic() {
        if (haveFoucs) {
            return;
        }

        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            //release时必须将其置位null，不然isPlaying（）会抛异常
            player = null;
            Log.e("AudioFocus", "停止播放静音音频");
        }
    }

//    /**
//     * @param text    要播报的内容
//     * @param isQueue 是否需要排队
//     */
//    public void syntheticVoice(String text, boolean isQueue) {
//        if (voiceList == null) {
//            voiceList = new LinkedList<>();
//        }
//        if (isQueue) {
//            voiceList.add(text);
//            if (!isSpeeching) {
//                syntheticVoice(voiceList.removeFirst());
//            }
//        } else {
//            if (TextUtils.equals(text, lastReadContent)) {
//                if (System.currentTimeMillis() - lastReadTime > 3000) {
//                    stopVoice();
//                    syntheticVoice(text);
//                }
//            } else {
//                stopVoice();
//                syntheticVoice(text);
//            }
//        }
//    }

    /**
     * 播放语音
     *
     * @param msg
     */
    public void syntheticVoice(String msg) {
        if (TextUtils.equals(msg, lastReadContent) && System.currentTimeMillis() - lastReadTime <= 3000) {
            return;
        }
        boolean voiceAble = getMyPreferences().getBoolean(Config.SP_VOICE_ABLE, true);
        if (!voiceAble) {
            return;
        }
        stopVoice();
        if (mSpeechSynthesizer == null) {
            initBaiduTTs();
            return;
        }
        if (requestFocus() && null != mSpeechSynthesizer) {
            lastReadContent = msg;
            lastReadTime = System.currentTimeMillis();
            int code = mSpeechSynthesizer.speak(msg);
        }
    }

    /**
     * 停止播放
     */
    public void stopVoice() {
        if (null != mSpeechSynthesizer) {
            if (isSpeeching) {
                mSpeechSynthesizer.stop();
            }
        }
        abandonFocus();
    }

    /**
     * 清理播放列表
     */
    public void clearVoiceList() {
//        if (null != voiceList) {
//            voiceList.clear();
//        }
    }

    /**
     * 震动
     */
    public void shake() {
        boolean shakeAble = XApp.getMyPreferences().getBoolean(Config.SP_SHAKE_ABLE, true);
        if (shakeAble) {
            PhoneUtil.vibrate(XApp.getInstance(), false);
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.release();
        }
    }

    /**
     * 开启定位服务
     */
    public void startLocService() {
//        if (SysUtil.isServiceWork(this, "com.easymi.component.loc.LocService")) {
//            return;
//        }
        Intent intent = new Intent(this, LocService.class);
        intent.setAction(LocService.START_LOC);
        intent.setPackage(this.getPackageName());
        try {
            startService(intent);
        } catch (Exception ex) {
            CrashReport.postCatchedException(ex);
        }
    }

    /**
     * 关闭定位服务
     */
    public void stopLocService() {
        Intent intent = new Intent(this, LocService.class);
        intent.setAction(LocService.STOP_LOC);
        intent.setPackage(this.getPackageName());
        startService(intent);
    }

    /**
     * 因为应用内存在多进程 会重复调用Application的onCreate() 这里做个判断
     *
     * @return
     */
    protected boolean isAppProcess() {
        int pid = android.os.Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = AndroidProcesses.getRunningAppProcessInfo(this);
        if (processInfos.size() != 0) {
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                if (pid == processInfo.pid) {
                    String currentProcessName = processInfo.processName;
                    if (StringUtils.isNotBlank(currentProcessName)
                            && currentProcessName.equals(getPackageName())) {
                        return true;
                    }
                }
            }
            return false;
        } else { //在7.0以上或者部分手机还是不能获取到
            return true;
        }
    }





}
