## 对应包名及其作用

| 包名        | 释义   |  追加描述  |
| --------   | :-----:  | :----:  |
| activity     | 组件性质的activity   |   最基础多模块都常用的activity     |
| adapter      |   适配器   |      |
| app          |    Application基础类及Activity管理类    |  Application的配置和activity的管理  |
| base         |    rx基础类    |  rxActivity与rxFragment（基础base的封装）  |
| db           |    数据库    |  数据库表的操作（更改数据库记得加版本号）  |
| entity       |    实体类    |  常用和公共的实体  |
| loc          |    定位服务相关    | 位置分发用到了观察者模式   |
| network      |    网络层    | RxJava+Retrofit   |
| pay          |    支付功能封装    | 银联、微信、支付宝   |
| permission   |    Android 6.0+权限申请    | RxJava设计   |
| push         |    即时通信    | Mqtt   |
| receiver     |    系统级广播接收器    |  主要是gps和网络变化监测  |
| result       |    网络请求返回实体基础类    |  code、data、msg格式  |
| rxmvp        |    RxJava + MVP模式基础类    |  主要有添加订阅和取消订阅  |
| share        |    第三方分享    |  qq和微信相关配置和监听  |
| trace        |    轨迹纠偏工具    |  定位点位置轨迹纠偏工具  |
| update       |    应用更新组件    |  应用系统内更新模块  |
| utils        |    各种常用的工具类    |  项目中公用或常用的工具集合  |
| widget       |    自定义控件   |  公用或常用的视图控件集合  |

# How To Use
## XApp.java
应用的Application应该继承自该类。包含了ARouter、DataBase、讯飞IflytekTTS、bugly以及其他三方库和常用配置的初始化，提供给外部调用的函数：
```java
    /**
     * 语音合成
     * @param text    要播报的内容
     * @param isQueue 是否需要排队
     */
    public void syntheticVoice(String text, boolean isQueue) {
    }
```
```java
    /**
     * 手机震动
     */
    public void shake() {
    }
```
```java
    /**
     * 开启定位服务
     */
    public void startLocService() {
    }
```
```java
    /**
     * 关闭定位服务
     */
    public void stopLocService() {
    }
```

## RxBaseActivity.java
所有activity的基类，**所有的Activity都应该继承自该类**，内部提供Activity栈管理、网络监控、Gps监控、Rx管理、权限请求管理、选择图片、tiredReceiver、HttpCustomReceiver等的事件接收和统一分发等。

## SqliteHelper.java
数据库帮助类，继承自SQLiteOpenHelper。静态变量VERSION代表数据库版本号，版本号只能增加不能减，在数据库更新时需要增加版本号，会回调onUpgrade函数，避免应用需要卸载安装。具体使用方法参见SQLiteOpenHelper注释文档

## 几个重要的实体类
### DymOrder.java
这个在该项目的顶层业务模块有使用到，订单信息相关，会保存到数据库
### EmLoc.java
位置信息

