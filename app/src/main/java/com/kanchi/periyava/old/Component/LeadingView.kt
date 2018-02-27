package com.kanchi.periyava.old.Component

import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.annotation.TargetApi
import android.text.SpannableString
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.kanchi.periyava.R
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.layout_leadingview.view.*


/**
 * Created by m84098 on 2/14/18.
 */


class LeadingView : RelativeLayout {
    lateinit var  view:View
    init {
        view = View.inflate(context, R.layout.layout_leadingview, null)

    }

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr){
        onCreate(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes){
        onCreate(attrs)
    }



    fun onCreate(attrs: AttributeSet?) {

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it,
                    R.styleable.LeadingView_attributes, 0, 0)
            val title = resources.getText(typedArray
                    .getResourceId(R.styleable
                            .LeadingView_attributes_text, R.string.empty))

            val ico = resources.getDrawable(typedArray
                    .getResourceId(R.styleable
                            .LeadingView_attributes_src,R.drawable.perivaya_sitting))
            val leftMargin = ico.intrinsicWidth + 10

            content.text = title
            icon.setImageDrawable(ico)


            val ss = SpannableString(title)
            // Выставляем отступ для первых трех строк абазца
            ss.setSpan(CustomLeadingMarginSpan2(3, leftMargin), 0, ss.length, 0)

            content.text = ss
            typedArray.recycle()
        }


        addView(view)

    }

}
    class CustomLeadingMarginSpan2(private val lines: Int, private val margin: Int) : LeadingMarginSpan.LeadingMarginSpan2 {

        /* Возвращает значение, на которе должен быть добавлен отступ */
        override fun getLeadingMargin(first: Boolean): Int {
            return if (first) {

                margin
            } else {

                0
            }
        }

        override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int,
                                       top: Int, baseline: Int, bottom: Int, text: CharSequence,
                                       start: Int, end: Int, first: Boolean, layout: Layout) {
        }


        override fun getLeadingMarginLineCount(): Int {
            return lines
        }
    }
