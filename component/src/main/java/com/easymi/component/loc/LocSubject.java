package com.easymi.component.loc;

import com.easymi.component.entity.EmLoc;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public interface LocSubject {
    //添加观察者
    void addObserver(LocObserver obj);
    //移除观察者
    void deleteObserver(LocObserver obj);
    //当主题方法改变时,这个方法被调用,通知所有的观察者
    void notifyObserver(EmLoc loc);
}
