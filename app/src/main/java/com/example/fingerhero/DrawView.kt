package com.example.fingerhero

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.sqrt

/*
View customizada para desenho personalizado
 */


class DrawView : View, View.OnTouchListener {
    val circleRadius = 70.0f

    val colors =
        arrayOf(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN)

    // Especifica qual circulo esta ativo
    var circleEnabled = Array<Boolean>(5){false}
    var positionCircle = Array<PointF>(5){ PointF(0.0f, 0.0f) }

    //objetos
    lateinit var inst1 : Circle

    init {

    }

    constructor(context : Context) : super(context) {
        // Setar o listener do evento de touch
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.strokeWidth = 7.0f

        //fazendo um for com o while para desenhar o multi-clique
        var index = 0
        while (index < circleEnabled.size){
            if (circleEnabled[index]){
                paint.color = colors[index]
                val position = positionCircle[index]
                canvas?.drawCircle(position.x, position.y, circleRadius, paint)
            }

            index++
        }

        //conseguindo o centro
        val centerX = (width / 2.0f)
        val centerY = (height / 2.0f)

        //definindo objetos lateinit
        var inst1 = Circle(centerX,centerY)

        //desenhando objetos
        canvas.drawCircle(inst1.posX, inst1.posY, inst1.radius, inst1.paint)

        //colisão de todos os toques contra o inst1
        for (position in positionCircle)
        {
            if(circleCollision(inst1.position, position, inst1.radius, circleRadius)){
                inst1.updateColor(Color.RED)
                canvas.drawCircle(inst1.posX, inst1.posY, inst1.radius, inst1.paint)
            }
        }

    }

    // Implementacao do comportamento ao gerar o evento de touch
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

        var pointerIndex = motionEvent?.actionIndex ?: -1
        var pointerId = motionEvent?.getPointerId(pointerIndex) ?: -1

        when (motionEvent?.actionMasked){

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                circleEnabled[pointerId] = false
                positionCircle[pointerId] = PointF(-200f,-200f)

                invalidate()
            }

            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {

                if (pointerId < circleEnabled.size){
                    circleEnabled[pointerId] = true
                    positionCircle[pointerId].x = motionEvent.getX(pointerIndex)
                    positionCircle[pointerId].y = motionEvent.getY(pointerIndex)
                }
                Log.i("Pointers", "Pointer Index: $pointerIndex")
                Log.i("Pointers", "Pointer ID: $pointerId")

                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {

                var index = 0
                while (index < motionEvent.pointerCount){

                    if (index < circleEnabled.size) {
                        var _pointId = motionEvent.getPointerId(index)
                        positionCircle[_pointId].x = motionEvent.getX(index)
                        positionCircle[_pointId].y = motionEvent.getY(index)
                    }

                    index++
                }

                invalidate()
            }
        }
        return true
    }

    //Funções de Colisão
    private fun circleCollision(position1 : PointF, position2 : PointF,
                                radius1 : Float, radius2 : Float,
                                tolerance : Float = 0f) : Boolean {

        var distance : Float
        var result : Float

        var dif1 = (position2.x - position1.x)
        var dif2 = (position2.y - position1.y)

        result = (dif1 * dif1) + (dif2 * dif2)

        Log.i("collision", "$result")

        distance = sqrt(result)

        Log.i("collision", "distancia: $distance")


        var collision = false

        var index = 0
        while (index < circleEnabled.size){
                collision = when {
                    distance <= (radius1 + radius2 + tolerance)-> {
                        true
                    }

                    else ->{
                        false
                    }
                }

            index++
        }

        return collision
    }


}