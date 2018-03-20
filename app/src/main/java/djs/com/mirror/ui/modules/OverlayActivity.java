package djs.com.mirror.ui.modules;

import android.app.Activity;
import android.graphics.Color;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.FrameLayout;

import djs.com.mirror.R;
import djs.com.mirror.basics.Consumer;
import djs.com.mirror.events.Notifier;
import djs.com.mirror.modules.events.SleepEvent;
import djs.com.mirror.modules.events.WakeEvent;

/**
 * Created by Daniel on 24/02/2018.
 */

public class OverlayActivity implements BasicActivity {

    private final Activity activity;

    private final Notifier notifier;

    private FrameLayout overlay;

    public OverlayActivity(Activity activity, Notifier notifier) {
        this.activity = activity;
        this.notifier = notifier;
        subscribe();
    }

    private void subscribe() {
        overlay = (FrameLayout) activity.findViewById(R.id.overlay);

        notifier.subscribe(SleepEvent.class, new Consumer<SleepEvent>() {
            @Override
            public void accept(SleepEvent value) {
                try {
                    setBrightness(0);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        notifier.subscribe(WakeEvent.class, new Consumer<WakeEvent>() {
            @Override
            public void accept(WakeEvent value) {
                try {
                    setBrightness(1);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setBrightness(float f) throws Settings.SettingNotFoundException {
        overlay.setAlpha(1-f);
        overlay.setBackgroundColor(Color.argb(255, 0, 0, 0));
        int brightnessMode = Settings.System.getInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE);
        if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Settings.System.putInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = f;
        activity.getWindow().setAttributes(layoutParams);
    }

}
