package com.kanchi.periyava.component

/**
 * Created by m84098 on 2/23/18.
 */

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

class AutofitRecyclerView : RecyclerView {
    private var manager: GridLayoutManager? = null
    private var columnWidth = -1 //default value

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.columnWidth)
            val array = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }

        manager = GridLayoutManager(getContext(), 1)
        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {

        if (columnWidth > 0) {
            val spanCount = Math.max(1, measuredWidth / columnWidth)
            manager!!.spanCount = spanCount
        }
        var expandSpec:Int = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2,
                View.MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, expandSpec)
    }
}
