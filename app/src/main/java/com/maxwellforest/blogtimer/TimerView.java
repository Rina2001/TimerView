package com.maxwellforest.blogtimer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.sampletimer.R;

import java.util.concurrent.TimeUnit;

/**
 * Countdown timer view.
 *
 */
public class TimerView extends View {

    private static final int ARC_START_ANGLE = 270; // 12 o'clock

    private static final float THICKNESS_SCALE = 0.08f;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private RectF mCircleOuterBounds;
    private RectF mCircleInnerBounds;

    private Paint mCirclePaint;
    private Paint textPainter,subTextPainter;

    private Paint fullBgPaint;

    private float mCircleSweepAngle;
    long second;



    private ValueAnimator mTimerAnimator;

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int circleColor = Color.RED;
        float fontSize = 15f;
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimerView);
            if (ta != null) {
                circleColor = ta.getColor(R.styleable.TimerView_circleColor, circleColor);
                fontSize = ta.getDimension(R.styleable.TimerView_fontSize,fontSize);
                ta.recycle();
            }
        }

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(8);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.GRAY);

        textPainter = new Paint();
        textPainter.setAntiAlias(true);
        textPainter.setTextSize(fontSize);
        textPainter.setColor(circleColor);

        subTextPainter = new Paint();
        subTextPainter.setAntiAlias(true);
        subTextPainter.setTextSize(fontSize-1);
        subTextPainter.setColor(circleColor);

        fullBgPaint = new Paint();
        fullBgPaint.setAntiAlias(true);
        fullBgPaint.setStyle(Paint.Style.STROKE);
        fullBgPaint.setStrokeWidth(8);
        fullBgPaint.setColor(circleColor);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // Trick to make the view square
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(Color.TRANSPARENT);
            mCanvas = new Canvas(mBitmap);
        }

        super.onSizeChanged(w, h, oldw, oldh);
        updateBounds();
    }

    @Override
    protected void onDraw(Canvas canvas) {
          mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        canvas.drawArc(mCircleInnerBounds, ARC_START_ANGLE, 360, false, fullBgPaint);

        mCanvas.drawArc(mCircleInnerBounds, ARC_START_ANGLE, mCircleSweepAngle, false, mCirclePaint);
        Log.d("TAG", canvas.getWidth()+" XY "+canvas.getHeight());
        if((""+second).length()==1){
            mCanvas.drawText(""+second,50,60, textPainter);
            mCanvas.drawText("វិនាទី",35,90,subTextPainter);
        }
        else{
            mCanvas.drawText(""+second,40,60, textPainter);
            mCanvas.drawText("វិនាទី",34,90,subTextPainter);
        }



        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    /**
     *
     * @param secs
     * @description as Second
     */
    public void start(int secs) {
        stop();

        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                second = animation.getCurrentPlayTime()/1000;
                float animationVal = (float) animation.getAnimatedValue();
                drawProgress(animationVal);
            }
        });
        mTimerAnimator.start();
    }

    public void stop() {
        if (mTimerAnimator != null && mTimerAnimator.isRunning()) {
            mTimerAnimator.cancel();
            mTimerAnimator = null;
            drawProgress(0f);
        }
    }

    private void drawProgress(float progress) {
        mCircleSweepAngle = 360 * ( progress);

        invalidate();
    }

    private void updateBounds() {
        final float thickness = getWidth() * THICKNESS_SCALE;

        mCircleOuterBounds = new RectF(0, 0, getWidth(), getHeight());
        mCircleInnerBounds = new RectF(
                mCircleOuterBounds.left + thickness,
                mCircleOuterBounds.top + thickness,
                mCircleOuterBounds.right - thickness,
                mCircleOuterBounds.bottom - thickness);

        invalidate();
    }
}
