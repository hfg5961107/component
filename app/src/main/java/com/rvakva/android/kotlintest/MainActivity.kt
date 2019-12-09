package com.rvakva.android.kotlintest

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.easymi.component.base.RxBaseActivity
import com.rvakva.android.business.fragment.FoundPersonFragment
import com.rvakva.android.person.fragment.PersonFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RxBaseActivity() {


    lateinit var f1: FoundPersonFragment
    lateinit var f2: FoundPersonFragment
    lateinit var f3: FoundPersonFragment
    lateinit var f4: FoundPersonFragment
    lateinit var f5: PersonFragment

    private var currentFragment: Fragment? = null

//    val mTabTitle = listOf(
//        this.resources.getString(R.string.main_tabs_1),
//        this.resources.getString(R.string.main_tabs_2),
//        this.resources.getString(R.string.main_tabs_3),
//        this.resources.getString(R.string.main_tabs_4),
//        this.resources.getString(R.string.main_tabs_5)
//    )

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
                    switchFragment(transaction, f1)
                }
                R.id.tab_2 -> {
                    switchFragment(transaction, f2)
                }
                R.id.tab_3 -> {
                    switchFragment(transaction, f3)
                }
                R.id.tab_4 -> {
                    switchFragment(transaction, f4)
                }
                R.id.tab_5 -> {
                    switchFragment(transaction, f5)
                }
                else -> true
            }
        }
        initFragment()
    }

    fun initFragment() {
        f1 = FoundPersonFragment().newInstance(1)
        f1.arguments?.putString("name",resources.getString(R.string.main_tabs_1))

        f2 = FoundPersonFragment().newInstance(2)
        f2.arguments?.putString("name",resources.getString(R.string.main_tabs_2))

        f3 = FoundPersonFragment().newInstance(3)
        f3.arguments?.putString("name",resources.getString(R.string.main_tabs_3))

        f4 = FoundPersonFragment().newInstance(4)
        f4.arguments?.putString("name",resources.getString(R.string.main_tabs_4))

        f5 = PersonFragment()
        f5.arguments?.putString("name",resources.getString(R.string.main_tabs_5))
    }

    private fun switchFragment(transaction: FragmentTransaction, targetFragment: Fragment): Boolean {
        if (!targetFragment.isAdded) {
            currentFragment?.let {
                transaction
                    .hide(it)
                    .add(R.id.frameLayout, targetFragment)
                    .commit()
            }
            println("还没添加呢")
        } else {
            currentFragment?.let {
                transaction
                    .hide(it)
                    .show(targetFragment)
                    .commit()
            }
            println("添加了( ⊙o⊙ )哇")
        }
        currentFragment = targetFragment
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
