package pl.marchuck.balloonview.balloonviewexample.balloonsView;

import android.animation.TimeInterpolator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.animation.BaseInterpolator;

import static pl.marchuck.balloonview.balloonviewexample.MainActivity.TAG;

/**
 * Project "Monsters"
 * <p>
 * Created by Lukasz Marczak
 * on 23.11.2016.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
public class CreepyInterpolator extends BaseInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        float rety = Math.max(3 * input, 0.8f);
        Log.i(TAG, "getInterpolation: " + input + " -> " + rety);

        return rety;
    }
}
