package com.rvakva.android.business.fragment

import android.os.Bundle
import com.easymi.component.base.RxBaseFragment
import com.rvakva.android.business.R
import kotlinx.android.synthetic.main.fragment_business.*

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: FoundPersonFragment
 * @Author: hufeng
 * @Date: 2019-12-07 15:11
 * @Description:
 * @History:
 */
class FoundPersonFragment : RxBaseFragment(){

     var type : Int = 0

    fun newInstance(index:Int) : FoundPersonFragment{
        var fragment = FoundPersonFragment()
        this.type = index
        return fragment
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_business
    }

    override fun finishCreateView(state: Bundle?) {
        tv_businees_name!!.setText("业务："+arguments!!.get("name"))
    }

}