package com.easymi.component.network;

import com.easymi.component.result.EmResult2;

import rx.functions.Func1;

/**
 * Created by Administrator on 2016/9/26.
 */


/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class HttpResultFunc2<T> implements Func1<EmResult2<T>, T> {

    int errCode;

    public HttpResultFunc2() {
        errCode = 1;
    }

    public HttpResultFunc2(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public T call(EmResult2<T> t) {
        if (t.getCode() != errCode) {
            String msg = t.getMessage();
            for (ErrCode errCode : ErrCode.values()) {
                if (t.getCode() == errCode.getCode()) {
                    msg = errCode.getShowMsg();
                    break;
                }
            }
            throw new ApiException(t.getCode(), msg);
        } else {
            return t.getData();
        }
    }
}
