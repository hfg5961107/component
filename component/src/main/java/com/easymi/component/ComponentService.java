package com.easymi.component;

import com.easymi.component.entity.PassengerLcResult;
import com.easymi.component.entity.RechargeResult;
import com.easymi.component.result.EmResult2;
import com.google.gson.JsonElement;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ComponentService {

    /**
     * 乘客位置请求
     *
     * @param
     * @return
     */
    @GET("api/v1/message/location/passenger")
    Observable<PassengerLcResult> passengerLoc(@Query("orderId") Long orderId);

    @FormUrlEncoded
    @POST("api/v1/system/pay")
    Observable<EmResult2<JsonElement>> payOrder(@Field("driver") boolean isDriver,
                                                @Field("id") Long orderId,
                                                @Field("channel") String channel);

    /**
     * 司机充值
     *
     * @param channel 充值方式
     * @param fee     充值金额
     * @return
     */
    @FormUrlEncoded
    @POST("api/v1/resources/driver/recharge")
    Observable<RechargeResult> recharge(@Field("channel") String channel,
                                        @Field("fee") Double fee);

}
