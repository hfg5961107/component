package com.rvakva.android.person.fragment

import android.os.Bundle
import com.easymi.component.base.RxBaseFragment
import com.rvakva.android.person.R
import kotlinx.android.synthetic.main.fragment_person.*

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: PersonFragment
 * @Author: hufeng
 * @Date: 2019-12-06 15:17
 * @Description:
 * @History:
 */
class PersonFragment : RxBaseFragment(){


    override fun finishCreateView(state: Bundle?) {
        tv_title.setText(arguments?.getString("name"))
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_person
    }

}