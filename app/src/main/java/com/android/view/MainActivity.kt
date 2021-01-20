package com.android.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.view.databinding.ActivityMainBinding
import com.android.view.demo.NumberKeyboardFragment
import com.android.view.demo.TextFragment
import com.android.view.demo.ZXingFragment
import com.qw.framework.App
import com.qw.framework.AppStateTracker
import com.qw.framework.ui.QFragmentActivity
import com.qw.framework.ui.SupportListFragment

class MainActivity : AppCompatActivity(), SupportListFragment.OnListItemClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.init(application, MainActivity::class.java)
        AppStateTracker.getInstance().appState = AppStateTracker.APP_STATE_ONLINE
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mQToolbar.title = "Demo"
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.mFragmentContainerView,
                SupportListFragment.newInstance(ArrayList<QFragmentActivity.Clazz>().apply {
                    this.add(QFragmentActivity.Clazz("Text", TextFragment::class.java))
                    this.add(
                        QFragmentActivity.Clazz(
                            "NumberKeyboard",
                            NumberKeyboardFragment::class.java
                        )
                    )
                    this.add(
                        QFragmentActivity.Clazz(
                            "zxing",
                            ZXingFragment::class.java
                        )
                    )
                })
            ).commitAllowingStateLoss()
    }

    override fun onListItemClick(clazzInfo: QFragmentActivity.Clazz?) {
        startActivity(QFragmentActivity.getIntent(this, clazzInfo))
    }
}