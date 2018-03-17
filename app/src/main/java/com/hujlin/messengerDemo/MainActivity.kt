package com.hujlin.messengerDemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val messenger: Messenger = Messenger(MyHandler(this))

    class MyHandler(context: Context) : Handler() {
        private val mContext: MainActivity = context as MainActivity
        override fun handleMessage(msg: Message?) {
            when (msg!!.what) {
                2 -> {
                    Log.e("MainActivity", msg.data.getString("msg2"))
                    mContext.textView.text = msg.data.getString("msg2")
                }
            }
        }
    }


    class MyServiceConnection(context: Context) : ServiceConnection {
        private val mContext: MainActivity = context as MainActivity

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val messenger = Messenger(service)
            val message = Message.obtain(null, 1)
            val bundle = Bundle()
            mContext.btn.setOnClickListener {
                val content = mContext.editText.editableText.toString()
                bundle.putString("msg", content)
                message.data = bundle
                //必不可少
                message.replyTo = mContext.messenger
                messenger.send(message)
            }
        }

    }

    private val connection = MyServiceConnection(this)
    lateinit var editText: EditText
    lateinit var btn: Button
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.editText)
        btn = findViewById(R.id.btn)
        textView = findViewById(R.id.textView)
        val intent = Intent(this, MyServices::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(connection)
        super.onDestroy()
    }
}
