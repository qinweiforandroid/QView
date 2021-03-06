package com.android.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.view.databinding.ActivityMainBinding
import com.android.view.demo.EditorFragment
import com.android.view.demo.NumberKeyboardFragment
import com.android.view.demo.TextFragment
import com.android.view.demo.ZXingFragment
import com.qw.framework.ThemeImpl
import com.qw.framework.core.App
import com.qw.framework.core.AppStateTracker
import com.qw.framework.core.theme.Theme
import com.qw.framework.core.ui.BaseFragment
import com.qw.framework.ui.QFragmentActivity
import com.qw.framework.ui.SupportListFragment
import com.qw.list.ui.InstalledAppActivity
import com.qw.list.ui.SectionListActivity
import com.qw.list.ui.SmartRefreshLayoutActivity
import com.qw.list.ui.SwipeRefreshLayoutActivity
import com.qw.theme.Themes

class MainActivity : AppCompatActivity(), SupportListFragment.OnListItemClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.init(App.Builder(this))
        App.online()
        Theme.inject(ThemeImpl())
        AppStateTracker.init(application)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mQToolbar.title = "Demo"
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.mFragmentContainerView,
                SupportListFragment.newInstance(ArrayList<QFragmentActivity.Clazz>().apply {
                    applySampleList(this)
                })
            ).commitAllowingStateLoss()
    }

    private fun applySampleList(list: java.util.ArrayList<QFragmentActivity.Clazz>) {
        list.add(QFragmentActivity.Clazz("Editor", EditorFragment::class.java))
        list.add(QFragmentActivity.Clazz("Text", TextFragment::class.java))
        list.add(QFragmentActivity.Clazz("NumberKeyboard", NumberKeyboardFragment::class.java))
        list.add(QFragmentActivity.Clazz("zxing", ZXingFragment::class.java))

        list.add(QFragmentActivity.Clazz("installedApp", BaseFragment::class.java))
        list.add(QFragmentActivity.Clazz("SmartRefreshLayout", BaseFragment::class.java))
        list.add(QFragmentActivity.Clazz("SwipeRefreshLayout", BaseFragment::class.java))
        list.add(QFragmentActivity.Clazz("SectionList", BaseFragment::class.java))

    }

    override fun onListItemClick(clazzInfo: QFragmentActivity.Clazz) {
        when(clazzInfo.title){
            "installedApp"->{
                startActivity(Intent(this,InstalledAppActivity::class.java))
            }
            "SmartRefreshLayout"->{  startActivity(Intent(this, SmartRefreshLayoutActivity::class.java))}
            "SwipeRefreshLayout"->{  startActivity(Intent(this, SwipeRefreshLayoutActivity::class.java))}
            "SectionList"->{  startActivity(Intent(this, SectionListActivity::class.java))}
            else->{
                startActivity(QFragmentActivity.getIntent(this, clazzInfo))
            }
        }
    }
}