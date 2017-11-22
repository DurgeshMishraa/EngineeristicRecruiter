package com.engineeristic.recruiter.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.crittercism.app.CrittercismConfig;
import com.engineeristic.recruiter.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
	public static Toast mToast;
	public static Context mApplicationContext;
	public static String mCookie;
	public static Context mContext;
	public static ProgressDialog pDialog;

	public static void showLoadingDialog(Activity mActivity){
		if(pDialog == null && !mActivity.isFinishing()) {
			pDialog = new ProgressDialog(mActivity);
			pDialog.setMessage(mActivity.getResources().getString(R.string.loading_lbl));
			pDialog.setCancelable(false);
			pDialog.show();
		}
	}
	public static void dismisLoadingDialog(){
		if (pDialog != null	&& pDialog.isShowing()){
			pDialog.dismiss();
			pDialog = null;
		}
	}
	public static boolean isNetworkAvailable(Context mContext)
	{
		boolean isConnected = false;
		try{

			if(NetworkUtils.isMobileConnected(mContext)
					|| NetworkUtils.isWifiConnected(mContext)){
				isConnected = true;
			}
			if (!isConnected)
			{
				showToastMessage(mContext.getApplicationContext(), mContext.getResources().getString(R.string.msg_no_network), Toast.LENGTH_SHORT);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isConnected;
	}


	public static boolean isNetworkAvailableRelative(Context mContext, RelativeLayout rl_coordinator)
	{
		boolean isConnected = false;
		try{

			if(NetworkUtils.isMobileConnected(mContext)
					|| NetworkUtils.isWifiConnected(mContext)){
				isConnected = true;
			}
			if (!isConnected)
			{
				Snackbar snackbar = Snackbar.make(rl_coordinator, mContext.getResources().getString(R.string.msg_no_network), Snackbar.LENGTH_LONG);
				snackbar.show();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isConnected;
	}
	public static boolean isNetworkAvailableLinear(Context mContext, LinearLayout ll_coordinator)
	{
		boolean isConnected = false;
		try{

			if(NetworkUtils.isMobileConnected(mContext)
					|| NetworkUtils.isWifiConnected(mContext)){
				isConnected = true;
			}
			if (!isConnected)
			{
				Snackbar snackbar = Snackbar.make(ll_coordinator, mContext.getResources().getString(R.string.msg_no_network), Snackbar.LENGTH_LONG);
				snackbar.show();			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isConnected;
	}

	public static boolean isNetworkAvailableCordinator(Context mContext, CoordinatorLayout coordinator)
	{
		boolean isConnected = false;
		try{

			if(NetworkUtils.isMobileConnected(mContext)
					|| NetworkUtils.isWifiConnected(mContext)){
				isConnected = true;
			}
			if (!isConnected)
			{
				Snackbar snackbar = Snackbar.make(coordinator, mContext.getResources().getString(R.string.msg_no_network), Snackbar.LENGTH_LONG);
				snackbar.show();			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isConnected;
	}


	public static void setContext(Context context) {
		mApplicationContext = context.getApplicationContext();
	}
	public static Context getContext()
	{
		return mApplicationContext;
	}
	public static String getAppVersionName() {
		try {
			PackageInfo packageInfo = mApplicationContext.getPackageManager().getPackageInfo(mApplicationContext.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static void setCrittercismForLogging() {
		String userName = getUserName();
		if(mApplicationContext!=null)
		{
			CrittercismConfig config = new CrittercismConfig();
			boolean shouldIncludeVersionCode = true;
			boolean shouldCollectLogcat = true;
			boolean delaySendingAppLoad = true;
			String myCustomVersionName = getAppVersionName();
			// set the custom version name.
			config.setCustomVersionName(myCustomVersionName);
			// Set the custom version name.
			config.setVersionCodeToBeIncludedInVersionString(shouldIncludeVersionCode);
			// initialize.
			// instantiate metadata json object
			JSONObject metadata = new JSONObject();
			try {
				if(userName != null){
					metadata.put("name",userName);
				}
				else{
					metadata.put("name","User Without Login");
				}
				metadata.put("delaySendingAppLoad", delaySendingAppLoad); // send
				metadata.put("customVersionName", myCustomVersionName);
				metadata.put("shouldCollectLogcat", shouldCollectLogcat); // send
				metadata.put("includeVersionCode", shouldIncludeVersionCode);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Crittercism.logHandledException(e);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			Crittercism.setMetadata(metadata);
			Crittercism.initialize(mApplicationContext,"b252c4bf1cf74ef1bc2df09be7347b5d00555300",
					config);
			if(userName != null){
				Crittercism.setUsername(userName);
			}
			else{
				Crittercism.setUsername("Not Login");
			}
		}
	}
	public final static boolean isValidEmail(CharSequence target){
		if (target == null){
			return false;
		}
		else{
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public static int getAppVersion() {
		try {
			PackageInfo packageInfo = mApplicationContext.getPackageManager()
					.getPackageInfo(mApplicationContext.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	private static SharedPreferences initUserInfo(){
		if(mApplicationContext != null){
			SharedPreferences mSharedPreference = mApplicationContext.getSharedPreferences("Cookies", Context.MODE_PRIVATE);
			return mSharedPreference;
		}else
			return null;
	}
	private static String getUserName(){
		final SharedPreferences nSharedPreference = initUserInfo();
		if(nSharedPreference != null){
			return nSharedPreference.getString("userName", null);
		}else{
			return null;
		}
	}
	public static String getItemSelection(List<String> jobSelList){
		String temp = "";
		if(jobSelList != null && jobSelList.size() > 0) {
			for (String item : jobSelList)
				temp = temp + item + ", ";
			temp = temp.substring(0, temp.length() - 2);
		}
		return temp;
	}
	public static Map<String,String> params(){
		Map<String,String> params = new HashMap<String, String>();
		params.put("industryStr", MySharedPreference.getFilterType(Constant.FILTER_INDUSTRY_PARAMS, "0"));
		params.put("funcStr", MySharedPreference.getFilterType(Constant.FILTER_FUNCTIONAL_PARAMS, "0"));
		params.put("minexp", MySharedPreference.getFilterType(Constant.FILTER_MIN_EXPERIENCE_PARAMS, "-1"));
		params.put("maxexp", MySharedPreference.getFilterType(Constant.FILTER_MAX_EXPERIENCE_PARAMS, "-1"));
		params.put("instiStr", MySharedPreference.getFilterType(Constant.FILTER_INSTITUTE_PARAMS, "0"));
		params.put("locStr", MySharedPreference.getFilterType(Constant.FILTER_LOCATION_PARAMS, "0"));
		params.put("minbatch", MySharedPreference.getFilterType(Constant.FILTER_MIN_BATCH_PARAMS, "-1"));
		params.put("maxbatch", MySharedPreference.getFilterType(Constant.FILTER_MAX_BATCH_PARAMS, "-1"));
		params.put("minctc", MySharedPreference.getFilterType(Constant.FILTER_MIN_SALARY_PARAMS, "0"));
		params.put("maxctc", MySharedPreference.getFilterType(Constant.FILTER_MAX_SALARY_PARAMS, "0"));
		params.put("gender", MySharedPreference.getFilterType(Constant.FILTER_GENDER_PARAMS, "-1"));
		params.put("noticeperiod", MySharedPreference.getFilterType(Constant.FILTER_NOTICE_PERIOD_PARAMS, "0"));
		return params;
	}
	public static boolean isFilterSelected(){
		boolean isAvailable = false;

		if(!MySharedPreference.getFilterType(Constant.FILTER_INDUSTRY_PARAMS, "0").equals("0")
				||!MySharedPreference.getFilterType(Constant.FILTER_FUNCTIONAL_PARAMS, "0").equals("0")
				|| !MySharedPreference.getFilterType(Constant.FILTER_MIN_EXPERIENCE_PARAMS, "-1").equals("-1")
				|| !MySharedPreference.getFilterType(Constant.FILTER_MAX_EXPERIENCE_PARAMS, "-1").equals("-1")
				|| !MySharedPreference.getFilterType(Constant.FILTER_INSTITUTE_PARAMS, "0").equals("0")
				|| !MySharedPreference.getFilterType(Constant.FILTER_LOCATION_PARAMS, "0").equals("0")
				|| !MySharedPreference.getFilterType(Constant.FILTER_MIN_BATCH_PARAMS, "-1").equals("-1")
				|| !MySharedPreference.getFilterType(Constant.FILTER_MAX_BATCH_PARAMS, "-1").equals("-1")
				|| !MySharedPreference.getFilterType(Constant.FILTER_MIN_SALARY_PARAMS, "0").equals("0")
				|| !MySharedPreference.getFilterType(Constant.FILTER_MAX_SALARY_PARAMS, "0").equals("0")
				|| !MySharedPreference.getFilterType(Constant.FILTER_GENDER_PARAMS, "-1").equals("-1")
				|| !MySharedPreference.getFilterType(Constant.FILTER_NOTICE_PERIOD_PARAMS, "0").equals("0")
				){
			isAvailable  = true;
		}
		return isAvailable;
	}
	public static void showToastMessage(Context mContext, String nMessage, int nDuration){
		if(mToast != null
				&& mToast.getView() != null
				&& mToast.getView().isShown()){
			mToast.setText(nMessage);
		}else{
			mToast = Toast.makeText(mContext, nMessage, nDuration);
			mToast.setGravity(Gravity.CENTER, 5, 2);
			mToast.show();
		}
	}
	public static View getViewByPosition(final int position, final ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (position < firstListItemPosition || position > lastListItemPosition ) {
			return listView.getAdapter().getView(position, null, listView);
		} else {
			final int childIndex = position - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}
	public static File generatePicturePath() {
		try {
			File storageDir = getAlbumDir();
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String imageFileName = "IMG_" + timeStamp + "_";
			return File.createTempFile(imageFileName, ".jpg", storageDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "engineeristic Recruiter");
			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						return null;
					}
				}
			}
		} else {

		}
		return storageDir;
	}
	public static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	public static Bitmap decodeFile(String path) {
		int orientation;
		try {
			if (path == null) {
				return null;
			}
			// decode image size 
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 0;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeFile(path, o2);
			Bitmap bitmap = bm;
			ExifInterface exif = new ExifInterface(path);
			orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			Matrix m = new Matrix();
			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}
			else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);

				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
			return null;
		}

	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	public static String getPath(final Uri uri, final Context mContext) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(mContext, contentUri, null, null);
			} else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};
				return getDataColumn(mContext, contentUri, selection, selectionArgs);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(mContext, uri, null, null);
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} catch (Exception e) {

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 150;
		int targetHeight = 150;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,targetHeight,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth),
						((float) targetHeight)) / 2),
				Path.Direction.CCW);
		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap,
				new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight()),
				new Rect(0, 0, targetWidth,
						targetHeight), null);
		return targetBitmap;
	}
	public static String ReadFromfile(String fileName, Context context) {
		StringBuilder returnString = new StringBuilder();
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets()
					.open(fileName, Context.MODE_PRIVATE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while ((line = input.readLine()) != null) {
				returnString.append(line);
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
		return returnString.toString();
	}
	public static int getIndexInArray(String value, String dataArray[]){
		int index = -1;
		int p = 0;
		while(p < dataArray.length){
			if(value.equalsIgnoreCase(dataArray[p])){
				return p;
			}
			p++;
		}
		return index;
	}
	public static void cancelNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager)mApplicationContext.getSystemService(ns);
		//nMgr.cancel(notifyId);
		nMgr.cancelAll();
	}

	public static void cancelNotificationID(int id) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager)mApplicationContext.getSystemService(ns);
		nMgr.cancel(id);
	}
}
