package checkversion.solar.com.checkversion.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.logging.Handler;

import checkversion.solar.com.checkversion.R;

public class CustomRadarView extends View {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private int defaultWidth;
    private int defaultHeight;
    private int start;
    private int centerX;
    private int centerY;
    private int radarRadius;
    private int circleColor = Color.parseColor("#12ffffff");
    private int radarColor = Color.parseColor("#99EEEEEE");
    private int tailColor = Color.parseColor("#20aaaaaa");

    private Paint mPaintCircle;
    private Paint mPaintRadar;
    private Matrix matrix;

    private android.os.Handler handler=new android.os.Handler();
    private Runnable run = new Runnable()
    {
        @Override
        public void run()
        {
            start += 2;
            matrix = new Matrix();
            matrix.postRotate(start, centerX, centerY);
            postInvalidate();
            handler.postDelayed(run, 10);
        }
    };
    public void startAnimation() {
        handler.removeCallbacks(run);
        handler.post(run);
    }

    public void stopAnimation() {
        handler.removeCallbacks(run);
    }
    public CustomRadarView(Context context) {

        super(context);
    }

    public CustomRadarView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        init(attrs,context);
    }

    public CustomRadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radarRadius = Math.min(w, h);
    }
    private void init(AttributeSet attrs, Context context)
    {
        if (attrs != null)
        {
            TypedArray ta = context.obtainStyledAttributes(attrs,
                    R.styleable.RadarScanView);
            circleColor = ta.getColor(R.styleable.RadarScanView_circleColor, circleColor);
            radarColor = ta.getColor(R.styleable.RadarScanView_radarColor, radarColor);
            tailColor = ta.getColor(R.styleable.RadarScanView_tailColor, tailColor);
            ta.recycle();
        }
        initPaint();
        defaultWidth = dip2px(context, DEFAULT_WIDTH);
        defaultHeight = dip2px(context, DEFAULT_HEIGHT);

        matrix = new Matrix();
        handler.post(run);
    }

    private void initPaint()
    {
        mPaintCircle = new Paint();
        mPaintCircle.setColor(circleColor);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(2);

        mPaintRadar = new Paint();
        mPaintRadar.setColor(radarColor);
        mPaintRadar.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, radarRadius / 7, mPaintCircle);
        canvas.drawCircle(centerX, centerY, radarRadius / 4, mPaintCircle);
        canvas.drawCircle(centerX, centerY, radarRadius / 3, mPaintCircle);
        canvas.drawCircle(centerX, centerY, 3 * radarRadius / 7, mPaintCircle);

        Shader shader = new SweepGradient(centerX, centerY, Color.parseColor("#00A8D7A7"),
                Color.parseColor("#ffEEEEEE"));
        mPaintRadar.setShader(shader);
        canvas.concat(matrix);
        canvas.drawCircle(centerX, centerY, 3 * radarRadius / 7, mPaintRadar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int resultWidth = 0;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY)
        {
            resultWidth = sizeWidth;
        }
        else
        {
            resultWidth = defaultWidth;
            if (modeWidth == MeasureSpec.AT_MOST)
            {
                resultWidth = Math.min(resultWidth, sizeWidth);
            }
        }

        int resultHeight = 0;
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeHeight == MeasureSpec.EXACTLY)
        {
            resultHeight = sizeHeight;
        }
        else
        {
            resultHeight = defaultHeight;
            if (modeHeight == MeasureSpec.AT_MOST)
            {
                resultHeight = Math.min(resultHeight, sizeHeight);
            }
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }
    private int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    private int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