## LocService.java
核心定位服务,通过改变Config.NORMAL_LOC_TIME的值可改变定位周期。在司机端App上需要常驻通知栏持续定位，故开启前台进程的代码：
```java
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            locClient.enableBackgroundLocation(NOTI_ID, buildNotification());
        } else {
            startForeground(NOTI_ID, buildNotification());
        }
```
这里重点提一下关于兼容Andorid O的代码片段，由于Andorid O上加强了通知的管理，在开启通知栏前必须创建通知渠道：
```java
 if (Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationChannel.setSound(null, null);
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        }
```
在代码中你还会见到一些以Trace结尾的函数，如：startTrace()、stopTrace()等。这些是之前弃用的纠偏相关的一些函数，因为一些三方原因（纠偏不准啊之类的）暂时没有使用到，有兴趣可以研究下纠偏相关课题：
[轨迹实时纠偏LBSTraceClient](http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html)
[猎鹰sdk服务类AMapTrackClient](http://a.amap.com/lbs/static/unzip/Android_Track_Doc/index.html)

在定位成功后，会发出广播给LocReceiver.java处理

## LocReceiver.java
定位服务处理中转站，采用了观察者的设计模式。在任何地方你需要使用到实时的位置信息时，添加一个观察者即可。调用函数：
```java
LocReceiver.getInstance.addObserver(this);
```
移除观察者：
```java
LocReceiver.getInstance.deleteObserver(this);
```
实时位置回调：
```java
void receiveLoc(EmLoc loc);
```
当然，在有些地方你并不需要实时回调位置，只需要知道上次的即可，你可以调用如下：
```java
EmLoc loc = EmUtil.getLastLoc();
```
获得上次的位置信息

## http/https请求
采用了RxJava2+Retrofit2++OkHttp3的基础、封装。通过下面的一个代码片段就能明白大致用法了：
```java
Observable<Object> observable = ApiManager.getInstance().createApi(Config.HOST, BaocheService.class)
                .updateApply(useCarApply.id, 3, useCarApply.version)
                .map(new HttpResultFunc2<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mRxManager.add(observable.subscribe(new MySubscriber<Object>(this,
                true,
                true,
                o -> {
                    cancelTimer();
                    Intent intent = new Intent(ApplyDetailActivity.this, OperationSucActivity.class);
                    intent.putExtra("type", 3);
                    intent.putExtra("orderId", useCarApply.id);
                    startActivity(intent);
                    finish();
                })));
```

就目前而言，后台返回的数据存在两种情况，一种是：
```json
{
    "code":1,
    "msg":"success",
    "data":"对象object",
    "total":10
    }
```
上述的就是EmResult2所对应的json数据，EmResult2带有一个泛型，即data所对应的对象。如果上级只需要处理data对象，Rx操作符可以选择map(),然后传入HttpResultFunc2.java的实体即可，如果上级是那种需要访问json根节点数据的，比如返回的列表数据我要知道total是多少，Rx操作符就可以选择fliter(),传入HttpResultFunc3.java的实体得到EmResul2对象。

另一种：
```json
{
    "code":1,
    "msg":"success",
    "xxx":"对象object",
    "xxx":10
    }
```
此类json数据只有code和msg是确定了其字段名的，故在解析时要定义一个新的对象继承自EmResult.java，Rx操作符选择filter(),传入HttpResultFunc.java实体，HttpResultFunc的泛型为继承自EmResult.java的类。

**一定注意在什么场合下使用HttpResultFunc、HttpResultFunc2、HttpResultFunc3**

### 两个重要的拦截器
EncryptInterceptor：网络请求参数加密拦截器
TokenInterceptor：统一添加请求头token、appKey,检验身份信息。token来自登录

### 一个重要的转换器
后台返回的数据都是加密的数据，需要使用一个response转换器来转换成json。KeyGsonRequestBodyConverter类里面解密.

### 错误码
ErrCode.java里面存放了各式错误码，如果后台新增了错误码，在此文件里面增加即可

### 统一错误管理
MySubscriber.java里面在网络请求任何一环节出错都会回调onError(Throwable e)

### Ssl
Ssl.java,信任所有证书

## 长连接
项目中的长连接是基于Mqtt协议的，长连接客户端基于第三方开源框架Eclipse paho，做了一些封装。
[什么是mqtt?戳这里](https://www.eclipse.org/paho/clients/java/)

### MqttManager.java
封装的一个mqtt管理类，在任何你需要开启mqtt服务场景下直接调用
```java
MqttManager.getInstance().creatConnect();
```
在creatConnect()函数内部有逻辑判断,在什么情况下能连接，故在外部调用时不再需要判断是否登录啊、是否正在运行中之类的。
断开mqtt调用以下代码：
```java
MqttManager.getInstance().release();
```
向服务端Push消息：
```java
MqttManager.getInstance().publish("json字符串");
```
服务端下发数据：
```java
/**
 * 服务端下发的数据消息回调
 * @param topic
 * @param message
 * @throws Exception
 */
@Override
void messageArrived(String topic, MqttMessage message){
}
```
在接收到数据后，该函数内部会将消息以广播发出，由MessageReceiver.java处理

### MessageReceiver.java
消息处理类，根据消息的不同type区分消息类型，作相应的处理。在订单信息和乘客位置信息这里使用到了观察者的设计模式，在有需要的地方添加观察者即可。

## 更新组件
如果需要更换检测更新的地址，在UpdateHelper.java里的checkUrl()更改链接地址。
## 工具类
util包下大多的工具类能够通过名字来辨识作用，这里重点说下EmUtil。这里面有一些很实用的函数，比如完全退出等等
## 配置文件Config.java
最常用的配置类，包含了项目所有基本配置信息。如后台地址、mqtt配置、appkey配置、七牛云上传回显地址、分享app_id、SharedPrefence常量配置等等 
打包需要替换的字段：

字段|释义
--|:--:
HOST|域名地址
MQTT_HOST|长连接地址
APP_KEY|系统key
QQ_APP_ID|QQ分享id
WX_APP_ID|微信分享id

## 特殊或重要类使用介绍
 * NaviActivity 导航activity，所有需要导航的业务都是使用这个activity进行导航，只需要传入起点终点信息即可进行基本导航。
 * ApiManager 网络请求的管理类，设置网络请求的相关配置，以及添加日志、token、加密等拦截器等。设置日志输出等级。

 