package com.example.kenkl.ledapp

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.sdk25.coroutines.*

class MainActivityUI : AnkoComponent<MainActivity>, AnkoLogger{
    var ip = "192.168.7.12"
    var port = 42069
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        val choices = arrayOf("Off", "Solid", "Rainbow", "Rainbow Cycle")

        relativeLayout {
            fun sendData(data: ControlData) = doAsync{
                info("Sending data")
                val conn = LightConnection(ip, port).send(data)
                info("Sent data")
                if (conn != null) {
                    uiThread {
                        toast("Problem: ${conn.message}")
                    }
                }
            }
            val red = seekBar {
                id = R.id.red
                progressDrawable.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
                thumb.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
                onSeekBarChangeListener {
                    onProgressChanged { seekBar, it, b ->
                        info("Progress: $it")
                        sendData(ControlData("red", it))
                    }
                }
            }.lparams {
                padding = dip(10)
                margin = dip(5)
                width = matchParent
            }

            val green = seekBar {
                id = R.id.green
                progressDrawable.colorFilter = PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
                thumb.colorFilter = PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
                onSeekBarChangeListener {
                    onProgressChanged { seekBar, it, b ->
                        info("Progress: $it")
                        sendData(ControlData("green", it))
                    }
                }
            }.lparams {
                padding = dip(10)
                margin = dip(5)
                width = matchParent
                below(red)
            }

            val blue = seekBar {
                id = R.id.blue
                progressDrawable.colorFilter = PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
                thumb.colorFilter = PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
                onSeekBarChangeListener {
                    onProgressChanged { seekBar, it, b ->
                        info("Progress: $it")
                        sendData(ControlData("blue", it))
                    }
                }
            }.lparams {
                padding = dip(10)
                margin = dip(5)
                width = matchParent
                below(green)
            }

            spinner {
                id = R.id.spinner
                adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, choices)
                onItemSelectedListener {
                    onItemSelected { p0, p1, p2, p3 ->
                        info("Click")
                        val anim = when (selectedItem) {
                            "Off" -> 0
                            "Solid" -> 1
                            "Rainbow" -> 2
                            "Rainbow Cycle" -> 3
                            else -> 0
                        }
                        sendData(ControlData("animation", anim))
                    }
                }
            }.lparams {
                padding = dip(20)
                width = matchParent
                below(blue)
            }

            floatingActionButton {
                imageResource = android.R.drawable.ic_input_add
                onClick {
                    alert {
                        customView {
                            verticalLayout {
                                toolbar {
                                    lparams(width = matchParent, height = matchParent)
                                    backgroundColor = ContextCompat.getColor(ctx, R.color.colorPrimaryDark)
                                    title = "Settings"
                                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                                }
                                val addr = editText {
                                    hint = "IP address"
                                    padding = dip(20)
                                    text.append(ip)
                                }
                                val p = editText {
                                    hint = "Port"
                                    padding = dip(20)
                                    text.append(port.toString())
                                }
                                positiveButton("Go") {
                                    when {
                                        addr.text.toString().isEmpty() -> toast("Address can't be blank!")
                                        p.text.toString().isEmpty() -> toast("Port can't be blank!")
                                        try {
                                            p.text.toString().toInt(); false
                                        } catch (e: Exception) {
                                            true
                                        } -> toast("Port has to be just a number.")
                                        else -> {
                                            ip = addr.text.toString()
                                            port = p.text.toString().toInt()
                                        }
                                    }
                                }
                            }
                        }
                    }.show()
                }
            }.lparams {
                margin = dip(10)
                alignParentBottom()
                alignParentRight()
                alignParentEnd()
                gravity = Gravity.BOTTOM or Gravity.END
            }
        }.apply {
            layoutParams = FrameLayout.LayoutParams(matchParent, matchParent).apply {
                leftMargin = dip(5)
                rightMargin = dip(5)
            }
        }
    }
}