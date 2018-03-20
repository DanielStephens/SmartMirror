package djs.com.mirror.ui.modules;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import djs.com.mirror.R;
import djs.com.mirror.events.Notifier;

/**
 * Created by Daniel on 24/02/2018.
 */

public class TimeActivity implements BasicActivity {

    private final Activity activity;

    private final Notifier notifier;

    private DateFormat mainDateFormat = new SimpleDateFormat("HH:mm");
    private DateFormat secondsDateFormat = new SimpleDateFormat("ss");

    private TextView timeValue;
    private TextView secondsValue;

    private ImageView lineImg;

    private ImageView ball;
    private ImageView tail;

    int base;
    int height;
    int diff;
    double startVelocity;
    double acceleration;
    float density;

    int[] textWidths;

    private Handler updateHandler = new Handler();

    public TimeActivity(Activity activity, Notifier notifier) {
        this.activity = activity;
        this.notifier = notifier;
        subscribe();
    }

    private void subscribe(){

        density = activity.getResources().getDisplayMetrics().density;

        timeValue = (TextView) activity.findViewById(R.id.Time);
        secondsValue = (TextView) activity.findViewById(R.id.Seconds);

//        lineImg = (ImageView)activity.findViewById(R.id.BounceLine);
//
//        tail = (ImageView)activity.findViewById(R.id.Tail);
//        ball = (ImageView)activity.findViewById(R.id.Ball);

        textWidths = setupSizes();
        updateHandler.post(update());
    }

    private Runnable update(){
        return new Runnable() {
            public void run() {

                Date date = new Date();

                timeUpdate(date);
                ballUpdate(date);
                magnitudeUpdate(date);

                updateHandler.postDelayed(update(), 50);
            }
        };
    }

    private void timeUpdate(Date date) {
        timeValue.setText(mainDateFormat.format(date));
        secondsValue.setText(secondsDateFormat.format(date));
    }

    private void ballUpdate(Date date) {
//        base = timeValue.getBaseline()-16;
//        height = base-144;
//        diff = base-height;
//        startVelocity = 4*((double)diff/100f);
//        acceleration = -8*((double)diff/100f);
//
//        float milli = mills-0.05f;
//        milli = milli < 0 ? 1+milli : milli;
//        float delayTime = milli-0.1f;
//        delayTime = delayTime < 0 ? 1+delayTime : delayTime;
//
//        double h1 = (startVelocity*milli + 0.5f*acceleration*milli*milli)*100f;
//        double h2 = (startVelocity*delayTime + 0.5f*acceleration*delayTime*delayTime)*100f;
//
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(16, 16);
//
//        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
//        lp.setMargins(0, base-(int)h1, 0, 0);
//        ball.setLayoutParams(lp);
//
//        tail.setPivotY(0);
//
//        double tailSize = (h1-h2)/32f;
//        tail.setScaleY((float) tailSize);
//        lp = new FrameLayout.LayoutParams(16, 32);
//        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
//        lp.setMargins(0, (base-(int)h1)+8, 0, 0);
//        tail.setLayoutParams(lp);
//
//        float val = 20*mills;
//        if(val > 1){
//            val = (20-val)/19f;
//        }
//        lineImg.setScaleX(val);
    }

    private void magnitudeUpdate(Date date) {
//        int width = ((FrameLayout)findViewById(R.id.ballanimcontainer)).getMeasuredWidth()/2;
//
//        target = width + textWidths[magnitude-1];// + GetSize(magnitude);
//        lineImg.setPivotX(target);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(target, 1);
//        lp.gravity = Gravity.TOP | Gravity.END;
//        lp.setMargins(0, 0, width, 0);
//        lineImg.setLayoutParams(lp);
    }

    int[] setupSizes(){
        int[] is = new int[6];

        String origText = secondsValue.getText().toString();
        secondsValue.setText("0");
        int x = GetWidth(secondsValue, secondsValue.getHeight());
        secondsValue.setText(origText);

        is[0] = x - 6;
        is[1] = is[0] + x;

        origText = timeValue.getText().toString();
        timeValue.setText("0");
        x = GetWidth(timeValue, timeValue.getHeight());
        timeValue.setText(":");
        int c = GetWidth(timeValue, timeValue.getHeight());
        timeValue.setText(origText);

        is[2] = is[1] + 10 + x;
        is[3] = is[2] + x;
        is[4] = is[3] + c;;
        is[5] = is[4] + x;;

        return is;
    }

    public int GetHeight(TextView textView, int width){
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    public int GetWidth(TextView textView, int height){
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredWidth();
    }

    private int getResource(String name) {
        return activity.getResources().getIdentifier(name, "drawable", activity.getPackageName());
    }

}
