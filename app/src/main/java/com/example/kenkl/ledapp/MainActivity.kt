package com.example.kenkl.ledapp

import android.app.Activity
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.setContentView

class MainActivity : AppCompatActivity() {
    val ui = MainActivityUI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        get {
            ui.ip = getString("lastIP", "192.168.7.2")
            ui.port = getInt("lastPort", 42069)
        }
        savedInstanceState?.let {
            ui.ip = it.getString("lastIP")
            ui.port = it.getInt("lastPort")
        }
        ui.setContentView(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("lastIP", ui.ip)
        outState?.putInt("lastPort", ui.port)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()

        save {
            putString("lastIP", ui.ip)
            putInt("lastPort", ui.port)
        }
    }

    companion object {
        const val SAVE_FILE = "led_conn_data"
    }
}

fun Activity.save(block: SharedPreferences.Editor.() -> Unit) {
    getSharedPreferences(MainActivity.SAVE_FILE, 0).edit().apply(block).apply()
}

fun Activity.get(block: SharedPreferences.() -> Unit) {
    getSharedPreferences(MainActivity.SAVE_FILE, 0).apply(block)
}
