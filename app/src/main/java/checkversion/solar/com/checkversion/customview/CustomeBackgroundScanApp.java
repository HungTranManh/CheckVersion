package checkversion.solar.com.checkversion.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import checkversion.solar.com.checkversion.itemdata.CircleScan;

public class CustomeBackgroundScanApp extends View {
    private List<CircleScan> circleScans;
    private int radius;
    private Paint paint;
    private int x, y;
    private int radiusEnd;
    private static final String BLUE1 = "#4aabec", BLUE2 = "#81D4FA";
    private static final int space = 150;
    private int index = 15;
    private Handler handler = new Handler();

    public CustomeBackgroundScanApp(Context context) {
        super(context);
        setUp();
    }

    public CustomeBackgroundScanApp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public CustomeBackgroundScanApp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        x = getWidth() / 2;
        y = getHeight() / 2;
        int max = x > y ? x : y;
        if (circleScans.size() == 0) {
            int indexCircle = 0;
            radius = max*2;
            while (radius - space >=0) {
                radius -=space;
                CircleScan scan = new CircleScan();
                scan.setRadius(radius);
                if (indexCircle % 2 == 0) {
                    scan.setColor(Color.parseColor(BLUE1));
                } else {
                    scan.setColor(Color.parseColor(BLUE2));
                }
                circleScans.add(scan);
                indexCircle++;
            }
            radiusEnd=circleScans.get(circleScans.size()-2).getRadius();
        }
        if (circleScans.get(0).getRadius() >= max*2) {
            circleScans.remove(circleScans.get(0));
        }
        if (circleScans.get(circleScans.size()-1).getRadius()-radiusEnd==0) {
            radius = radiusEnd-space;
            CircleScan scan = new CircleScan();
            scan.setRadius(radius);
            if (circleScans.get(circleScans.size() - 1).getColor() == Color.parseColor(BLUE1)) {
                scan.setColor(Color.parseColor(BLUE2));
            }
            else {
                scan.setColor(Color.parseColor(BLUE1));

            }
            circleScans.add(scan);
        }
        for (int i = 0; i < circleScans.size(); i++) {
            paint.setColor(circleScans.get(i).getColor());
            canvas.drawCircle(x, y, circleScans.get(i).getRadius(), paint);
            int radiusCurrent = circleScans.get(i).getRadius() + space/30;
            circleScans.get(i).setRadius(radiusCurrent);
        }
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            invalidate();
            handler.postDelayed(run, 200);
        }
    };

    public void startAnimation() {
        handler.removeCallbacks(run);
        handler.post(run);
    }

    public void stopAnimation() {
        handler.removeCallbacks(run);
    }

    public void setUp() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(50);
        paint.setStyle(Paint.Style.FILL);
        circleScans = new ArrayList<>();
    }
}
