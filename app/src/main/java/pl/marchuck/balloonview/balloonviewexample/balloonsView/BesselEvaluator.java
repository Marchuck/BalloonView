package pl.marchuck.balloonview.balloonviewexample.balloonsView;

import android.animation.TypeEvaluator;

/**
 * Project "Monsters"
 * <p>
 * Created by Lukasz Marczak
 * on 23.11.2016.
 */

public class BesselEvaluator implements TypeEvaluator<float[]> {
    private float point1[] = new float[2], point2[] = new float[2];

    public BesselEvaluator(float[] point1, float[] point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public float[] evaluate(float fraction, float[] point0, float[] point3) {
        float[] currentPosition = new float[2];
        currentPosition[0] = point0[0] * (1 - fraction) * (1 - fraction) * (1 - fraction)
                + point1[0] * 3 * fraction * (1 - fraction) * (1 - fraction)
                + point2[0] * 3 * (1 - fraction) * fraction * fraction
                + point3[0] * fraction * fraction * fraction;
        currentPosition[1] = point0[1] * (1 - fraction) * (1 - fraction) * (1 - fraction)
                + point1[1] * 3 * fraction * (1 - fraction) * (1 - fraction)
                + point2[1] * 3 * (1 - fraction) * fraction * fraction
                + point3[1] * fraction * fraction * fraction;
        return currentPosition;
    }
}