package com.kanchi.periyava.version.dashboard.model

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kanchi.periyava.BR
import com.kanchi.periyava.R
import com.kanchi.periyava.model.OnItemClickListener
import com.kanchi.periyava.model.Volume

/**
 * Created by m84098 on 2/15/18.
 */

class VolumeAdapter : RecyclerView.Adapter<VolumeAdapter.ViewHolder> {

    lateinit var context: Context
    var features = ArrayList<Volume>()
    private var selectedPosition = 0

    // Define listener member variable
    private var listener: OnItemClickListener? = null


    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    constructor(context: Context, value: ArrayList<Volume>) : super() {
        this.context = context
        this.features = value
    }

    override fun getItemCount(): Int = features.size;

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.list_item_volume, parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (features[position] != null) {
            if(features[position].id != Volume.Type.NONE)
                holder!!.bind(features[position]!!)
        }
    }

    inner class ViewHolder(private var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        /* override fun onClick(v: View?) {
             binding.root.setOnClickListener {
                 if (listener != null) {
                     val position = adapterPosition
                     if (position != RecyclerView.NO_POSITION) {
                         listener.onItemClick(itemView, position)
                     }
                 } }
         }*/
        fun bind(obj: Volume) {
            binding.root.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener!!.onItemClick(itemView, position, obj)
                    }
                }
            }
            binding.setVariable(BR.volume, obj)
            binding.executePendingBindings()

        }
    }
}
