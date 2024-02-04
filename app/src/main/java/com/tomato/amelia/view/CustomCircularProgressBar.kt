package com.tomato.amelia.view

/**
 * author: created by tomato on 2024/2/1 16:09
 * description:
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable

class CustomCircularProgressBar @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progressBarWidth: Float = 20f
    private var progressColor: Int = android.graphics.Color.BLUE
    private var trackColor: Int = android.graphics.Color.GRAY
    private var progress: Float = 0f
    private var max: Float = 100f

    private val rectF = RectF()
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val foregroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        // Customize initial properties, could be modified later
        backgroundPaint.color = trackColor
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = progressBarWidth

        foregroundPaint.color = progressColor
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = progressBarWidth
    }

    fun setProgressBarWidth(width: Float) {
        progressBarWidth = width
        backgroundPaint.strokeWidth = width
        foregroundPaint.strokeWidth = width
        invalidate()
    }

    fun setProgressColor(color: Int) {
        progressColor = color
        foregroundPaint.color = color
        invalidate()
    }

    fun setTrackColor(color: Int) {
        trackColor = color
        backgroundPaint.color = color
        invalidate()
    }

    fun setProgress(newProgress: Float) {
        progress = newProgress
        invalidate()
    }

    fun setMax(newMax: Float) {
        max = newMax
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        foregroundPaint.strokeCap= Paint.Cap.ROUND
        // Draw the background track
        canvas.drawOval(rectF, backgroundPaint)

        // Calculate the angle to draw from based on the current progress
        val angle = 360 * progress / max

        // Draw the progress on top of the background track
        canvas.drawArc(rectF, -90f, angle, false, foregroundPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = Math.min(width, height)

        // Ensure this view is always square (aspect ratio 1:1)
        setMeasuredDimension(min, min)

        // Define the position and size for the progress bar
        rectF.set(
            0 + progressBarWidth / 2,
            0 + progressBarWidth / 2,
            min - progressBarWidth / 2,
            min - progressBarWidth / 2
        )
    }
}