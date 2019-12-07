package com.rvakva.android.business.fragment

import android.os.Bundle
import com.easymi.component.base.RxBaseFragment
import com.rvakva.android.business.R
import kotlinx.android.synthetic.main.fragment_business.*

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: BusinessFragment
 * @Author: hufeng
 * @Date: 2019-12-07 15:11
 * @Description:
 * @History:
 */
class BusinessFragment : RxBaseFragment(){

    override fun getLayoutResId(): Int {
        return R.layout.fragment_business
    }

    override fun finishCreateView(state: Bundle?) {
        tv_businees_name.setText("业务："+arguments!!.get("name"))
    }

}