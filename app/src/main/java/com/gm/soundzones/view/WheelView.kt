package com.gm.soundzones.view

import android.content.Context
import android.graphics.*
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gm.soundzones.R
import com.gm.soundzones.log


class WheelView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val wheelBitmap by lazy {
        BitmapFactory.decodeResource(resources, R.mipmap.whl)
    }
    private val triggerMessage:Message
        get(){
            val obtain:Message = Message.obtain(handler, {
                onChange?.let { it(currentProcentage) }
//                log("precentage $currentProcentage")
            })
            obtain.what= WHAT
            return obtain;
        }

    private val paint:Paint
    private val imagePaint=Paint(Paint.ANTI_ALIAS_FLAG)
    private val currentPosition:PointF = PointF()

    private val imageRect:Rect
    private val viewRect:RectF = RectF()
    private var drawAngle =0.0
    private var currentProcentage=0.0
    private var startAngle =0.0
//    private var velocityTracker:VelocityTracker?=null
//    private var animator:Animator?=null

    companion object {
        private const val WHAT=123
        private const val TRIGGER_DELAY=100L
        private const val TAU = Math.PI*2
        private const val STOP_TIME=500
        private const val MIN_PERCENTAGE=0
        const val MAX_PERCENTAGE=600
        private const val DISABLE_ALPHA=155
        private const val ENABLE_ALPHA=255
    }

    var onChange:((percent:Double)->Unit)?= null

    init {
        paint=Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color=Color.rgb(170,255,255)
        paint.textSize=100f
        imageRect=Rect(0,0,wheelBitmap.width,wheelBitmap.height)
        isClickable = true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val size = when{
            widthMode == View.MeasureSpec.EXACTLY && widthSize > 0 -> widthSize
            heightMode == View.MeasureSpec.EXACTLY && heightSize > 0 ->heightSize
            else -> if (widthSize < heightSize) widthSize else heightSize
        }

        val finalMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
        viewRect.bottom=size.toFloat()
        viewRect.right=size.toFloat()
        super.onMeasure(finalMeasureSpec, finalMeasureSpec)
    }

    fun setPosition(percentage:Double){
        log("wheel Position $percentage")
        startAngle=0.0
        drawAngle=percentage/100* TAU
        currentProcentage=percentage
        invalidate()
    }

    override fun setEnabled(enabled: Boolean) {
        imagePaint.alpha=if(enabled) ENABLE_ALPHA else DISABLE_ALPHA
        super.setEnabled(enabled)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        animator?.cancel()
        when(event?.takeIf { isEnabled }?.actionMasked){
            MotionEvent.ACTION_DOWN->{
//                velocityTracker= VelocityTracker.obtain();
                currentPosition.x=event.x
                currentPosition.y=event.y
                startAngle = calculateAngle(currentPosition)
//                calcBorderPosition(startAngle)
//                val event1 = MotionEvent.obtain(event.downTime, event.eventTime, event.action, borderPosition.x, borderPosition.y, event.metaState)
//                velocityTracker?.addMovement(event1)
//                event1.recycle()
//                return true
            }
            MotionEvent.ACTION_MOVE->{

                currentPosition.x=event.x
                currentPosition.y=event.y
                calcPosition(calculateAngle(currentPosition))
//                calcBorderPosition(drawAngle)
//                val event1 = MotionEvent.obtain(event.downTime, event.eventTime, event.action, borderPosition.x, borderPosition.y, event.metaState)
//                event.offsetLocation(borderPosition.x-event.x,borderPosition.y-event.y)
//                velocityTracker?.addMovement(event)
//                event1.recycle()
//                return true
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                startAngle=drawAngle
//                velocityTracker?.let {
//                    it.computeCurrentVelocity(80)
//                    animator?.cancel()
//                    animator=createAccAnimator(it.xVelocity,it.yVelocity)
//                    Log.d("dada","x velocity ${it.xVelocity} y velocity ${it.yVelocity}")
//                    it.clear()
//                    it.recycle()
//                }
//                velocityTracker=null
//                return true
            }

        }
        return super.onTouchEvent(event)
    }
    //    val borderPosition=PointF()
//    private fun calcBorderPosition(angle: Double){
//        val dy = Math.sin(angle)*viewRect.height()/2
//        val dx = Math.cos(angle)*viewRect.width()/2
//        borderPosition.x= (viewRect.centerX()+dx).toFloat()
//        borderPosition.y= (viewRect.centerY()+dy).toFloat()
//    }
    private fun calcPosition(angle:Double){
        drawAngle +=angle- startAngle
        var currentAngle=normalizeAngle(angle)
        startAngle =normalizeAngle(startAngle)
        if(Math.abs(currentAngle- startAngle)>Math.PI){
            if(currentAngle> startAngle) startAngle += TAU else currentAngle+= TAU
        }
        currentProcentage+=((currentAngle- startAngle)/ TAU)*100
        if(currentProcentage> MAX_PERCENTAGE){
            currentProcentage= MAX_PERCENTAGE.toDouble()
            drawAngle=currentProcentage/100* TAU
        }else if(currentProcentage< MIN_PERCENTAGE){
            currentProcentage= MIN_PERCENTAGE.toDouble()
            drawAngle=currentProcentage/100* TAU
        }
        startAngle =currentAngle
        handler?.takeUnless { it.hasMessages(WHAT) }?.sendMessageDelayed(triggerMessage, TRIGGER_DELAY)
        invalidate()
    }

    //    private fun createAccAnimator(toX:Float,toY:Float):Animator{
//        val valueAnimator = ValueAnimator.ofInt(0, STOP_TIME)
//        var div=0
//        valueAnimator.duration= STOP_TIME.toLong()
//        valueAnimator.addUpdateListener {
//            val value=it.animatedValue as Int
//            div=value-div
//            currentPosition.x+=toX*div/STOP_TIME
//            currentPosition.y+=toY*div/STOP_TIME
//            calcPosition()
//            div=value
//        }
//        valueAnimator.interpolator=DecelerateInterpolator()
//        valueAnimator.start()
//        return valueAnimator
//    }
    private fun normalizeAngle(angle:Double)=(angle+ TAU)% TAU

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            save()
            rotate(Math.toDegrees(drawAngle).toFloat(),viewRect.centerX(),viewRect.centerY())
            drawBitmap(wheelBitmap,imageRect,viewRect,imagePaint)
            restore()
//            drawPoint(borderPosition.x,borderPosition.y,paint)
        }

        super.onDraw(canvas)
    }
    private fun calculateAngle(currentPosition:PointF):Double{
        var radiant=Math.atan(((currentPosition.y - viewRect.centerY()) / (currentPosition.x - viewRect.centerX())).toDouble())
        radiant += if(currentPosition.x<viewRect.centerX()) Math.PI else 0.0
        return radiant
    }

}