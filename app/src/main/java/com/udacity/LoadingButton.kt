package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var buttonTextColor = 0
    private var buttonBackgroundColor = 0
    private var buttonLoadingColor = 0
    private var buttonCircleColor = 0

    private var loadingWidth = 0f
    private var circleAngle = 0f

    private var loadingAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Initial) { p, old, new ->
        when (new) {
            ButtonState.Initial -> handleInitialState()
            ButtonState.Loading -> handleLoadingState()
        }
    }

    private fun handleInitialState() {
        loadingWidth = 0f
        circleAngle = 0f
    }

    private fun handleLoadingState() {
        startLoadingAnimation()
        startCircleAnimation()
    }

    private fun startLoadingAnimation() {
        loadingAnimator.setFloatValues(0f, measuredWidth.toFloat())
        loadingAnimator.duration = 2000
        loadingAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator? ) {
                isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                isEnabled = true
                buttonState = ButtonState.Initial
            }
        })
        loadingAnimator.addUpdateListener { updatedAnimation ->
            loadingWidth = updatedAnimation.animatedValue as Float
            invalidate()
        }
        loadingAnimator.start()
    }

    private fun startCircleAnimation() {
        circleAnimator.setFloatValues(0f, 360f)
        circleAnimator.duration = 2000
        circleAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                buttonState = ButtonState.Initial
            }
        })
        circleAnimator.addUpdateListener { updatedAnimation ->
            circleAngle = updatedAnimation.animatedValue as Float
            invalidate()
        }
        circleAnimator.start()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonTextColor = getColor(R.styleable.LoadingButton_buttonTextColor, 0)
            buttonBackgroundColor = getColor(R.styleable.LoadingButton_buttonBackgroundColor, 0)
            buttonLoadingColor = getColor(R.styleable.LoadingButton_buttonLoadingColor, 0)
            buttonCircleColor = getColor(R.styleable.LoadingButton_buttonCircleColor, 0)
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Light color rectangle (background)
        paint.color = buttonBackgroundColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Rectangle loading progress
        paint.color = buttonLoadingColor
        canvas.drawRect(0f, 0f, loadingWidth, measuredHeight.toFloat(), paint)

        // Button text
        paint.color = buttonTextColor
        canvas.drawText(context.getString(buttonState.buttonText), (width/2).toFloat(), (height/2 + 10).toFloat(), paint)

        // Circle loading progress ("pac man")
        paint.color = buttonCircleColor
        canvas.drawArc(measuredWidth - 200f,
                       measuredHeight / 2 - 15f,
                       measuredWidth - 170f,
                       measuredHeight / 2 + 15f,
                       0f,
                       circleAngle,
                       true,
                       paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()
        buttonState = ButtonState.Loading

        invalidate()
        return true
    }
}