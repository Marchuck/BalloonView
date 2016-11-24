package pl.marchuck.balloonview.balloonviewexample.balloonsView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


/**
 * Project "Monsters"
 * <p/>
 * Created by Yasic on 2016/6/1.
 */
public class BalloonsView extends RelativeLayout {

    public static final String TAG = BalloonsView.class.getSimpleName();

    private float scale = -1;
    private int viewWidth = dp2pix(50), viewHeight = dp2pix(50);
    private int riseDuration = 4000;
    private int bottomPadding = 200;
    private int originsOffset = 60;

    Subject<BalloonModel> publishNewBalloonsSubject = BehaviorSubject.create();

    private final Collection<Integer> list = Collections.synchronizedCollection(new ArrayList<Integer>());

    private final SparseBooleanArray freePositions = new SparseBooleanArray();

    private int startDelay = 0;
    boolean started = false;

    private volatile boolean isDisposed = false;
    private boolean isTimeout;
    private long animationDuration;
    volatile Disposable subscription;

    public BalloonsView(Context context) {
        super(context);
    }

    public BalloonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BalloonsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public BalloonsView withRiseDuration(int riseDuration) {
        this.riseDuration = riseDuration;
        return this;
    }

    public BalloonsView setBottomPadding(int px) {
        this.bottomPadding = px;
        return this;
    }

    public BalloonsView setOriginsOffset(int px) {
        this.originsOffset = px;
        return this;
    }


    public BalloonsView withAnimationDelay(int delay) {
        this.startDelay = delay;
        return this;
    }

    public BalloonsView setItemViewWH(int viewWidth, int viewHeight) {
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
        return this;
    }

