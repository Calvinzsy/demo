package com.example.calvin.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * A custom view that can contain both title and subtitle. In addition, it has borders around and a triangle decoration at the bottom right corner.
 * Note that rather than using our own custom attribute for background, we instead reuse the view's background and draw everything else upon it. Therefore,
 * the background drawable is not limited to be a simple {@link android.graphics.drawable.ColorDrawable}.
 */
public class DocumentView extends View {

    //Default border color
    private static final int DEFAULT_BORDER_COLOR = 0xFF000000;

    //Default border width in dip
    private static final int DEFAULT_BORDER_WIDTH_IN_DIP = 1;

    //Default decoration color
    private static final int DEFAULT_DECOR_COLOR = 0xFF888888;

    //Default title color
    private static final int DEFAULT_TITLE_COLOR = 0xFF000000;

    //Default subtitle color
    private static final int DEFAULT_SUBTITLE_COLOR = 0xFF888888;

    //Default decoration size
    private static final int DEFAULT_DECOR_SIZE_IN_DIP = 20;

    //Default title and subtitle text size in sp
    private static final int DEFAULT_TEXT_SIZE_IN_SP = 16;

    //Paint used for drawing borders
    private Paint mBorderPaint;

    //Paint used for drawing decoration
    private Paint mDecorPaint;

    //Paint used for drawing title
    private TextPaint mTitlePaint;

    //Paint used for drawing
    private TextPaint mSubtitlePaint;

    //Paint used for cropping out the bottom right corner
    private Paint mDecorMaskPaint;

    //The size of decoration square
    private float mDecorSize;

    //The path used for cropping out the bottom right corner
    private Path mDecorMaskPath;

    //The path used for drawing decoration
    private Path mDecorPath;

    //The path used for drawing borders
    private Path mBorderPath;

    //The title
    private CharSequence mTitle;

    //The subtitle
    private CharSequence mSubtitle;

    //The helper layout for drawing title
    private Layout mTitleLayout;

    //The helper layout for drawing subtitle
    private Layout mSubtitleLayout;

    /**
     * {@inheritDoc}
     */
    public DocumentView(Context context) {
        this(context, null);
    }

