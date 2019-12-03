package com.easymi.component.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import android.view.WindowManager
import com.amap.api.maps.model.Marker

/**
 * Created by yinxin on 2018/3/7.
 * 方向传感器帮助.
 */
class SensorEventHelper constructor(private val context: Context) : SensorEventListener {

    private var mSensorManager: SensorManager? =
            context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private var lastTime: Long = 0
    private val TIME_SENSOR = 50

    private var mAngle: Float = 0f
    private var mSensor: Sensor?
    var marker: Marker? = null

    init {
        mSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    }

    fun registerSensorListener() {
        mSensorManager?.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unRegisterSensorListener() {
        mSensorManager?.unregisterListener(this, mSensor)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //do nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return
        }

        when (event.sensor.type) {
            Sensor.TYPE_ORIENTATION -> {
                var x = event.values[0]
                x += getScreenRotationOnPhone(context).toFloat()
                x %= 360.0f
                if (x > 180.0f)
                    x -= 360.0f
                else if (x < -180.0f)
                    x += 360.0f

                if (Math.abs(mAngle - x) < 3.0f) {
                    return
                }

                mAngle = if (java.lang.Float.isNaN(x)) 0f else x
                marker?.rotateAngle = 360 - mAngle
                lastTime = System.currentTimeMillis()
            }
        }

    }


    /**
     * 获取当前屏幕旋转角度
     *
     * @param context context
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    private fun getScreenRotationOnPhone(context: Context): Int {
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        return when (display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> -90
            else -> 0
        }
    }

}