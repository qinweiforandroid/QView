package com.qw.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qw.framework.core.ui.BaseActivity
import com.qw.list.R

class RatingBarActivity : BaseActivity() {
    private lateinit var mRecyclerView: RecyclerView

    override fun setContentView() {
        setContentView(R.layout.rating_bar_activity, true)
    }

    override fun initView() {
        mRecyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = ListAdapter()
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var max = 5
        private var currentLevel = -1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return Holder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.star_item_layout, parent, false)
            )
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var mPosition = 0

            init {
                itemView.setOnClickListener {
                    currentLevel = if (currentLevel == mPosition) {
                        -1
                    } else {
                        mPosition
                    }
                    notifyDataSetChanged()
                }
            }

            fun initData(position: Int) {
                this.mPosition = position
                if (position <= currentLevel) {
                    (itemView as ImageView).setImageResource(R.drawable.pick_ic_period_checked)
                } else {
                    (itemView as ImageView).setImageResource(R.drawable.pick_ic_period_normal)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is Holder) {
                holder.initData(position)
            }
        }

        override fun getItemCount(): Int {
            return max
        }
    }
}