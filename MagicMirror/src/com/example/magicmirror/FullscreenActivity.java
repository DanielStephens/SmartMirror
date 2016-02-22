package com.example.magicmirror;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.magicmirror.util.SystemUiHider;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Color;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity{
	public static FullscreenActivity instance;
	
	static int networkAccessed = 0;
	static int weatherUpdatedAccessed = 0;
	
	ArrayList<NewsItem> newsItemArray;
		
	int newsIndex = -1;
	int colour = 0;
	int fadeDirection = 1;
	boolean firstLoop = true;
	boolean firstLoop2 = true;
	
	int blackoverlay = 0;
	
	ContentResolver cr;
	private TextView timeValue;
	private TextView secondsValue;
	TextView dateValue;
	private TextView endingValue;
	private TextView monthValue;
	private TextView tempValue;
	private TextView curTempValue;
	private ImageView weatherImg;
	private ImageView lineImg;
	
	private TextView wordOfDay;
	private TextView wordDefinitions;
	private TextView newsHeading;
	private TextView newsSummary;
	private TextView calendarValue;
	//private View loadingGif;
	
	private ImageView ball;
	private ImageView tail;
	
	private FrameLayout overlay;
	
	int base;
	int height;
    int diff;
    double startVelocity;
    double acceleration;
	
	float density;
	
	int target;
	int[] textWidths;
	
	//Power management
	WindowManager.LayoutParams layoutParams;
	/*
	PowerManager pm;
	WakeLock wakeLock;
	KeyguardManager keyguardManager;
	KeyguardLock keyguardLock;
	*/
	//
	
	//MODULES
	private CalendarModule mCalendar;
	private CalendarModule.CalendarListener mCalendarListener;
	
	private NewsModule mNews;
	private NewsModule.NewsListener mNewsListener;
	
	private WeatherModule mWeather;
	private WeatherModule.WeatherListener mWeatherListener;
	
	private WODModule mWOD;
	private WODModule.WODListener mWODListener;
	
	private TimeModule mTime;
	private TimeModule.TimeListener mTimeListener;
	
	public static boolean isSleeping = false;
	
	private static int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	
	private Handler updateHandler = new Handler();
	private SystemUiHider mSystemUiHider;
	
	BrightnessSchedule bSchedule = new BrightnessSchedule();

	/*
	@Override
    protected void onPause() {
        // WHEN THE SCREEN IS ABOUT TO TURN OFF
        if (ScreenReceiver.wasScreenOn) {
            // THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A SCREEN STATE CHANGE
            System.out.println("SCREEN TURNED OFF");
        } else {
            // THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        }
        super.onPause();
    }
 
    @Override
    protected void onResume() {
        // ONLY WHEN SCREEN TURNS ON
        if (!ScreenReceiver.wasScreenOn) {
            // THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
            System.out.println("SCREEN TURNED ON");
        } else {
            // THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        }
        super.onResume();
    }
    */
	View contentView;
	
	@Override
	protected void onResume(){
		super.onResume();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		instance = this;
		
		contentView = findViewById(R.id.fullscreen_content);

        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		
        contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				delayedHide(0);
			}
		});
		
        /*
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        */
        
        density = getResources().getDisplayMetrics().density;
		
		timeValue = (TextView) findViewById(R.id.Time);
		secondsValue = (TextView) findViewById(R.id.Seconds);
		dateValue = (TextView) findViewById(R.id.Date);
		endingValue = (TextView) findViewById(R.id.Ending);
		monthValue = (TextView) findViewById(R.id.Month);
		tempValue = (TextView) findViewById(R.id.Temperature);
		curTempValue = (TextView) findViewById(R.id.CurTemperature);
		wordOfDay = (TextView) findViewById(R.id.WordOfDay);
		wordDefinitions = (TextView) findViewById(R.id.Definition);
		weatherImg = (ImageView)findViewById(R.id.WeatherIcon);
		newsHeading = (TextView)findViewById(R.id.NewsHeading);
		newsSummary = (TextView)findViewById(R.id.NewsSummary);
		calendarValue = (TextView)findViewById(R.id.Calendar);
		
		lineImg = (ImageView)findViewById(R.id.BounceLine);
		
		tail = (ImageView)findViewById(R.id.Tail2);
		ball = (ImageView)findViewById(R.id.Ball);
		
		overlay = (FrameLayout)findViewById(R.id.overlay);
		
	    //WebView wV = (WebView)findViewById(R.id.LoadingGif);
	    //wV.loadDataWithBaseURL("file:///android_asset/", loadData("meditation.gif", 250) , "text/html", "utf-8",null);
	    
		delayedHide(10);
		instance = this;
		isSleeping = false;
		
		Setup();
		PostSetup();
	}
	
	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		instance = this;
		
		contentView = findViewById(R.id.fullscreen_content);

        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		
        contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				delayedHide(0);
			}
		});
        
        density = getResources().getDisplayMetrics().density;
		
		timeValue = (TextView) findViewById(R.id.Time);
		secondsValue = (TextView) findViewById(R.id.Seconds);
		dateValue = (TextView) findViewById(R.id.Date);
		endingValue = (TextView) findViewById(R.id.Ending);
		monthValue = (TextView) findViewById(R.id.Month);
		tempValue = (TextView) findViewById(R.id.Temperature);
		curTempValue = (TextView) findViewById(R.id.CurTemperature);
		wordOfDay = (TextView) findViewById(R.id.WordOfDay);
		wordDefinitions = (TextView) findViewById(R.id.Definition);
		weatherImg = (ImageView)findViewById(R.id.WeatherIcon);
		newsHeading = (TextView)findViewById(R.id.NewsHeading);
		newsSummary = (TextView)findViewById(R.id.NewsSummary);
		calendarValue = (TextView)findViewById(R.id.Calendar);
		
		lineImg = (ImageView)findViewById(R.id.BounceLine);
		
		tail = (ImageView)findViewById(R.id.Tail2);
		ball = (ImageView)findViewById(R.id.Ball);
		
		overlay = (FrameLayout)findViewById(R.id.overlay);
		
	    //WebView wV = (WebView)findViewById(R.id.LoadingGif);
	    //wV.loadDataWithBaseURL("file:///android_asset/", loadData("meditation.gif", 250) , "text/html", "utf-8",null);
	    
		delayedHide(10);
		instance = this;
		isSleeping = false;
		
		Setup();
		PostSetup();
		
	}
	*/
	
	
	void Setup(){
		cr = getContentResolver();
		
		mCalendar = new CalendarModule();
        mCalendarListener = new CalendarModule.CalendarListener() {
        	@Override
        	public void onCalendarUpdate(List<String> eventDetails) {
        		if (eventDetails.size() > 0) {
        			String newdetails = "";
        			for (int i = 0; i < eventDetails.size() && i < mCalendar.maxItems; i++) {
        				if (i > 0) newdetails += "\n";
        				newdetails += eventDetails.get(i);
        			}
        			calendarValue.setText(newdetails);
        			calendarValue.setVisibility(View.VISIBLE);
        		} else {
        			calendarValue.setVisibility(View.GONE);
        		}
        	}
        };
        
        mNews = new NewsModule();
        mNewsListener = new NewsModule.NewsListener() {
			@Override
			public void onNewsUpdate(ArrayList<NewsItem> newsDetails) {
				newsItemArray = newsDetails;
			}
		};
		
		mWeather = new WeatherModule();
		mWeatherListener = new WeatherModule.WeatherListener() {
			
			@Override
			public void onWeatherUpdate(String[] weatherDetails) {
				try {
					tempValue.setText(weatherDetails[1] + "°C | " + weatherDetails[2] + "°C");
					curTempValue.setText(weatherDetails[3] + "°C");
					weatherImg.setImageResource(GetResourceID(weatherDetails[0]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onSunsetUpdate(int hh, int mm) {
				bSchedule.sunsetH = hh;
				bSchedule.sunsetM = mm;
			}
		};
		
		mWOD = new WODModule();
		mWODListener = new WODModule.WODListener() {
			
			@Override
			public void onWODUpdate(String[] wodDetails) {
				wordOfDay.setText(wodDetails[0]);
				wordDefinitions.setText(wodDetails[1]);
			}
		};
		
		mTime = new TimeModule();
		mTimeListener = new TimeModule.TimeListener() {
			
			@Override
			public void onTimeUpdate(String t, String s, int hh, int mm, int ss) {
				/*
				if(firstLoop2){
					bSchedule.a = hh*3600+mm*60+ss + 10;
					bSchedule.b = hh*3600+mm*60+ss + 20;
					firstLoop2 = false;
				}
				*/
				
				bSchedule.SetBrightness(hh, mm, ss);
				
				if(isSleeping) return;
				
				timeValue.setText(t);
			    secondsValue.setText(s);
			    
			    if(mm == 5 && ss == 0){
					if(mWeather != null){
						WeatherModule.getWeatherEvents(mWeatherListener);
					}
					if(mNews != null){
						NewsModule.getNewsEvents(mNewsListener);
					}
			    }
			    
			}
			
			@Override
			public void onMilliUpdate(float mills){
				if(isSleeping) return;
				
				base = timeValue.getBaseline()-16;
				height = base-144;
			    diff = base-height;
			    startVelocity = 4*((double)diff/100f);
			    acceleration = -8*((double)diff/100f);
				
			    float milli = mills-0.05f;
			    milli = milli < 0 ? 1+milli : milli;
				float delayTime = milli-0.1f;
				delayTime = delayTime < 0 ? 1+delayTime : delayTime;
				
				double h1 = (startVelocity*milli + 0.5f*acceleration*milli*milli)*100f;
				double h2 = (startVelocity*delayTime + 0.5f*acceleration*delayTime*delayTime)*100f;
				
			    
			    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(16, 16);
			    
			    lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
			    lp.setMargins(0, base-(int)h1, 0, 0);
			    ball.setLayoutParams(lp);
			    
			    tail.setPivotY(0);
			    
			    double tailSize = (h1-h2)/32f;
			    tail.setScaleY((float) tailSize);
			    lp = new FrameLayout.LayoutParams(16, 32);
			    lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
			    lp.setMargins(0, (base-(int)h1)+8, 0, 0);
			    tail.setLayoutParams(lp);
			    
			    float val = 20*mills;
			    if(val > 1){
			    	val = (20-val)/19f;
			    }
			    lineImg.setScaleX(val);
				
			}
			
			@Override
			public void onMagnitudeUpdate(int magnitude) {
				if(isSleeping) return;
				
				int width = ((FrameLayout)findViewById(R.id.ballanimcontainer)).getMeasuredWidth()/2;
				
				target = width + textWidths[magnitude-1];// + GetSize(magnitude);
				lineImg.setPivotX(target);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(target, 1);
			    lp.gravity = Gravity.TOP | Gravity.END;
			    lp.setMargins(0, 0, width, 0);
			    lineImg.setLayoutParams(lp);
			}
			
			@Override
			public void onDateUpdate(String[] d) {
				if(isSleeping) return;
				dateValue.setText(d[0]);
				endingValue.setText(d[1]);
				monthValue.setText(d[2]);
			}
		};
		
		textWidths = SetupSizes();
		updateHandler.post(Update);
	}
	
	int[] SetupSizes(){
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
	
	void PostSetup(){
		if(mCalendar != null){
        	CalendarModule.getCalendarEvents(cr, mCalendarListener);
        }
		
		if(mNews != null){
			NewsModule.getNewsEvents(mNewsListener);
		}
		
		if(mWeather != null){
			WeatherModule.getWeatherEvents(mWeatherListener);
			WeatherModule.getSunset(mWeatherListener);
		}
		
		if(mWOD != null){
			WODModule.getWODEvents(mWODListener);
		}
		
		if(mTime != null){
        	TimeModule.getTime(mTimeListener);
        }
        
		
		firstLoop = true;
		newsIndex = -1;
		colour = 0;
		fadeDirection = 1;
		updateHandler.post(colourLoop);
		
	}
	
	public void Sleep(boolean b){
		isSleeping = b;
		try {
			SetBright(0.1f);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SetOverlay(b ? 255 : 0);
		
		if(!b){
			PostSetup();
		}
	}
	
	void SetBright(float f) throws SettingNotFoundException {
	    int brightnessMode = Settings.System.getInt(getContentResolver(),
	            Settings.System.SCREEN_BRIGHTNESS_MODE);
	    if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
	        Settings.System.putInt(getContentResolver(),
	                Settings.System.SCREEN_BRIGHTNESS_MODE,
	                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	    }
	    
	    layoutParams = getWindow().getAttributes();
	    layoutParams.screenBrightness = f;
	    getWindow().setAttributes(layoutParams);
	}
	
	private Runnable Update = new Runnable(){
		public void run(){
			if (mTime != null){
	        	TimeModule.getTime(mTimeListener);
	        }

			updateHandler.post(this);
		}
	};
	
	/*
	private Runnable Update5s = new Runnable(){
		public void run(){
			Sleep(!isSleeping);
			updateHandler.postDelayed(this, 2000);
		}
	};
	*/
	
	void SetVisibility(LinearLayout l, int vis){
		int count = l.getChildCount();
		View v = null;
		for(int i = 0; i < count; i++) {
			v = l.getChildAt(i);
			v.setVisibility(vis);
		}
	}
	
	private Runnable switchNews = new Runnable(){
		public void run(){
			fadeDirection = -1;
		}
	};
	
	private Runnable colourLoop = new Runnable(){
		public void run(){
			if(isSleeping){
				return;
			}
			
			if(newsItemArray == null || newsItemArray.size() == 0){
				newsHeading.setText("Nothing to display");
				newsSummary.setText("");
				updateHandler.postDelayed(colourLoop, 1000);
			}else{
				if(colour > 255){
					colour = 255;
				}else if(colour < 0){
					colour = 0;
				}
				newsHeading.setTextColor(Color.rgb(colour, colour, colour));
				newsSummary.setTextColor(Color.rgb(colour, colour, colour));
				
				
				if(colour == 0){
					fadeDirection = 1;
					newsIndex ++;
					newsIndex %= newsItemArray.size();
					
					newsHeading.setText(newsItemArray.get(newsIndex).Title);
					String s = newsItemArray.get(newsIndex).Summary;
					
					newsSummary.setText(s);
					LinearLayout newsBracket = (LinearLayout)findViewById(R.id.NewsBracket);
					boolean shortened = false;
					
					while(s.length() > 1 && GetHeight(newsHeading, newsBracket.getWidth()) + GetHeight(newsSummary, newsBracket.getWidth()) > newsBracket.getHeight()){
						shortened = true;
						s = s.substring(0, s.length()-1);
						newsSummary.setText(s);
					}
					
					if(shortened && s.length()>3){
						s = s.substring(0, s.length()-3);
						s += "...";
						newsSummary.setText(s);
					}
				}
				
				colour += fadeDirection;
				
				if(colour == 255){
					updateHandler.postDelayed(switchNews, 10000);
				}
				
				updateHandler.post(this);
			}
		}
	};
	
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
	
	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};
	
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	public void SetOverlay(int i){
		overlay.setBackgroundColor(Color.argb(i, 0, 0, 0));
	}
	
	public int GetResourceID(String s){
		String correctedString = s;
		correctedString = correctedString.toLowerCase(Locale.UK);
		correctedString = correctedString.replace(" ", "_");
		
		try {
			return this.getResources().getIdentifier(correctedString, "drawable", this.getPackageName());
		} catch (Exception e) {
			return R.drawable.nullimage;
		}
	}
	
	public String loadData(String gif_url, int scale){
		return "<!DOCTYPE html><html><body style=\"margin: 0; padding: 0\"><img src=\"" + gif_url + "\" width=\"" + scale + "\" height=\""+scale+"\"></body></html>";
	}
}

