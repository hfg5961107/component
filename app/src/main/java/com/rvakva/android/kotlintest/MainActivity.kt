package com.rvakva.android.kotlintest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.easymi.component.base.RxBaseActivity
import com.rvakva.android.person.fragment.PersonFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RxBaseActivity() {


    var f1 : PersonFragment? = null
    var f2 : PersonFragment? = null
    var f3 : PersonFragment? = null
    var f4 : PersonFragment? = null
    var f5 : PersonFragment? = null


    override fun isEnableSwipe(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews(savedInstanceState: Bundle?) {
        bottomBar.setOnNavigationItemSelectedListener {
            val transaction = supportFragmentManager.beginTransaction()
            hideAllFragment(transaction)
            when (it.itemId) {
                R.id.tab_1 -> {
                    setFragmentPosition(transaction,1)
                }
                R.id.tab_2 -> {
                    setFragmentPosition(transaction,2)
                }
                R.id.tab_3 -> {
                    setFragmentPosition(transaction,3)
                }
                R.id.tab_4 -> {
                    setFragmentPosition(transaction,4)
                }
                R.id.tab_5 -> {
                    setFragmentPosition(transaction,5)
                }
                else -> true
            }
        }
    }

    fun setFragmentPosition(transaction : FragmentTransaction,index:Int) : Boolean{
        if(f1==null){
            f1 = PersonFragment()
            transaction.add(R.id.frameLayout, f1!!)
        }else{
            transaction.show(f1!!)
        }
        return true
    }



    //隐藏所有Fragment
    fun hideAllFragment(transaction: FragmentTransaction) {
        if (f1 != null) {
            transaction.hide(f1!!)
        }
        if (f2 != null) {
            transaction.hide(f2!!)
        }
        if (f3 != null) {
            transaction.hide(f3!!)
        }
        if (f4 != null) {
            transaction.hide(f4!!)
        }
        if (f5 != null) {
            transaction.hide(f5!!)
        }

    }


}
