package pl.marchuck.balloonview.balloonviewexample.balloonsView;

import android.animation.Animator;

/**
 * Project "Monsters"
 * <p>
 * Created by Lukasz Marczak
 * on 23.11.2016.
 *
 * reduce boiler plate :
 *
 * one you have to do is to implement onAnimationEnd()
 */

public abstract class EndableAnimationListener implements Animator.AnimatorListener {
    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
