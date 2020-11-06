package com.example.androidcustomview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.androidcustomview.R;

public class CustomView extends View {

    private static final int SQUARE_SIZE_DEF = 300;
    private Rect mRectSquare;

    private Paint mPaintSquare;
    private Paint mPaintCircle;

    private int mSquareColor;
    private int mSquareSize;

    private float mCircleX, mCircleY;
    private float mCircleRadius = 100f;

    private RectF bounds;
    private float[] xPos = new float[360];
    private float[] yPos = new float[360];

    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);

    }

    public float getCircleX(){
        return mCircleX;
    }

    public float getCircleY(){
        return mCircleY;
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mRectSquare = new Rect();

        mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);

        if(set == null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);

        mSquareColor = ta.getColor(R.styleable.CustomView_square_color, Color.GREEN);
        mSquareSize = ta.getDimensionPixelOffset(R.styleable.CustomView_square_size, SQUARE_SIZE_DEF);

        mPaintSquare.setColor(mSquareColor);
        mPaintCircle.setColor(Color.parseColor("#00ccff"));

        ta.recycle();
    }

    public void swapColor() {
        mPaintSquare.setColor((mPaintSquare.getColor() == mSquareColor ? Color.RED : mSquareColor));

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mRectSquare.left = 50;
        mRectSquare.top = 50;
        mRectSquare.right = mRectSquare.left + mSquareSize;
        mRectSquare.bottom = mRectSquare.top + mSquareSize;

        canvas.drawRect(mRectSquare, mPaintSquare);

        if(mCircleX == 0f || mCircleY == 0f){
            mCircleX = getWidth() / 2f;
            mCircleY = getHeight() / 2f;
        }

        canvas.drawCircle(mCircleX, mCircleY, mCircleRadius, mPaintCircle);

        bounds = new RectF(canvas.getClipBounds());
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();

        int angleDeg = 0;
        float radius = 20f;

        while(angleDeg != 360){
            xPos[angleDeg] = radius * (float)Math.cos(Math.toRadians(angleDeg)) + centerX;
            yPos[angleDeg] = radius * (float)Math.sin(Math.toRadians(angleDeg)) + centerY;

            if(angleDeg >= 1){
                canvas.drawLine(xPos[angleDeg - 1], yPos[angleDeg - 1], xPos[angleDeg], yPos[angleDeg], mPaintCircle);
            }

            angleDeg++;
        }


        //draw my point at xPos/yPos

    }

    public void setPos(float x, float y){
        mCircleX = x;
        mCircleY = y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {


                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                float x = event.getX();
                float y = event.getY();

                double dx = Math.pow(x - mCircleX, 2);
                double dy = Math.pow(y - mCircleY, 2);

                if(dx + dy < Math.pow(mCircleRadius, 2)){
                    mCircleX = x;
                    mCircleY = y;

                    postInvalidate();

                    return true;
                }

                return value;
            }
        }

        return value;
    }
}
