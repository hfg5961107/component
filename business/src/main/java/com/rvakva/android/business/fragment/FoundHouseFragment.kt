package com.rvakva.android.business.fragment

import android.os.Bundle
import com.easymi.component.base.RxBaseFragment
import com.rvakva.android.business.R
import kotlinx.android.synthetic.main.fragment_business.*

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: FoundHouseFragment
 * @Author: hufeng
 * @Date: 2019-12-09 15:57
 * @Description:
 * @History:
 */
class FoundHouseFragment : RxBaseFragment(){

    var type : Int = 0

    fun newInstance(index:Int) : FoundHouseFragment{
        var fragment = FoundHouseFragment()
        this.type = index
        return fragment
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_business
    }

    override fun finishCreateView(state: Bundle?) {
        tv_businees_name.text = "业务：44444"
    }

}