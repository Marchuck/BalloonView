package pl.marchuck.balloonview.balloonviewexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import pl.marchuck.balloonview.balloonviewexample.balloonsView.BalloonModel;
import pl.marchuck.balloonview.balloonviewexample.balloonsView.BalloonsView;
import pl.marchuck.balloonview.balloonviewexample.balloonsView.CreepyInterpolator;

import static io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BalloonModel theSame = new BalloonModel(null, android.R.drawable.ic_menu_info_details);

        final BalloonsView balloonsView = (BalloonsView) findViewById(R.id.balloons_view);

        final View fab =
                findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setScaleX(0);
                fab.setScaleY(0);
                fab.animate().scaleX(1f).scaleY(1f)
                        .setInterpolator(new CreepyInterpolator()).setDuration(3000).start();

                if (!balloonsView.started()) {

                    Subject<BalloonModel> trigger = BehaviorSubject.create();

                    Observable.interval(400, TimeUnit.MILLISECONDS)
                            .map(new Function<Long, BalloonModel>() {
                                @Override
                                public BalloonModel apply(Long aLong) throws Exception {
                                    return theSame;
                                }
                            }).observeOn(AndroidSchedulers.mainThread()).subscribe(trigger);

                    balloonsView.setTrigger(trigger).startAnimation();
                    balloonsView.setAnimationDuration(5000);
                } else {
                    Log.e(TAG, "onClick: already clicked");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}
