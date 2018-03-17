package com.hujlin.messengerDemo

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import java.util.*

/**
 * Created by hujlin on 2018/3/17 08:38 .
 */
class MyServices : Service() {

    companion object {
        val TAG = "MessengerService"
        val What = 1
    }

    private val messenger = Messenger(MessengerHandler())

    override fun onBind(intent: Intent?): IBinder {
        return messenger.binder
    }

    class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                What -> {
                    //接收数据
                    Log.e(TAG, "receive message")
                    Log.e(TAG, msg.data.getString("msg"))
                    //发送数据
                    val messenger: Messenger = msg.replyTo
                    val message = Message.obtain(null, 2)
                    val bundle = Bundle()
                    bundle.putString("msg2", "i am from server:" + Random().nextInt(100))
                    message.data = bundle
                    messenger.send(message)
                }
            }
            super.handleMessage(msg)
        }
    }
}