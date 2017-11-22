package com.engineeristic.recruiter.myapp;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.util.BitmapLruCache;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class RecruiterApplication extends MultiDexApplication {

	private static RecruiterApplication mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private Tracker mTracker;
	private static final String PROPERTY_ID = "UA-60165416-3";
	private HurlStack stack;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		MultiDex.install(getApplicationContext());
		Utility.setContext(getApplicationContext());
		Utility.setCrittercismForLogging();
		MySharedPreference.getInstance().initPrefrenceManger(getApplicationContext());
	}
	public static RecruiterApplication getApplication() {
		return mInstance;
	}

	public RequestQueue getReqQueue() {
		if (mRequestQueue == null) {
			stack = new HurlStack(null, createSslSocketFactory());
			mRequestQueue = Volley.newRequestQueue(getApplicationContext(), stack);
			//mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getReqQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue, new BitmapLruCache());
		}
		return this.mImageLoader;
	}

	public enum TrackerName {
		APP_TRACKER, GLOBAL_TRACKER, ECOMMERCE_TRACKER,
	}

	public HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	public synchronized Tracker getTracker(TrackerName appTracker) {
		if (!mTrackers.containsKey(appTracker)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (appTracker == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (appTracker == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.ecommerce_tracker);
			mTrackers.put(appTracker, t);
		}
		return mTrackers.get(appTracker);
	}

	private static SSLSocketFactory createSslSocketFactory() {
		TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}
		}};

		SSLContext sslContext = null;
		SSLSocketFactory sslSocketFactory = null;
		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, byPassTrustManagers, new SecureRandom());
			sslSocketFactory = sslContext.getSocketFactory();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		return sslSocketFactory;
	}

	/*synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// Setting mTracker to Analytics Tracker declared in our xml Folder
			mTracker = analytics.newTracker(R.xml.app_tracker);
		}
		return mTracker;
	}*/

}
