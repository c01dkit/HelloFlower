package com.example.helloflower_kotlin

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.math.BigInteger
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class AliMqtt(PRODUCTKEY:String, DEVICENAME:String, DEVICESECRET:String, applicationContext: Context) {
    private val mqttTAG = "AiotMqtt"
//    "a1YFxxHqbhJ"
//    "c01dkit"
//    "1078a7226ae0ce5a657a484605b88a07"

    /* 自动Topic, 用于上报消息 */
    private val PUB_TOPIC = "/$PRODUCTKEY/$DEVICENAME/user/update"

    /* 阿里云Mqtt服务器域名 */
    val host = "tcp://$PRODUCTKEY.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883"
    private var clientId: String? = null
    private var userName: String? = null
    private var passWord: String? = null
    var mqttAndroidClient: MqttAndroidClient? = null

    /**
     * MQTT建连选项类，输入设备三元组productKey, deviceName和deviceSecret, 生成Mqtt建连参数clientId，username和password.
     */
    internal class AiotMqttOption {
        var username = ""
            private set
        var password = ""
            private set
        var clientId = ""
            private set

        /**
         * 获取Mqtt建连选项对象
         * @param productKey 产品秘钥
         * @param deviceName 设备名称
         * @param deviceSecret 设备机密
         * @return AiotMqttOption对象或者NULL
         */
        fun getMqttOption(
            productKey: String?,
            deviceName: String?,
            deviceSecret: String?
        ): AiotMqttOption? {
            if (productKey == null || deviceName == null || deviceSecret == null) {
                return null
            }
            try {
                val timestamp =
                    java.lang.Long.toString(System.currentTimeMillis())

                // clientId
                clientId = productKey + "." + deviceName + "|timestamp=" + timestamp +
                        ",_v=paho-android-1.0.0,securemode=2,signmethod=hmacsha256|"

                // userName
                username = "$deviceName&$productKey"

                // password
                val macSrc =
                    "clientId" + productKey + "." + deviceName + "deviceName" +
                            deviceName + "productKey" + productKey + "timestamp" + timestamp
                val algorithm = "HmacSHA256"
                val mac: Mac = Mac.getInstance(algorithm)
                val secretKeySpec =
                    SecretKeySpec(deviceSecret.toByteArray(), algorithm)
                mac.init(secretKeySpec)
                val macRes: ByteArray = mac.doFinal(macSrc.toByteArray())
                password = java.lang.String.format("%064x", BigInteger(1, macRes))
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return this
        }
    }

    /**
     * 订阅特定的主题
     * @param topic mqtt主题
     */
    fun subscribeTopic(topic: String?) {
        try {
            mqttAndroidClient!!.subscribe(topic, 0, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i(mqttTAG, "subscribed success")
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    Log.i(mqttTAG, "subscribed failed")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    /**
     * 向默认的主题/user/update发布消息
     * @param payload 消息载荷
     */
    fun publishMessage(payload: String) {
        try {
            if (mqttAndroidClient!!.isConnected == false) {
                mqttAndroidClient!!.connect()
            }
            val message = MqttMessage()
            message.payload = payload.toByteArray()
            message.qos = 0
            mqttAndroidClient!!.publish(PUB_TOPIC, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i(mqttTAG,  "publish succeed!")
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    Log.i(mqttTAG,  "publish failed!")
                }
            })
        } catch (e: MqttException) {
            Log.e(mqttTAG,  e.toString())
            e.printStackTrace()
        }
    }

    init {
        val aiotMqttOption = AiotMqttOption().getMqttOption(PRODUCTKEY, DEVICENAME, DEVICESECRET)
        if (aiotMqttOption == null) {
            Log.e(mqttTAG, "device info error")
        } else {
            clientId = aiotMqttOption.clientId
            userName = aiotMqttOption.username
            passWord = aiotMqttOption.password
        }

        /* 创建MqttConnectOptions对象，并配置username和password。 */
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.userName = userName
        mqttConnectOptions.password = passWord!!.toCharArray()


        /* 创建MqttAndroidClient对象，并设置回调接口。 */
        mqttAndroidClient = MqttAndroidClient(applicationContext, host, clientId)
        mqttAndroidClient!!.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                Log.i(mqttTAG, "connection lost")
            }

            @Throws(java.lang.Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                Log.i(mqttTAG,"topic: " + topic + ", msg: " + String(message.payload))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                Log.i(mqttTAG, "msg delivered")
            }
        })

        /* 建立MQTT连接。 */
        try {
            mqttAndroidClient!!.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i(mqttTAG, "connect succeed")
                }
                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    Log.i(mqttTAG, "connect failed")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}

