package com.kanchi.periyava.version.dashboard.model

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kanchi.periyava.model.OnItemClickListener
import java.lang.reflect.Array.getDouble
import java.nio.file.Files.exists
import android.util.DisplayMetrics
import com.kanchi.periyava.old.Activity.MainActivity
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.kanchi.periyava.model.Audio
import com.kanchi.periyava.model.Feature
import com.kanchi.periyava.model.MoreLineage
import com.kanchi.periyava.version.dashboard.activity.DashBoardActivity
import kotlin.reflect.KClass


/**
 * Created by m84098 on 2/15/18.
 */

class GenericAdapter(var context: Context,
                     var mapping: HashMap<KClass<*>, ArrayList<Int>>,
                     var values: ArrayList<Any>,
                     listener: OnItemClickListener)
                    : RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    var TAG: String = DashBoardActivity::class.java.simpleName
    private var listener: OnItemClickListener? = listener
    override fun getItemCount(): Int = values.size;

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val layoutid = getLayoutId(when(values[viewType]) {
            is Feature -> Feature::class
            is MoreLineage -> MoreLineage::class
            is Audio -> Audio::class
            else -> Feature::class
        }
        )
        return ViewHolder(DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutid, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (values[position] != null) {
            holder!!.bind(values[position]!!)
        }
    }

    inner class ViewHolder(private var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: kotlin.Any) {
            binding.root.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(itemView, position, data)
                    }
                }
            }
            binding.setVariable(getBindingId(
                    when(data) {
                        is Feature -> Feature::class
                        is MoreLineage -> MoreLineage::class
                        is Audio -> Audio::class
                        else -> Feature::class
                    }
            ), data)
            binding.executePendingBindings()

        }
    }


    fun getLayoutId(cls:KClass<*>):Int{
        return mapping.get(cls)!![0]
    }

    fun getBindingId(cls:KClass<*>):Int{
        return mapping.get(cls)!![1]
    }
}
