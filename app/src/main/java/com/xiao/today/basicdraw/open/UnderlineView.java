package com.xiao.today.basicdraw.open;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.xiao.today.basicdraw.R;

@SuppressWarnings({"WeakerAccess", "unused"})
public class UnderlineView extends View {
    private static final float UNDERLINE_CLEAR_GAP = 5.5f;

    private final String mFamily;
    private final String mText;
    private final Type mType;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect mBounds = new Rect();
    private final Paint mStroke = new Paint();
    private final Path mUnderline = new Path();
    private final Path mOutline = new Path();

    public enum Type {
        // API level 19 and up
        PATH,
        // API level 1 and up
        REGION
    }

    public UnderlineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderlineView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public UnderlineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UnderlineView,
                defStyleAttr, defStyleRes);
        try {
            int type = a.getInt(R.styleable.UnderlineView_type, 0);
            mType = Type.values()[type];

            String family = a.getString(R.styleable.UnderlineView_family);
            if (family == null) family = "cursive";
            mFamily = family;

            String text = a.getString(R.styleable.UnderlineView_text);
            if (text == null) text = "Elegant, practical, high-quality";
            mText = text;
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        float density = getContext().getResources().getDisplayMetrics().density;

        mPaint.setTextSize(24.0f * density);
        mPaint.setTypeface(Typeface.create(mFamily, Typeface.NORMAL));

        mPaint.getTextBounds(mText, 0, mText.length(), mBounds);
        mPaint.getTextPath(mText, 0, mText.length(), 0.0f, 0.0f, mOutline);

        mStroke.setStyle(Paint.Style.FILL_AND_STROKE);
        mStroke.setStrokeWidth(UNDERLINE_CLEAR_GAP * density);
        mStroke.setStrokeCap(Paint.Cap.BUTT);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (mUnderline.isEmpty()) {
            buildUnderline();
        }

        // Kind-of centered
        canvas.translate(
                (int) ((getWidth() - mBounds.width()) / 2.0f),
                (int) ((getHeight() - mBounds.height()) / 2.0f));

        canvas.drawText(mText, 0.0f, 0.0f, mPaint);
        canvas.drawPath(mUnderline, mPaint);
    }

    private void buildUnderline() {
        float density = getContext().getResources().getDisplayMetrics().density;
        Path strokedOutline = new Path();

        /* 用path实现 */
        if (mType == Type.PATH) {
            // Add the underline rectangle to a path
            /*
            * mUnderline：下划线path
            * */
            mUnderline.addRect(
                    (float) mBounds.left, 3.0f * density,
                    (float) mBounds.right, 3.8f * density,
                    Path.Direction.CW);

            // Intersects the text outline with the underline path to clip it
            /*
            * mOutline：文字path
            * Path.Op.INTERSECT：保留两个 Path 重叠的部分
            * mOutline和mUnderline重叠的部分
            * */
            mOutline.op(mUnderline, Path.Op.INTERSECT);

            // Stroke the clipped text outline and get the result as a fill path
            /*
            * mOutline重叠部分path，输出到strokedOutline中
            * */
            mStroke.getFillPath(mOutline, strokedOutline);

            // Subtract the stroked outline from the underline
            /*
            * Path.Op.DIFFERENCE：保留第一个 Path 中不与第二个 Path 重叠的部分
            * 从下划线path中移除与文字path mOutline重叠的部分
            * */
            mUnderline.op(strokedOutline, Path.Op.DIFFERENCE);
        }
        /* 用region实现 */
        else {
            // Create a rectangular region for the underline
            Rect underlineRect = new Rect(
                    mBounds.left, (int) (3.0f * density),
                    mBounds.right, (int) (3.5f * density));
            /* 下划线region */
            Region underlineRegion = new Region(underlineRect);

            // Create a region for the text outline and clip it with the underline
            /* 文字region */
            Region outlineRegion = new Region();
            outlineRegion.setPath(mOutline, underlineRegion);

            // Extract the resulting region's path, we now have a clipped version
            // of the text outline
            /*
            * path.rewind：清除路径内容，保留底层数据结构
            * */
            mOutline.rewind();
            outlineRegion.getBoundaryPath(mOutline);

            // Stroke the clipped text and get the result as a fill path
            mStroke.getFillPath(mOutline, strokedOutline);

            // Create a region from the clipped stroked outline
            outlineRegion = new Region();
            outlineRegion.setPath(strokedOutline, new Region(mBounds));

            // Subtracts the clipped, stroked outline region from the underline
            underlineRegion.op(outlineRegion, Region.Op.DIFFERENCE);

            // Create a path from the underline region
            underlineRegion.getBoundaryPath(mUnderline);
        }
    }
}