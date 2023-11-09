package com.example.fingerhero

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.sqrt
import kotlin.random.Random

/*
Finger Hero
 */


class DrawView : View, View.OnTouchListener {
    val circleRadius = 30.0f
    val notesRadius = 50.0f

    val colors =
        arrayOf(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW)

    var points : Int = 0

    // Especifica qual circulo esta ativo
    var circleEnabled = Array<Boolean>(4){false}
    var positionCircle = Array<PointF>(4){ PointF(0.0f, 0.0f) }

    //objetos
    var inst1 : Circle
    var inst2 : Circle
    var inst3 : Circle
    var inst4 : Circle

    lateinit var listInst : Array<Circle>
    lateinit var listNotes : MutableList<Circle>

    init {
        //definindo objetos
        inst1 = Circle(250.0f,90.0f)
        inst2 = Circle(200.0f,250.0f )
        inst3 = Circle(200.0f,420.0f)
        inst4= Circle(250.0f,580.0f)

        listInst = arrayOf(inst1, inst2, inst3, inst4)

        //notas
        listNotes = arrayListOf<Circle>()
        var numCols = 35

        var i = 0
        while (i < numCols){
            var random = Random.nextInt(0, 100).toFloat()
            if(random <= 40){
                var _circle = Circle(1000f + i * (2 * notesRadius) + (i * 40f),
                    inst1.posY,
                    Color.GRAY,
                    notesRadius)
                listNotes.add(_circle)
            }

            Log.i("lista","tamanhoLista: ${listNotes.size}")
            Log.i("Random","random: $random")

            i++
        }
        i=0
        while (i < numCols) {
            var random = Random.nextInt(0, 100).toFloat()
            if (random <= 40) {
                var _circle = Circle(
                    1000f + i * (2 * notesRadius) + (i * 40f),
                    inst2.posY,
                    Color.GRAY,
                    notesRadius
                )
                listNotes.add(_circle)
            }
            i++
        }
        i=0
        while (i < numCols) {
            var random = Random.nextInt(0, 100).toFloat()
            if (random <= 40) {
                var _circle = Circle(
                    1000f + i * (2 * notesRadius) + (i * 40f),
                    inst3.posY,
                    Color.GRAY,
                    notesRadius
                )
                listNotes.add(_circle)
            }
            i++
        }
        i=0
        while (i < numCols) {
            var random = Random.nextInt(0, 100).toFloat()
            if (random <= 40) {
                var _circle = Circle(
                    1000f + i * (2 * notesRadius) + (i * 40f),
                    inst4.posY,
                    Color.GRAY,
                    notesRadius
                )
                listNotes.add(_circle)
            }
            i++
        }

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
                canvas.drawCircle(position.x, position.y, circleRadius, paint)
            }

            index++
        }

        //conseguindo o centro (p/ utilidades)
        val centerX = (width / 2.0f)
        val centerY = (height / 2.0f)

        //movimento do listNotes
        for (note in listNotes){
            note.updatePosition(-3f)
        }

        //desenhando Notas
        for(note in listNotes){
            canvas.drawCircle(note.posX, note.posY, note.radius, note.paint)
        }

        //desenhando Instrumentos
        for (inst in listInst) {
            canvas.drawCircle(inst.posX, inst.posY, inst.radius, inst.paint)
        }

        //colisão de todos os toques contra TODOS os Instrumentos
        for (inst in listInst){
            for (position in positionCircle) {
                if(circleCollision(inst.position, position, inst.radius, circleRadius)){
                    when (inst) {
                        inst1 -> inst.updateColor(Color.GREEN)
                        inst2 -> inst.updateColor(Color.RED)
                        inst3 -> inst.updateColor(Color.YELLOW)
                        inst4 -> inst.updateColor(Color.BLUE)
                    }
                    canvas.drawCircle(inst.posX, inst.posY, inst.radius, inst.paint)
                }
            }
        }

        //colisão dos toques contra as notas (condição de estar na colisão do instrumento)
        for (inst in listInst){
            for (note in listNotes){
                for (position in positionCircle) {
                    if(circleCollision(note.position, position,
                            notesRadius, circleRadius)
                        &&
                        (circleCollision(note.position, inst.position,
                            notesRadius, inst.radius,
                            -40f))
                        ){
                        note.updateColor(Color.BLUE)
                        points += 50
                        Log.i("TESTE","COLIDIU e pontos= $points")
                        canvas.drawCircle(note.posX, note.posY, note.radius, note.paint)
                    }
                }
            }
        }

        //limpa os botões par cinza novamente
        for (inst in listInst){
            inst.updateColor(Color.GRAY)
        }

        invalidate()
    }

    // Implementacao do comportamento ao gerar o evento de touch
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

        var pointerIndex = motionEvent?.actionIndex ?: -1
        var pointerId = motionEvent?.getPointerId(pointerIndex) ?: -1

        //teste que ajudou a resolver o crash de quando se usava mais dedos do que o tamanho de circleEnabled
        Log.i("Exception", "${circleEnabled.size}")
        Log.i("Exception", "${pointerId}")

        when (motionEvent?.actionMasked){

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if(pointerId < circleEnabled.size){
                    circleEnabled[pointerId] = false
                    positionCircle[pointerId] = PointF(-200f,-200f)
                }

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
                while (index < motionEvent.pointerCount && index < circleEnabled.size){

                    if (index < circleEnabled.size) {
                        var _pointId = motionEvent.getPointerId(index)
                        if(_pointId < circleEnabled.size) {
                            positionCircle[_pointId].x = motionEvent.getX(index)
                            positionCircle[_pointId].y = motionEvent.getY(index)
                        }
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