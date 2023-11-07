package com.example.fingerhero

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF

class Circle(var posX : Float, var posY : Float,
             var paintColor : Int = Color.GRAY,
             var radius : Float = 80.0f){

    val paint = Paint()
    var position = PointF(posX, posY)

    init {
        paint.color = paintColor
    }

    fun updateColor(newColor : Int)
    {
        paint.color = newColor
    }

}