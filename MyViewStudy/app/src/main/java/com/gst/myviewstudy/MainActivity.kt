package com.gst.myviewstudy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.gst.myviewstudy.sysViewTest.ActSysViewTest
import com.gst.myviewstudy.ui.adapter.MainGamesAdapter
import com.gst.myviewstudy.ui.adapter.OnRecyclerViewItemOnClickedListener
import com.gst.myviewstudy.ui.base.ConstantBeans
import com.gst.myviewstudy.ui.bean.ViewInfo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnRecyclerViewItemOnClickedListener {

    lateinit var mAdapter: MainGamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_all_games.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        mAdapter = MainGamesAdapter(this, ConstantBeans.viewInfos)
        rv_all_games.adapter = mAdapter
    }

    override fun onItemClicked(v: View?, obj: Any?) {
        if (obj is ViewInfo) {
            var intent = Intent(this, ActSysViewTest::class.java)
            intent.putExtra(ActSysViewTest.TAG_MOTHED_NAME, obj.name)
            startActivity(intent)
        }
    }

}
