package com.easymi.component.app;

import android.app.Activity;
import android.text.TextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author xyin
 * @date 2016/12/14
 * 管理activity,使用内部容器法实现单例模式.
 */

public class ActManager {

    private static Stack<Activity> activityStack;

    private ActManager() {
        activityStack = new Stack<>();
    }

    /**
     * SingletonHolder实现单例模式.
     */
    private static class SingletonHolder {
        private static final ActManager INSTANCE = new ActManager();
    }

    /**
     * 获得实例方法.
     *
     * @return 返回单例对象
     */
    public static ActManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int getSize() {
        return activityStack == null ? 0 : activityStack.size();
    }

    public Activity getTopActivity() {
        Activity activity = null;
        if (activityStack != null) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 判断栈中是否存在该Activity.
     *
     * @param activityName 需要判读的activity名字
     * @return 如果存在则返回true, 否则返回false
     */
    public boolean existActivity(String activityName) {
        if (!TextUtils.isEmpty(activityName) && activityStack != null) {
            for (Activity activity : activityStack) {
                String name = activity.getLocalClassName();
                if (!TextUtils.isEmpty(name) && name.contains(activityName)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean existActivity(Class<?> cls) {
        if (cls!=null && activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 增加一个activity实例在Stack中.
     *
     * @param activity activity实例
     */
    public void addActivity(Activity activity) {
        if (activityStack != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 从stack中移除一个activity实例.
     *
     * @param activity 需要移除的activity实例
     */
    public void removeActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取栈顶activity的名字,该名字从包名结束出开始.
     *
     * @return activity name
     */
    public String getTopLocalClassName() {
        String activityName = null;
        if (activityStack != null && !activityStack.isEmpty()) {
            Activity activity = activityStack.lastElement();
            if (activity != null) {
                activityName = activity.getLocalClassName();
            }
        }
        return activityName;
    }

    /**
     * finish top activity.
     */
    public void finishTopActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * finish一个activity,并将该activity从stack中移除.
     *
     * @param activity activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * finish一个指定类型activity.
     *
     * @param cls 该activity的class
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            List<Activity> activities = new LinkedList<>();
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    activities.add(activity);
                }
            }
            for (Activity activity : activities) {
                finishActivity(activity);
            }
        }
    }

    /**
     * finish activity by localClassName.
     *
     * @param localClassName 该名字为app包名后的部分.
     */
    public void finishActivity(String localClassName) {
        if (TextUtils.isEmpty(localClassName)) {
            return;
        }
        if (activityStack != null) {
            List<Activity> activities = new LinkedList<>();
            for (Activity activity : activityStack) {
                String name = activity.getLocalClassName(); //返回的是包名后的部分
                if (!TextUtils.isEmpty(name) && name.contains(localClassName)) {
                    activities.add(activity);
                }
            }
            for (Activity activity : activities) {
                finishActivity(activity);
            }
        }
    }

    /**
     * finish掉所有的activity,并清空stack.
     */
    public void finishAllActivity() {

        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * finish除了exclusiveName 名单外的所有activity
     *
     * @param exclusiveName 需要排除的activity的localClassName
     */
    public void finishAllActivity(String... exclusiveName) {
        if (exclusiveName == null || exclusiveName.length <= 0) {
            finishAllActivity();
            return;
        }
        if (activityStack != null) {
            List<Activity> activities = new LinkedList<>();
            for (Activity activity : activityStack) {
                String localName = activity.getLocalClassName(); //返回的是包名后的部分
                if (!contains(localName, exclusiveName)) {
                    activities.add(activity);
                }
            }

            for (Activity activity : activities) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 判断localName的activity是否在被排除的列表中.
     *
     * @param localName     需要检测的localName
     * @param exclusiveName 排除的列表
     * @return 如果包含在排除列表中返回true, 否则返回false
     */
    private boolean contains(String localName, String... exclusiveName) {
        boolean contains = false;
        if (exclusiveName != null && !localName.isEmpty()) {
            for (String name : exclusiveName) {
                if (localName.contains(name)) {
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

}
