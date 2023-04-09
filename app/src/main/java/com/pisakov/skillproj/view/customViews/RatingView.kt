package com.pisakov.skillproj.view.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.pisakov.skillproj.R
import kotlin.math.min

class RatingView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : View(context, attributeSet) {

    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var rating: Float = 1.0f

    private var stroke = 10f
    private var scaleSize = 60f

    private val oval = RectF()

    private lateinit var backgroundPaint: Paint
    private lateinit var ratingPaint: Paint
    private lateinit var progressPaint: Paint

    init {
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingView, 0, 0)
        try {
            stroke = a.getFloat(R.styleable.RatingView_stroke, stroke)
            rating = a.getFloat(R.styleable.RatingView_rating, rating)
        } finally {
            a.recycle()
        }
        initPaint()
    }

    private fun initPaint() {
        backgroundPaint = Paint().apply {
            color = Color.parseColor("#444444")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        ratingPaint = Paint().apply {
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(rating)
            isAntiAlias = true
        }
        progressPaint = Paint().apply {
            strokeWidth = stroke
            color = getPaintColor(rating)
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
    }

    fun setProgress(rt: Float) {
        rating = rt
        initPaint()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> 300
        }

    override fun onDraw(canvas: Canvas) {
        drawRating(canvas)
        drawText(canvas)
    }

    private fun getPaintColor(rating: Float): Int = when((rating * 10).toInt()) {
        in 0 .. 25 -> Color.parseColor("#e84258")
        in 26 .. 50 -> Color.parseColor("#fd8060")
        in 51 .. 75 -> Color.parseColor("#fee191")
        100 -> Color.parseColor("#00ff00")
        else -> Color.parseColor("#b0d8a4")
    }

    private fun drawRating(canvas: Canvas) {
        val scale = radius * 0.8f
        canvas.save()
        canvas.translate(centerX, centerY)
        oval.set(0f - scale, 0f - scale, scale , scale)
        canvas.drawCircle(0f, 0f, radius, backgroundPaint)
        canvas.drawArc(oval, -90f,rating * 36f, false, progressPaint)
        canvas.restore()
    }

    private fun drawText(canvas: Canvas) {
        val message = String.format("%.1f", rating)
        val widths = FloatArray(message.length)
        ratingPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        canvas.drawText(message, centerX - advance / 2, centerY  + advance / 4, ratingPaint)
    }
}