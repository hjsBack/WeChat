package cn.csu.software.wechat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import cn.csu.software.wechat.R;

/**
 * 气泡布局
 *
 * @author 来自网络
 * @since 2019-10-21
 */
public class BubbleLayout extends RelativeLayout {
    private static final int LEFT = 1;

    private static final int TOP = 2;

    private static final int RIGHT = 3;

    private static final int BOTTOM = 4;

    private static final int HALF = 2;

    /**
     * 当输入错误时，编译器给出错误提示
     *
     * @author 来自网络
     * @since 2019-10-21
     */
    @IntDef({LEFT, TOP, RIGHT, BOTTOM})
    private @interface Direction {
    }

    /**
     * 圆角大小
     */
    private int mRadius;

    /**
     * 三角形的方向
     */
    @Direction
    private int mDirection;

    /**
     * 三角形的底边中心点
     */
    private Point mDatumPoint;

    /**
     * 三角形位置偏移量(默认居中)
     */
    private int mOffset;

    private Paint mBorderPaint;

    private Path mPath;

    private RectF mRect;

    /**
     * 有参构造函数
     *
     * @param context Context
     * @param attrs AttributeSet
     */
    public BubbleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.BubbleLayout_radius, 0);
        // 三角形方向
        mDirection = typedArray.getInt(R.styleable.BubbleLayout_direction, BOTTOM);
        mOffset = typedArray.getDimensionPixelOffset(R.styleable.BubbleLayout_offset, 0);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        // 背景颜色
        int backGroundColor = typedArray.getColor(R.styleable.BubbleLayout_background_color, Color.WHITE);
        mBorderPaint.setColor(backGroundColor);
        int defShadowSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
            0, getResources().getDisplayMetrics());
        // 阴影尺寸
        int shadowSize = typedArray.getDimensionPixelSize(R.styleable.BubbleLayout_shadow_size, defShadowSize);
        // 阴影颜色
        int shadowColor = typedArray.getColor(R.styleable.BubbleLayout_shadow_color,
            Color.parseColor("#999999"));
        mBorderPaint.setShadowLayer(shadowSize, 0, 0, shadowColor);
        typedArray.recycle();

        mPath = new Path();
        mRect = new RectF();
        mDatumPoint = new Point();

        setWillNotDraw(false);
        // 关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDatumPoint.x <= 0 && mDatumPoint.y <= 0) {
            return;
        }
        switch (mDirection) {
            case LEFT:
                drawLeftTriangle(canvas);
                break;
            case TOP:
                drawTopTriangle(canvas);
                break;
            case RIGHT:
                drawRightTriangle(canvas);
                break;
            case BOTTOM:
                drawBottomTriangle(canvas);
                break;
            default:
                break;
        }
    }

    private void drawLeftTriangle(Canvas canvas) {
        int triangularLength = getPaddingLeft();
        if (triangularLength == 0) {
            return;
        }

        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CCW);
        mPath.moveTo(mDatumPoint.x, mDatumPoint.y - triangularLength / HALF);
        mPath.lineTo(mDatumPoint.x - triangularLength / HALF, mDatumPoint.y);
        mPath.lineTo(mDatumPoint.x, mDatumPoint.y + triangularLength / HALF);
        mPath.close();
        canvas.drawPath(mPath, mBorderPaint);
    }

    private void drawTopTriangle(Canvas canvas) {
        int triangularLength = getPaddingTop();
        if (triangularLength == 0) {
            return;
        }

        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CCW);
        mPath.moveTo(mDatumPoint.x + triangularLength / HALF, mDatumPoint.y);
        mPath.lineTo(mDatumPoint.x, mDatumPoint.y - triangularLength / HALF);
        mPath.lineTo(mDatumPoint.x - triangularLength / HALF, mDatumPoint.y);
        mPath.close();
        canvas.drawPath(mPath, mBorderPaint);
    }

    private void drawRightTriangle(Canvas canvas) {
        int triangularLength = getPaddingRight();
        if (triangularLength == 0) {
            return;
        }

        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CCW);
        mPath.moveTo(mDatumPoint.x, mDatumPoint.y - triangularLength / HALF);
        mPath.lineTo(mDatumPoint.x + triangularLength / HALF, mDatumPoint.y);
        mPath.lineTo(mDatumPoint.x, mDatumPoint.y + triangularLength / HALF);
        mPath.close();
        canvas.drawPath(mPath, mBorderPaint);
    }

    private void drawBottomTriangle(Canvas canvas) {
        int triangularLength = getPaddingBottom();
        if (triangularLength == 0) {
            return;
        }

        mPath.addRoundRect(mRect, mRadius, mRadius, Path.Direction.CCW);
        mPath.moveTo(mDatumPoint.x + triangularLength / HALF, mDatumPoint.y);
        mPath.lineTo(mDatumPoint.x, mDatumPoint.y + triangularLength / HALF);
        mPath.lineTo(mDatumPoint.x - triangularLength / HALF, mDatumPoint.y);
        mPath.close();
        canvas.drawPath(mPath, mBorderPaint);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mRect.left = getPaddingLeft();
        mRect.top = getPaddingTop();
        mRect.right = width - getPaddingRight();
        mRect.bottom = height - getPaddingBottom();

        switch (mDirection) {
            case LEFT:
                mDatumPoint.x = getPaddingLeft();
                mDatumPoint.y = height / HALF;
                break;
            case TOP:
                mDatumPoint.x = width / HALF;
                mDatumPoint.y = getPaddingTop();
                break;
            case RIGHT:
                mDatumPoint.x = width - getPaddingRight();
                mDatumPoint.y = height / HALF;
                break;
            case BOTTOM:
                mDatumPoint.x = width / HALF;
                mDatumPoint.y = height - getPaddingBottom();
                break;
            default:
                break;
        }

        if (mOffset != 0) {
            applyOffset();
        }
    }

    /**
     * 设置三角形偏移位置
     *
     * @param offset 偏移量
     */
    public void setTriangleOffset(int offset) {
        this.mOffset = offset;
        applyOffset();
        invalidate();
    }

    private void applyOffset() {
        switch (mDirection) {
            case LEFT:
            case RIGHT:
                mDatumPoint.y += mOffset;
                break;
            case TOP:
            case BOTTOM:
                mDatumPoint.x += mOffset;
                break;
            default:
                break;
        }
    }
}

