package com.example.kenkl.ledapp

import android.content.Context
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.sdk25.coroutines.onSeekBarChangeListener

class ColorBar(ctx: Context?) : RelativeLayout(ctx) {
    private var onProgressChange: ((Int) -> Unit)? = null
    private fun init() = AnkoContext.createDelegate(this).apply {
        seekBar {
            onSeekBarChangeListener {
                onProgressChanged { seekBar, progress, fromUser ->
                    onProgressChange?.invoke(progress)
                }
            }
        }
    }

    fun onProgressChange(block: (Int) -> Unit) {
        this.onProgressChange = block
    }
}

inline fun ViewManager.colorBar(init: ColorBar.() -> Unit) = ankoView(::ColorBar, 0, init)