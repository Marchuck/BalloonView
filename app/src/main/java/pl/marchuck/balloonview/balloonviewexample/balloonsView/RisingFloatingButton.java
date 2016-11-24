package pl.marchuck.balloonview.balloonviewexample.balloonsView;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;

import pl.marchuck.balloonview.balloonviewexample.R;


/**
 * Project "Monsters"
 * <p>
 * Created by Lukasz Marczak
 * on 02.10.16.
 */
public class RisingFloatingButton extends FloatingActionButton {
    public static final String TAG = RisingFloatingButton.class.getSimpleName();
    private TimeInterpolator timeInterpolator;


    public RisingFloatingButton(Context context) {
        super(context);
    }

    public RisingFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RisingFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TimeInterpolator getInterpolator() {
        if (timeInterpolator == null)
            timeInterpolator = new AccelerateDecelerateInterpolator();
        return timeInterpolator;
    }

    public View.OnClickListener enableCreepyAnimationOnClick() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                setScaleX(0f);
                setScaleY(0f);
                Log.d(TAG, "onClick: ");
                animate().scaleX(1f)
                        .scaleY(1f)
                        .setInterpolator(new BounceInterpolator()).setDuration(300).start();
            }
        };
    }

    @Override
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            setScaleX(0);
            setScaleY(0);
            animate().setDuration(300)
                    .scaleX(1).scaleY(1)
                    .setInterpolator(getInterpolator())
                    .start();
        }
    }

    public ImageButton withBalloonModel(BalloonModel newBalloon) {

        setOnClickListener(enableCreepyAnimationOnClick());
//        setOnClickListener(newBalloon.onClickListener);
        if (newBalloon.hasImage()) {
            setImageResource(newBalloon.drawableRes);
        }

        int color = getColorFromResource(R.color.colorAccent);
        int whenPressedColor = getColorFromResource(R.color.colorAccentPressed);

        if (newBalloon.hasColor()) {
            color = getColorFromResource(newBalloon.colorRes);
            whenPressedColor = getColorFromResource(newBalloon.pressedColorRes);
            if (whenPressedColor == color) {
                whenPressedColor = makeDarker(whenPressedColor, 0.1f);
            }
        }
        setBackgroundTintList(ColorStateList.valueOf(color));
        setRippleColor(whenPressedColor);

        return this;
    }

    public static int makeDarker(int color, float factor) {
        factor = 1 - factor;
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

    int getColorFromResource(@ColorRes int colorRes) {
        return getResources().getColor(colorRes);
    }
}