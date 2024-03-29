package com.carouseldemo.controls;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;

/**
 * This class encapsulates rotation.  The duration of the rotation
 * can be passed in the constructor and specifies the maximum time that
 * the rotation animation should take.  Past this time, the rotation is 
 * automatically moved to its final stage and computeRotationOffset()
 * will always return false to indicate that scrolling is over.
 */
public class Rotator {
    private int mMode;
    private float mStartAngle;
    private float mFinalAngle;
    private float mMaxAngle;
    private float mMinAngle;
    private float mCurrAngle;
    
    private long mStartTime;
    private int mDuration;
    
    private float mDeltaAngle;
    
    private float mViscousFluidScale;
    private float mViscousFluidNormalize;
    private boolean mFinished;

    private float mCoeffAngle = 0.0f;
    private float mVelocity;
    
    private static final int DEFAULT_DURATION = 250;
    private static final int SCROLL_MODE = 0;
    private static final int FLING_MODE = 1;
    
    private final float mDeceleration;
    
    
    /**
     * Create a Scroller with the specified interpolator. If the interpolator is
     * null, the default (viscous) interpolator will be used.
     */
    public Rotator(Context context) {
        mFinished = true;
        float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mDeceleration = SensorManager.GRAVITY_EARTH   // g (m/s^2)
                      * 39.37f                        // inch/meter
                      * ppi                           // pixels per inch
                      * ViewConfiguration.getScrollFriction();    
    }
    
    /**
     * 
     * Returns whether the scroller has finished scrolling.
     * 
     * @return True if the scroller has finished scrolling, false otherwise.
     */
    public final boolean isFinished() {
        return mFinished;
    }
    
    /**
     * Force the finished field to a particular value.
     *  
     * @param finished The new finished value.
     */
    public final void forceFinished(boolean finished) {
        mFinished = finished;
    }
    
    /**
     * Returns how long the scroll event will take, in milliseconds.
     * 
     * @return The duration of the scroll in milliseconds.
     */
    public final int getDuration() {
        return mDuration;
    }
    
    /**
     * Returns the current X offset in the scroll. 
     * 
     * @return The new X offset as an absolute distance from the origin.
     */
    public final float getCurrAngle() {
        return mCurrAngle;
    }
        
    
    
    /**
     * @hide
     * Returns the current velocity.
     *
     * @return The original velocity less the deceleration. Result may be
     * negative.
     */
    public float getCurrVelocity() {
        return mVelocity - mDeceleration * timePassed() / 2000.0f;
    }

