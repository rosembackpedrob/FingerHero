package com.example.sensorball

import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlin.math.sqrt

class DrawView : View, SensorEventListener {
    lateinit var position : PointF
    lateinit var paint : Paint

    lateinit var positionFinal : PointF
    lateinit var paintFinal : Paint

    lateinit var positionBomb : PointF
    lateinit var paintBomb : Paint

    var xAccel = 0.0f
    var yAccel = 0.0f
    var zAccel = 0.0f

    final val radius = 50.0f
    final val radiusFinal = 100.0f
    final val radiusBomb = 60.0f


    constructor(context: Context) : super(context)

    init {
        position = PointF(0.0f, 0.0f)
        paint = Paint()
        paint.color = Color.RED

        positionFinal = PointF(0.0f,100.0f)
        paintFinal = Paint()
        paintFinal.color = Color.GREEN

        positionBomb = PointF(0f,0f)
        paintBomb = Paint()
        paintBomb.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        resetGame()

        val centerX = canvas.width / 2
        val centerY = canvas.height / 2

        position.x = centerX - xAccel * (canvas.width / 20.0f)
        position.y = centerY + yAccel * (canvas.height / 20.0f)

        positionBomb = PointF(centerX -0.0f, centerY - 0.0f)
        positionFinal.x = centerX - 0.0f

        canvas.drawCircle(position.x, position.y, radius, paint)

        canvas.drawCircle(positionFinal.x, positionFinal.y,radiusFinal, paintFinal)

        canvas.drawCircle(positionBomb.x, positionBomb.y,radiusBomb, paintBomb)

    }

    private fun update()
    {
        circleCollision(position, positionFinal, radius, radiusFinal, "VENCEU")
        circleCollision(position, positionBomb, radius, radiusBomb, "MORREU")
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            xAccel = event?.values?.get(0) ?: 0.0f
            yAccel = event?.values?.get(1) ?: 0.0f
            zAccel = event?.values?.get(2) ?: 0.0f
        }

        update()

        invalidate()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun circleCollision(position1 : PointF, position2 : PointF, radius1 : Float, radius2 : Float, message : String)
    {

        var distance : Float
        var result : Float

        var dif1 = (position2.x - position1.x)
        var dif2 = (position2.y - position1.y)

        result = (dif1 * dif1) + (dif2 * dif2)

        Log.i("collision", "$result")

        distance = sqrt(result)


        when {
            distance < (radius1 + radius2) -> {
                Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
                resetGame()
            }
        }
    }

    private fun resetGame(){
        position = PointF((width/ 2 + 0.0f), (height + 0.0f))
        
        positionFinal = PointF(0.0f,100.0f)

        positionBomb = PointF(0f,0f)

    }
}