    public BalloonsView setGiftBoxImage(Drawable drawable, int positionX, int positionY) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawable);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageView.getWidth(),
                imageView.getHeight());
        this.addView(imageView, layoutParams);
        imageView.setX(positionX);
        imageView.setY(positionY);
        return this;
    }

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public void startAnimation() {
        startAnimation(getWidth(), getHeight());
    }

    void startAnimation(final int rankWidth, final int rankHeight) {

        removeAllViewsWithAnimation();
        destroy();

        freePositions.clear();
        //todo: fix this!!!
        for (int i = 10; i < getWidth() - 110; i += 200) {
            list.add(i);
            freePositions.put(i, true);
        }

        final Disposable subscription = publishNewBalloonsSubject
                .delay(startDelay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BalloonModel>() {
                    @Override
                    public void accept(BalloonModel newBalloon) throws Exception {
                        if (isDisposed) return;
                        bubbleAnimation(newBalloon, rankWidth, rankHeight);
                    }
                });

        if (isTimeout) {
            Observable.just(0L)
                    .delay(animationDuration, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            Log.e(TAG, "TIMEOUT! " + aLong);
                            isDisposed = true;
                            subscription.dispose();
                        }
                    });
        }
    }

    void scheduleTimeout() {
        Log.d(TAG, "scheduleTimeout: ");

    }

    void removeAllViewsWithAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.animate().setDuration(300).alpha(0).start();
        }
        if (getChildCount() > 0)
            getRootView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeAllViews();
                }
            }, 300);
    }


    void bubbleAnimation(BalloonModel newBalloon, int rankWidth, int rankHeight) {
        started = true;

        randomizeWidthAndHeight(rankWidth, rankHeight);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewWidth, viewHeight);
        ImageButton tempImageView = new RisingFloatingButton(getContext()).withBalloonModel(newBalloon);

        addView(tempImageView, layoutParams);

        ValueAnimator valueAnimator = getBesselAnimator(tempImageView, rankWidth, rankHeight);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(valueAnimator);

        animatorSet.start();
        tempImageView.setVisibility(VISIBLE);
    }

    void randomizeWidthAndHeight(int rankWidth, int rankHeight) {
        rankHeight -= bottomPadding;
        int seed = (int) (Math.random() * 3);
        switch (seed) {
            case 0:
                rankWidth -= originsOffset;
                break;
            case 1:
                rankWidth += originsOffset;
                break;
            case 2:
                rankHeight -= originsOffset;
                break;
        }
    }

    ValueAnimator getBesselAnimator(final ImageView imageView, int rankWidth, int rankHeight) {
        float point0[] = new float[2];
        point0[0] = rankWidth / 2;
        point0[1] = rankHeight;

        float point1[] = new float[2];
        point1[0] = (float) ((rankWidth) * (0.10)) + (float) (Math.random() * (rankWidth) * (0.8));
        point1[1] = (float) (rankHeight - Math.random() * rankHeight * (0.5));

        float point2[] = new float[2];
        point2[0] = (float) (Math.random() * rankWidth);
        point2[1] = (float) (Math.random() * (rankHeight - point1[1]));

        float point3[] = new float[2];
        point3[0] = (float) (Math.random() * rankWidth);
        point3[1] = 0;

        BesselEvaluator besselEvaluator = new BesselEvaluator(point1, point2);
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(besselEvaluator, point0, point3);
        valueAnimator.setDuration(riseDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float[] currentPosition = (float[]) animation.getAnimatedValue();
                imageView.setTranslationX(currentPosition[0]);
                imageView.setTranslationY(currentPosition[1]);
            }
        });

        valueAnimator.addListener(new EndableAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                arrangeBalloon(imageView);
            }
        });
        return valueAnimator;
    }

    void arrangeBalloon(final ImageView imageview) {
        //    Log.v(TAG, "arrangeBalloon: ");

        int closestPosition = findClosestPosition(imageview);
        if (closestPosition < 0) {
            removeViewSmoothly(imageview);
            return;
        }
        boolean isFree = freePositions.get(closestPosition);

        if (isFree) {
            boolean removingSucceeded = list.remove((Integer) closestPosition);
            if (removingSucceeded) {
                imageview.animate().translationX(closestPosition).setDuration(300).start();

                freePositions.put(closestPosition, false);
            } else {
                //     Log.e(TAG, "arrangeBalloon: #1 frag case");
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        arrangeBalloon(imageview);
                    }
                }, 300);
            }
        } else {
            // Log.e(TAG, "arrangeBalloon: #2 case");
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    arrangeBalloon(imageview);
                }
            }, 300);
        }
    }

    void removeViewSmoothly(final ImageView imageView) {
        imageView.animate().setDuration(300)
                .alpha(0)
                .translationY(-200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        removeView(imageView);
                        imageView.setImageDrawable(null);
                    }
                }).start();
    }

    int findClosestPosition(View view) {

        Iterator<Integer> it = list.iterator();
        if (!it.hasNext()) return -1;
        int firstPosition = it.next();

        int currentMin = (int) Math.abs(firstPosition - view.getX());
        int closestPosition = firstPosition;
        while (it.hasNext()) {
            int next = it.next();
            int diff = (int) Math.abs(next - view.getX());
            if (diff < currentMin) {
                currentMin = diff;
                closestPosition = next;
            }
        }
        return closestPosition;
    }

    public BalloonsView setTrigger(Observable<BalloonModel> triggerObservable) {
        triggerObservable.subscribe(publishNewBalloonsSubject);
        return this;
    }

    public boolean started() {
        return started;
    }

    public void destroy() {
        Log.d(TAG, "destroy: ");
        if (subscriptions.isDisposed()) subscriptions.dispose();
    }

    private int dp2pix(int dp) {
        if (scale < 0) scale = getResources().getDisplayMetrics().density;
        int pix = (int) (dp * scale + 0.5f);
        return pix;
    }

    private int pix2dp(int pix) {
        if (scale < 0) scale = getResources().getDisplayMetrics().density;
        int dp = (int) (pix / scale + 0.5f);
        return dp;
    }

    public void setAnimationDuration(long animationDuration) {
        this.isTimeout = true;
        this.animationDuration = animationDuration;
    }
}