    /**
     * Returns the start X offset in the scroll. 
     * 
     * @return The start X offset as an absolute distance from the origin.
     */
    public final float getStartAngle() {
        return mStartAngle;
    }    
    
    
    /**
     * Returns where the scroll will end. Valid only for "fling" scrolls.
     * 
     * @return The final X offset as an absolute distance from the origin.
     */
    public final float getFinalAngle() {
        return mFinalAngle;
    }    
    
    
    /**
     * Returns the time elapsed since the beginning of the scrolling.
     *
     * @return The elapsed time in milliseconds.
     */
    public int timePassed() {
        return (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    }
    
    /**
     * Extend the scroll animation. This allows a running animation to scroll
     * further and longer, when used with {@link #setFinalX(int)} or {@link #setFinalY(int)}.
     *
     * @param extend Additional time to scroll in milliseconds.
     * @see #setFinalX(int)
     * @see #setFinalY(int)
     */
    public void extendDuration(int extend) {
        int passed = timePassed();
        mDuration = passed + extend;
        mFinished = false;
    }
    
    /**
     * Stops the animation. Contrary to {@link #forceFinished(boolean)},
     * aborting the animating cause the scroller to move to the final x and y
     * position
     *
     * @see #forceFinished(boolean)
     */
    public void abortAnimation() {
        mCurrAngle = mFinalAngle;
        mFinished = true;
    }    
    
    private float viscousFluid(float x)
    {
        x *= mViscousFluidScale;
        if (x < 1.0f) {
            x -= (1.0f - (float)Math.exp(-x));
        } else {
            float start = 0.36787944117f;   // 1/e == exp(-1)
            x = 1.0f - (float)Math.exp(1.0f - x);
            x = start + x * (1.0f - start);
        }
        x *= mViscousFluidNormalize;
        return x;
    }    
    
    /**
     * Sets the final position (X) for this scroller.
     *
     * @param newX The new X offset as an absolute distance from the origin.
     * @see #extendDuration(int)
     * @see #setFinalY(int)
     */
    public void setFinalAngle(int newAngle) {
        mFinalAngle = newAngle;
        mDeltaAngle = mFinalAngle - mStartAngle;
        mFinished = false;
    }    
    

    /**
     * Call this when you want to know the new location.  If it returns true,
     * the animation is not yet finished.  loc will be altered to provide the
     * new location.
     */ 
    public boolean computeAngleOffset()
    {
        if (mFinished) {
            return false;
        }
        
        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
        
        if (timePassed < mDuration) {
        	switch (mMode) {
        		case SCROLL_MODE:
        			float sc = (float)timePassed / mDuration;
                    mCurrAngle = mStartAngle + Math.round(mDeltaAngle * sc);        			
                    break;
        		case FLING_MODE:
                    float timePassedSeconds = timePassed / 1000.0f;
                    float distance = (mVelocity * timePassedSeconds)
                            - (mDeceleration * timePassedSeconds * timePassedSeconds / 2.0f);
                    
                    mCurrAngle = mStartAngle + Math.round(distance * mCoeffAngle);
                    // Pin to mMinX <= mCurrX <= mMaxX
                    mCurrAngle = Math.min(mCurrAngle, mMaxAngle);
                    mCurrAngle = Math.max(mCurrAngle, mMinAngle);
                    break;                    
        	}
        }
        else
        {
        	mCurrAngle = mFinalAngle;
        	mFinished = true;
        }
        return true;
    }
    
    
    /**
     * Start scrolling by providing a starting point and the distance to travel.
     * 
     * @param startX Starting horizontal scroll offset in pixels. Positive
     *        numbers will scroll the content to the left.
     * @param startY Starting vertical scroll offset in pixels. Positive numbers
     *        will scroll the content up.
     * @param dx Horizontal distance to travel. Positive numbers will scroll the
     *        content to the left.
     * @param dy Vertical distance to travel. Positive numbers will scroll the
     *        content up.
     * @param duration Duration of the scroll in milliseconds.
     */
    public void startRotate(float startAngle, float dAngle, int duration) {
        mMode = SCROLL_MODE;
        mFinished = false;
        mDuration = duration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mStartAngle = startAngle;
        mFinalAngle = startAngle + dAngle;
        mDeltaAngle = dAngle;
        // This controls the viscous fluid effect (how much of it)
        mViscousFluidScale = 8.0f;
        // must be set to 1.0 (used in viscousFluid())
        mViscousFluidNormalize = 1.0f;
        mViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
    }    
    
    /**
     * Start scrolling by providing a starting point and the distance to travel.
     * The scroll will use the default value of 250 milliseconds for the
     * duration.
     * 
     * @param startX Starting horizontal scroll offset in pixels. Positive
     *        numbers will scroll the content to the left.
     * @param startY Starting vertical scroll offset in pixels. Positive numbers
     *        will scroll the content up.
     * @param dx Horizontal distance to travel. Positive numbers will scroll the
     *        content to the left.
     * @param dy Vertical distance to travel. Positive numbers will scroll the
     *        content up.
     */
    public void startRotate(float startAngle, float dAngle) {
        startRotate(startAngle, dAngle, DEFAULT_DURATION);
    }
    
    
    /**
     * Start scrolling based on a fling gesture. The distance travelled will
     * depend on the initial velocity of the fling.
     * 
     * @param startX Starting point of the scroll (X)
     * @param startY Starting point of the scroll (Y)
     * @param velocityX Initial velocity of the fling (X) measured in pixels per
     *        second.
     * @param velocityY Initial velocity of the fling (Y) measured in pixels per
     *        second
     * @param minX Minimum X value. The scroller will not scroll past this
     *        point.
     * @param maxX Maximum X value. The scroller will not scroll past this
     *        point.
     * @param minY Minimum Y value. The scroller will not scroll past this
     *        point.
     * @param maxY Maximum Y value. The scroller will not scroll past this
     *        point.
     */
    public void fling(float startAngle, float velocityAngle, 
            float minAngle, float maxAngle) {
        mMode = FLING_MODE;
        mFinished = false;

        float velocity = velocityAngle;
     
        mVelocity = velocity;
        mDuration = (int) (1000 * velocity / mDeceleration); // Duration is in
                                                            // milliseconds
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mStartAngle = startAngle;

        mCoeffAngle = 1.0f;

        int totalDistance = (int) ((velocity * velocity) / (2 * mDeceleration));
        
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
        
        
        mFinalAngle = startAngle + Math.round(totalDistance * mCoeffAngle);
        // Pin to mMinX <= mFinalX <= mMaxX
        mFinalAngle = Math.min(mFinalAngle, mMaxAngle);
        mFinalAngle = Math.max(mFinalAngle, mMinAngle);
        
    }
    
    
}
