package com.gm.soundzones.view

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Created by Pavel Aizendorf on 26/09/2017.
 */

class SoundControlView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val wheelView: WheelView
    private val button: TextView

    init {
        wheelView = WheelView(context, attrs, defStyleAttr)
                .also {
                    addView(it, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                }
        button = TextView(context, attrs, defStyleAttr)
                .also {
                    addView(it, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            .also { it.gravity = Gravity.CENTER })
                }
        wheelView.onChange = { value ->
            this.onChange?.let { it(value) }
        }

    }

    var onChange: ((percent: Double) -> Unit)? = null
    fun setPosition(percentage: Double) = wheelView.setPosition(percentage)
    fun setText(@StringRes resId: Int) {
        button.setText(resId)
    }

    fun setText(text: CharSequence?) {
        button.text = text
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }

    override fun setEnabled(enabled: Boolean) {
        wheelView.isEnabled = enabled
        button.isEnabled = enabled
    }


}
