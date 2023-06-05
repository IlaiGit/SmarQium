package com.example.beta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 * this class creates animation of the aquarium
 */
public class AquariumView extends View {

    /**
     * creating the object's attributes
     */

    private int mAquariumWidth;
    private int mAquariumHeight;
    private int mAquariumDepth;
    private int mNumberOfFish;
    private List<PointF> mFishPositions;
    private Paint mAquariumPaint;
    private Paint mFishPaint;
    private Random mRandom;
    private boolean mIsAnimating;

    public AquariumView(Context context) {
        super(context);
        init();
    }

    public AquariumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Initialize aquarium paint
        mAquariumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAquariumPaint.setStyle(Paint.Style.STROKE);
        mAquariumPaint.setColor(Color.BLUE);
        mAquariumPaint.setStrokeWidth(10f);

        // Initialize fish paint
        mFishPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFishPaint.setStyle(Paint.Style.FILL);
        mFishPaint.setColor(Color.YELLOW);

        // Initialize fish positions
        mFishPositions = new ArrayList<>();

        // Initialize random number generator
        mRandom = new Random();

        // By default, animation is not running
        mIsAnimating = false;
    }

    public void setAquariumDimensions(int width, int height, int depth) {
        mAquariumWidth = width;
        mAquariumHeight = height;
        mAquariumDepth = depth;
    }

    public void setNumberOfFish(int numberOfFish) {
        mNumberOfFish = numberOfFish;
        generateFishPositions();
    }

    private void generateFishPositions() {
        // Generate random positions for the fish
        mFishPositions.clear();
        for (int i = 0; i < mNumberOfFish; i++) {
            float x = mRandom.nextInt(mAquariumWidth);
            float y = mRandom.nextInt(mAquariumHeight);
            mFishPositions.add(new PointF(x, y));
        }
    }

    public void startAnimation() {
        mIsAnimating = true;
        invalidate();
    }

    public void stopAnimation() {
        mIsAnimating = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw aquarium
        canvas.drawRect(0f, 0f, mAquariumWidth, mAquariumHeight, mAquariumPaint);
        canvas.drawLine(0f, 0f, mAquariumDepth, mAquariumDepth, mAquariumPaint);
        canvas.drawLine(mAquariumWidth, 0f, mAquariumWidth + mAquariumDepth, mAquariumDepth, mAquariumPaint);
        canvas.drawLine(0f, mAquariumHeight, mAquariumDepth, mAquariumHeight + mAquariumDepth, mAquariumPaint);
        canvas.drawLine(mAquariumWidth, mAquariumHeight, mAquariumWidth + mAquariumDepth, mAquariumHeight + mAquariumDepth, mAquariumPaint);

        // Draw fish
        for (PointF position : mFishPositions) {
            canvas.drawCircle(position.x, position.y, 20f, mFishPaint);
        }

        // Animate fish
        if (mIsAnimating) {
            animateFish();
            invalidate();
        }
    }

    private void animateFish() {
        // Move fish to a new random position
        for (PointF position : mFishPositions) {
            float deltaX = mRandom.nextInt(40) - 20;
            float deltaY = mRandom.nextInt(40) - 20;
            position.offset(deltaX, deltaY);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Stop animation when the view is detached from the window
        stopAnimation();
    }
}