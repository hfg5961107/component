package com.easymi.component.rxmvp;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @Description: Rx管理者
 * @author: developerLzh
 * @date: 2017/1/21
 */

public class RxManager {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription(); //管理订阅者

    public void add(Subscription m) {
        mCompositeSubscription.add(m);  //增加订阅源
    }

    public void clear() {
        mCompositeSubscription.unsubscribe(); //取消订阅
    }

}