    /**
     * {@inheritDoc}
     */
    public DocumentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * {@inheritDoc}
     */
    public DocumentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        initializeAttributes(context, attrs, defStyleAttr, 0);
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DocumentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
        initializeAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Initialize all paints and paths. Also enable hardware acceleration for this view.
     */
    private void initialize() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mDecorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDecorPaint.setStyle(Paint.Style.FILL);
        mTitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSubtitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mDecorMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDecorMaskPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDecorMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mDecorMaskPath = new Path();
        mDecorPath = new Path();
        mBorderPath = new Path();
    }

    /**
     * Initialize all custom attributes from the resource.
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     * @param defStyleRes A resource identifier of a style resource that
     *        supplies default values for the view, used only if
     *        defStyleAttr is 0 or can not be found in the theme. Can be 0
     *        to not look for defaults.
     */
    private void initializeAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DocumentView, defStyleAttr, defStyleRes);
        if (array != null) {
            int borderColor = array.getColor(R.styleable.DocumentView_borderColor, DEFAULT_BORDER_COLOR);
            mBorderPaint.setColor(borderColor);

            float defaultBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_WIDTH_IN_DIP, getResources().getDisplayMetrics());
            float borderWidth = array.getDimension(R.styleable.DocumentView_borderWidth, defaultBorderWidth);
            mBorderPaint.setStrokeWidth(borderWidth);

            int decorColor = array.getColor(R.styleable.DocumentView_decorColor, DEFAULT_DECOR_COLOR);
            mDecorPaint.setColor(decorColor);

            int titleColor = array.getColor(R.styleable.DocumentView_titleColor, DEFAULT_TITLE_COLOR);
            mTitlePaint.setColor(titleColor);

            int subtitleColor = array.getColor(R.styleable.DocumentView_subtitleColor, DEFAULT_SUBTITLE_COLOR);
            mSubtitlePaint.setColor(subtitleColor);

            float defaultDecorSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DECOR_SIZE_IN_DIP, getResources().getDisplayMetrics());
            mDecorSize = array.getDimension(R.styleable.DocumentView_decorSize, defaultDecorSize);

            mTitle = array.getText(R.styleable.DocumentView_titleText);
            mTitle = mTitle == null ? "" : mTitle;

            mSubtitle = array.getText(R.styleable.DocumentView_subtitleText);
            mSubtitle = mSubtitle == null ? "" : mSubtitle;

            float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE_IN_SP, getResources().getDisplayMetrics());
            float titleTextSize = array.getDimension(R.styleable.DocumentView_titleTextSize, defaultTextSize);
            float subtitleTextSize = array.getDimension(R.styleable.DocumentView_subtitleTextSize, defaultTextSize);
            mTitlePaint.setTextSize(titleTextSize);
            mSubtitlePaint.setTextSize(subtitleTextSize);

            array.recycle();
        }
    }

    /**
     * Measure the view based on given requirements. It takes both decoration and text into account for its content space.
     * Therefore, it requires measuring of title and subtitle text on the way. The following steps roughly describes this process.
     *
     * 1. For each title, measure the width required to draw it on a single line and take the largest as the desired content width.
     *
     * 2. Make sure the total desired width doesn't exceed total width available.
     *
     * 3. Make use of the content space for each title separately so that shorter title doesn't have to take the whole line. If there
     * is no content space available after excluding the padding, then the title width should be 0. Use {@link StaticLayout} for drawing
     * multi-line title.
     *
     * 4. If a title is to be drawn, add its height to desired content height.
     *
     * 5. Make sure the total desired height doesn't exceed total height available.
     *
     * @param widthMeasureSpec horizontal space requirements as imposed by the parent.
     *                         The requirements are encoded with
     *                         {@link android.view.View.MeasureSpec}.
     * @param heightMeasureSpec vertical space requirements as imposed by the parent.
     *                         The requirements are encoded with
     *                         {@link android.view.View.MeasureSpec}.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int paddingLeft = getPaddingLeft();
        //Take decoration into account so that the titles don't overlap with it. Therefore, consider both right padding
        //and decoration size and use whichever is larger for right spacing.
        int paddingRight = Math.max(getPaddingRight(), (int)Math.ceil(mDecorSize));
        int paddingTop = getPaddingTop();
        //Take decoration into account so that the titles don't overlap with it. Therefore, consider both bottom padding
        //and decoration size and use whichever is larger for bottom spacing.
        int paddingBottom = Math.max(getPaddingBottom(), (int)Math.ceil(mDecorSize));

        int desiredWidth;
        int desiredHeight = 0;
        int measuredWidth;
        int measuredHeight;
        int desiredTitleTextWidth = 0;
        int desiredSubtitleTextWidth = 0;
        int measuredTitleWidth;
        int measuredSubtitleWidth;
        int measuredContentWidth;

        //Measure title if its not empty.
        if (!mTitle.toString().isEmpty()) {
            desiredTitleTextWidth = (int)Math.ceil(mTitlePaint.measureText(mTitle.toString()));
        }
        //Measure subtitle if its not empty.
        if (!mSubtitle.toString().isEmpty()) {
            desiredSubtitleTextWidth = (int)Math.ceil(mSubtitlePaint.measureText(mSubtitle.toString()));
        }
        //Calculate desired width based on mode.
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = widthSize;
        }else {
            desiredWidth = Math.max(desiredTitleTextWidth, desiredSubtitleTextWidth);
            desiredWidth += paddingLeft + paddingRight;
        }
        //Make sure the measured width doesn't exceed total width available.
        measuredWidth = Math.min(widthSize, desiredWidth);

        //Calculate content width. If the padding is too big for the view to take any content, use 0 as proposed content width.
        measuredContentWidth = Math.max(measuredWidth - paddingLeft - paddingRight, 0);

        //Calculate content width for title, if the title is shorter than proposed content width, use its own text width.
        measuredTitleWidth = Math.min(measuredContentWidth, desiredTitleTextWidth);

        //Calculate content width for subtitle, if the subtitle is shorter than proposed content width, use its own text width.
        measuredSubtitleWidth = Math.min(measuredContentWidth, desiredSubtitleTextWidth);
        //Create helper layouts for drawing text.
        mTitleLayout = new StaticLayout(mTitle, mTitlePaint, measuredTitleWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        mSubtitleLayout = new StaticLayout(mSubtitle, mSubtitlePaint, measuredSubtitleWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

        //Calculate desired height based on mode.
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = heightSize;
        }else {
            if (!mTitle.toString().isEmpty()) {
                desiredHeight += mTitleLayout.getHeight();
            }
            if (!mSubtitle.toString().isEmpty()) {
                desiredHeight += mSubtitleLayout.getHeight();
            }
            desiredHeight += paddingTop + paddingBottom;
        }

        //Make sure the measured height doesn't exceed total height available.
        measuredHeight = Math.min(heightSize, desiredHeight);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * Draw this view in the following steps.
     *
     * 1. Mask out the bottom right corner.
     *
     * 2. Draw decoration.
     *
     * 3. Draw borders.
     *
     * 4. Draw titles.
     *
     * @param canvas the canvas on which the background will be drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float borderRadius = mBorderPaint.getStrokeWidth() / 2;

        //Crop out the bottom right corner.
        mDecorMaskPath.rewind();
        mDecorMaskPath.moveTo(width, height);
        mDecorMaskPath.lineTo(width - mDecorSize, height);
        mDecorMaskPath.lineTo(width, height - mDecorSize);
        mDecorMaskPath.close();
        canvas.drawPath(mDecorMaskPath, mDecorMaskPaint);

        //Draw the triangle decoration.
        mDecorPath.rewind();
        mDecorPath.moveTo(width - mDecorSize, height - mDecorSize);
        mDecorPath.lineTo(width, height - mDecorSize);
        mDecorPath.lineTo(width - mDecorSize, height);
        mDecorPath.close();
        canvas.drawPath(mDecorPath, mDecorPaint);

        //Draw borders.
        mBorderPath.rewind();
        mBorderPath.moveTo(width - mDecorSize, height - borderRadius);
        mBorderPath.lineTo(borderRadius, height - borderRadius);
        mBorderPath.lineTo(borderRadius, borderRadius);
        mBorderPath.lineTo(width - borderRadius, borderRadius);
        mBorderPath.lineTo(width - borderRadius, height - mDecorSize);
        mBorderPath.lineTo(width - mDecorSize, height - borderRadius);
        mBorderPath.lineTo(width - mDecorSize, height - mDecorSize);
        mBorderPath.lineTo(width - borderRadius, height - mDecorSize);
        canvas.drawPath(mBorderPath, mBorderPaint);

        int paddingLeft = getPaddingLeft();
        int paddingRight = Math.max(getPaddingRight(), (int)Math.ceil(mDecorSize));
        int paddingTop = getPaddingTop();
        int paddingBottom = Math.max(getPaddingBottom(), (int)Math.ceil(mDecorSize));
        int titleWidth = mTitleLayout.getWidth();
        int titleHeight = mTitleLayout.getHeight();
        int subtitleWidth = mSubtitleLayout.getWidth();
        int subtitleHeight = mSubtitleLayout.getHeight();
        int totalWidth = width - paddingLeft - paddingRight;
        int totalHeight = height - paddingTop - paddingBottom;
        int contentWidth = 0;
        int contentHeight = 0;

        //Calculate the total content height by adding up the height of each title if it is to be drawn.
        if (titleWidth > 0) {
            contentWidth = Math.max(contentWidth, titleWidth);
            contentHeight += titleHeight;
        }
        if (subtitleWidth > 0) {
            contentWidth = Math.max(contentWidth, subtitleWidth);
            contentHeight += subtitleHeight;
        }

        //Discard if not enough content space
        if (contentHeight > totalHeight) {
            return;
        }

        //Save the canvas before coordination translation.
        canvas.save();
        //Move the coordinate to the beginning place of the title.
        canvas.translate(paddingLeft + (totalWidth - contentWidth) / 2, paddingTop + (totalHeight - contentHeight) / 2);
        //Draw title if available. Move the coordinate to the beginning of subtitle afterwards.
        if (titleWidth > 0 ) {
            mTitleLayout.draw(canvas);
            canvas.translate(0, titleHeight);
        }
        //Draw subtitle if available.
        if (subtitleWidth > 0) {
            mSubtitleLayout.draw(canvas);
        }
        //Restore coordinate system.
        canvas.restore();
    }

    /**
     * Set the border color.
     * @param borderColor the border color to be set.
     */
    public void setBorderColor(int borderColor) {
        mBorderPaint.setColor(borderColor);
        invalidate();
    }

    /**
     * Set the border width.
     * @param borderWidth the border width in pixel.
     */
    public void setBorderWidth(float borderWidth) {
        mBorderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    /**
     * Set the decoration color.
     * @param decorColor the decoration color to be set.
     */
    public void setDecorColor(int decorColor) {
        mDecorPaint.setColor(decorColor);
        invalidate();
    }

    /**
     * Set the decoration size.
     * @param decorSize the decoration size to be set.
     */
    public void setDecorSize(float decorSize) {
        mDecorSize = decorSize;
        invalidate();
    }

    /**
     * Set the title text color.
     * @param titleTextColor the title text color to be set.
     */
    public void setTitleColor(int titleTextColor) {
        mTitlePaint.setColor(titleTextColor);
        invalidate();
    }

    /**
     * Set the title text size.
     * @param titleTextSize the title text size to be set.
     */
    public void setTitleSize(float titleTextSize) {
        mTitlePaint.setTextSize(titleTextSize);
        requestLayout();
    }

    /**
     * Set the title text.
     * @param titleText the title text to be set.
     */
    public void setTitle(CharSequence titleText) {
        mTitle = titleText == null ? "" : titleText;
        requestLayout();
    }

    /**
     * Set the subtitle text color.
     * @param subtitleTextColor the subtitle text color to be set.
     */
    public void setSubtitleColor(int subtitleTextColor) {
        mSubtitlePaint.setColor(subtitleTextColor);
        invalidate();
    }

    /**
     * Set the subtitle text size.
     * @param subtitleTextSize the subtitle text size to be set.
     */
    public void setSubtitleSize(float subtitleTextSize) {
        mSubtitlePaint.setTextSize(subtitleTextSize);
        requestLayout();
    }

    /**
     * Set the subtitle text.
     * @param subtitleText the subtitle text to be set.
     */
    public void setSubtitle(CharSequence subtitleText) {
        mSubtitle = subtitleText == null ? "" : subtitleText;
        requestLayout();
    }

    /**
     * Save custom attributes during configuration change.
     * @return the saved state.
     */
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.borderColor = mBorderPaint.getColor();
        savedState.borderWidth = mBorderPaint.getStrokeWidth();
        savedState.decorColor = mDecorPaint.getColor();
        savedState.decorSize = mDecorSize;
        savedState.titleColor = mTitlePaint.getColor();
        savedState.titleSize = mTitlePaint.getTextSize();
        savedState.title = mTitle;
        savedState.subtitleColor = mSubtitlePaint.getColor();
        savedState.subtitleSize = mSubtitlePaint.getTextSize();
        savedState.subtitle = mSubtitle;
        return savedState;
    }

    /**
     * Restore custom attributes during configuration change.
     * @param state the saved state to restore from.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mBorderPaint.setColor(savedState.borderColor);
        mBorderPaint.setStrokeWidth(savedState.borderWidth);
        mDecorPaint.setColor(savedState.decorColor);
        mDecorSize = savedState.decorSize;
        mTitlePaint.setColor(savedState.titleColor);
        mTitlePaint.setTextSize(savedState.titleSize);
        mTitle = savedState.title;
        mSubtitlePaint.setColor(savedState.subtitleColor);
        mSubtitlePaint.setTextSize(savedState.subtitleSize);
        mSubtitle = savedState.subtitle;
    }

    /**
     * A class for saving and restoring this view's custom states.
     */
    private static class SavedState extends BaseSavedState {

        private int borderColor;
        private float borderWidth;
        private int decorColor;
        private float decorSize;
        private int titleColor;
        private float titleSize;
        private CharSequence title;
        private int subtitleColor;
        private float subtitleSize;
        private CharSequence subtitle;

        private SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            borderColor = in.readInt();
            borderWidth = in.readFloat();
            decorColor = in.readInt();
            decorSize = in.readFloat();
            titleColor = in.readInt();
            titleSize = in.readFloat();
            title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            subtitleColor = in.readInt();
            subtitleSize = in.readFloat();
            subtitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(borderColor);
            out.writeFloat(borderWidth);
            out.writeInt(decorColor);
            out.writeFloat(decorSize);
            out.writeInt(titleColor);
            out.writeFloat(titleSize);
            TextUtils.writeToParcel(title, out, flags);
            out.writeInt(subtitleColor);
            out.writeFloat(subtitleSize);
            TextUtils.writeToParcel(subtitle, out, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
