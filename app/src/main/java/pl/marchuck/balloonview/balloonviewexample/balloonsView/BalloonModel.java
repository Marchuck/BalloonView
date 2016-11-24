package pl.marchuck.balloonview.balloonviewexample.balloonsView;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import static android.view.Menu.NONE;

/**
 * Project "Monsters"
 * <p>
 * Created by Lukasz Marczak
 * on 23.11.2016.
 */

public class BalloonModel {

    @Nullable
    public final View.OnClickListener onClickListener;

    @NonNull
    public final String shortText;

    @ColorRes
    public final int colorRes;

    @ColorRes
    public final int pressedColorRes;

    @DrawableRes
    public final int drawableRes;

    public BalloonModel(@Nullable View.OnClickListener onClickListener,
                        @NonNull String shortText,
                        @ColorRes int colorRes) {

        this(onClickListener, shortText, NONE, colorRes, colorRes);
    }

    public BalloonModel(@Nullable View.OnClickListener onClickListener,
                        @DrawableRes int drawableRes) {
        this(onClickListener, "", drawableRes, NONE, NONE);
    }

    public BalloonModel(@Nullable View.OnClickListener onClickListener,
                        @NonNull String shortText,
                        @ColorRes int colorRes, @ColorRes int pressedColorRes) {

        this(onClickListener, shortText, NONE, colorRes, pressedColorRes);
    }
    public BalloonModel(@Nullable View.OnClickListener onClickListener,
                        @NonNull String shortText,
                        @DrawableRes int drawableRes,
                        @ColorRes int colorRes, @ColorRes int pressedColorRes) {
        this.onClickListener = onClickListener;
        this.shortText = shortText;
        this.colorRes = colorRes;
        this.pressedColorRes = pressedColorRes;
        this.drawableRes = drawableRes;
    }

    public boolean hasImage() {
        return drawableRes != NONE;
    }

    public boolean hasColor() {
        return colorRes != NONE;
    }
}
