package com.example.foto_rosto;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import com.example.foto_rosto.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ShareActionProvider;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ShareActivity extends Activity {

	private Intent camera;
	private boolean wasExecuted;
	private String localFoto;
	private File imgFile;
	private Uri uri;
	SeekBar sbSaturation, sbContrast, sbBrightness;
	
	private Context m_context;
	
	
	boolean executarCrop = false;
	
	ImageView myPhoto;
	
	
//	private Button btn_choose;
	private ImageView img_shower;
	
	private static final int PHOTO_PICKED_WITH_DATA = 0x10;
	
	private static final int PHOTO_CROP_DATA = 0x11;
	
	protected static final int SHOW_LOAD_DIALOG = 0x201;
	
	protected static final int DISMISS_LOAD_DIALOG = 0x202;
	
	private ProgressDialog myLoadProgressDialog;
	
	private Handler myViewhandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_LOAD_DIALOG:
				myLoadProgressDialog = new ProgressDialog(m_context);
				myLoadProgressDialog
						.setProgressStyle(ProgressDialog.STYLE_SPINNER);//

				myLoadProgressDialog
						.setMessage("loading image please wait ...");
				myLoadProgressDialog.setIndeterminate(false);
				myLoadProgressDialog.show();
				break;
			case DISMISS_LOAD_DIALOG:
				myLoadProgressDialog.dismiss();
				break;
			default:
				break;
			}

		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		camera = new Intent(getApplicationContext(), CameraActivity.class);
        wasExecuted = false;
        
		Intent iin = getIntent();
	    Bundle extras = iin.getExtras();
	    localFoto = (String) extras.getString("Foto_Local");
	    
	    Log.d("LOCAL FOTO", "LOCAL FOTO: " + localFoto);
	    
	    setPhoto();
	    setTools();
	}
	
	private void setPhoto(){
		
		Log.i("local", "LOCAL: " + localFoto);
	  
	    imgFile = new File(localFoto);
	    if(imgFile.exists()){

	    	Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

	    	myPhoto = (ImageView) findViewById(R.id.ImageFoto);
	    	myPhoto.setImageBitmap(myBitmap);
       
	    }
	    

		m_context = ShareActivity.this;

		findViews();
		setListeners();	 
	}
	
	private void setTools(){
		
		// Set the saturation bar
		sbSaturation = (SeekBar)findViewById(R.id.sbSaturation);
		sbSaturation.setOnSeekBarChangeListener(saturationSeekBarChangeListener);
		
		// Set the contrast bar
		sbContrast = (SeekBar)findViewById(R.id.sbContrast);
		sbContrast.setOnSeekBarChangeListener(contrastSeekBarChangeListener);
		
		// Set the brightness bar
		sbBrightness = (SeekBar)findViewById(R.id.sbBrightness);
		sbBrightness.setOnSeekBarChangeListener(brightnessSeekBarChangeListener);
		
	}	
	
	/** findViewById */
	private void findViews() {
//		btn_choose = (Button) findViewById(R.id.btn_choosePic);
		img_shower = (ImageView) findViewById(R.id.ImageFoto);

	}	
	
	private void setListeners() {
		
		if(executarCrop == true){
			pickPhotoFromGallery();
		}
		
//		btn_choose.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				pickPhotoFromGallery();
//			}
//		});
	}
	
// ________________________________________________________________________
// ________________________________________________________________________
// ________________________________________________________________________
// ________________________________________________________________________
	
	Bitmap bitmapSaturationMaster;
	
	OnSeekBarChangeListener saturationSeekBarChangeListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			loadBitmapSat();
		}
	};	
	
	private void loadBitmapSat() {
		
		if(bitmapSaturationMaster == null){
			bitmapSaturationMaster = (Bitmap)((BitmapDrawable)myPhoto.getDrawable()).getBitmap();
		}
		int progressSat = sbSaturation.getProgress();

		// Saturation: 0 = gray-scale, 1 = identity
		float sat = (float) progressSat / 256;
		System.out.println("Saturation: " + String.valueOf(sat));
		myPhoto.setImageBitmap(updateSat(bitmapSaturationMaster, sat));
	}	
	
	Bitmap bitmapContrastMaster;
	
	OnSeekBarChangeListener contrastSeekBarChangeListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			loadBitmapContrast();
		}
	};
	
	private void loadBitmapContrast() {
		
		if(bitmapContrastMaster == null){
			bitmapContrastMaster = (Bitmap)((BitmapDrawable)myPhoto.getDrawable()).getBitmap();
		}
		int progress = sbContrast.getProgress();
		float contrast = (float)progress;
		System.out.println("Contrast: " + String.valueOf(contrast));
		myPhoto.setImageBitmap(changeBitmapContrastBrightness(bitmapContrastMaster, contrast, 0));
	}
	
	OnSeekBarChangeListener brightnessSeekBarChangeListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			loadBitmapBrightness();
		}
	};	
	
	Bitmap bitmapBrightnessMaster;
	
	private void loadBitmapBrightness() {
		
		if(bitmapBrightnessMaster == null){
			bitmapBrightnessMaster = (Bitmap)((BitmapDrawable)myPhoto.getDrawable()).getBitmap();
		}
		int progress = sbBrightness.getProgress();
		float brightness = (float)progress - 255;
		System.out.println("Brightness: " + String.valueOf(brightness));
		myPhoto.setImageBitmap(changeBitmapContrastBrightness(bitmapBrightnessMaster, 1, brightness));
	}	
	
	private Bitmap updateSat(Bitmap src, float settingSat) {

		int w = src.getWidth();
		int h = src.getHeight();

		Bitmap bitmapResult = 
				Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvasResult = new Canvas(bitmapResult);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(settingSat);
		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
		paint.setColorFilter(filter);
		canvasResult.drawBitmap(src, 0, 0, paint);

		return bitmapResult;
	}	
	
	public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
	{
	    ColorMatrix cm = new ColorMatrix(
    		new float[]{
	                contrast, 0, 0, 0, brightness,
	                0, contrast, 0, 0, brightness,
	                0, 0, contrast, 0, brightness,
	                0, 0, 0, 1, 0
            }
		);

	    Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

	    Canvas canvas = new Canvas(ret);

	    Paint paint = new Paint();
	    paint.setColorFilter(new ColorMatrixColorFilter(cm));
	    canvas.drawBitmap(bmp, 0, 0, paint);

	    return ret;
	}	

// ________________________________________________________________________
// ________________________________________________________________________
// ________________________________________________________________________
// ________________________________________________________________________
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.share, menu);

		MenuItem shareItem = (MenuItem) menu.findItem(R.id.action_share);
		ShareActionProvider mShare = (ShareActionProvider)shareItem.getActionProvider();

		shareImage();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

		mShare.setShareIntent(shareIntent);

		return true;
	}
	
    public void shareImage() {
    	
    	Log.i("share", "share");
    	
//    	salvarfoto();
    	
    	
    	BitmapDrawable drawable = (BitmapDrawable) myPhoto.getDrawable();
//    	Bitmap shareBitmap = Bitmap.createBitmap(myPhoto.getDrawingCache());
    	Bitmap shareBitmap = drawable.getBitmap();
    	
    	String filePath = saveToExternalStorage(this, shareBitmap);
  	
    	File file = new File(filePath);
    	
  		uri = Uri.fromFile(file);
    }	

    public void salvarfoto(){
    	Bitmap bitmapView = transformImgToBit();
    	saveBitmapToSDcard(localFoto, bitmapView);
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("share 3", "share 3");
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Log.i("share 2", "share2");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void onBackPressed(){
        ShareActivity.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        getParent().finish();
    }

//  ________________________________________________________________________
//  ________________________________________________________________________
//  ________________________________________________________________________
//  ________________________________________________________________________
    
    private Bitmap transformImgToBit(){
    	
    	BitmapDrawable drawable = (BitmapDrawable) myPhoto.getDrawable();
    	Bitmap bitmap = drawable.getBitmap();
    	return bitmap;
    	
    }
    
	private void saveBitmapToSDcard(String strPath, Bitmap bitmap) {
		// TODO Auto-generated method stub
		File f = new File(strPath);
		
		Log.i("STRPATH", "STRPATH:" + strPath);

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}    
    
	
	public String saveToExternalStorage(Context context, Bitmap bitmapImage){
		File album = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES),getResources().getString(R.string.app_name));
	    if(!album.exists()){
	    	 if (!album.mkdirs()) {
	 	        Log.e("ActivityHome", "Album directory not created");
	 	    }
	    }
	    
		try {
			File imageFile = new File(album, "image.png");
			
			Log.i("FILE", "file: " + imageFile );
			
			FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			bitmapImage.compress(CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
			return imageFile.getAbsolutePath();
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
		}
		return null;
	}	
    
//  ________________________________________________________________________
//  ________________________________________________________________________
//  ________________________________________________________________________
//  ________________________________________________________________________

	
	private void pickPhotoFromGallery() {
		
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType("image/jpeg");
//		intent.putExtra("return-data", true);
		
//		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		
		activityvai();
	}
	
	private void activityvai(){

		Intent intent = new Intent(this,
				name.zhaoweihua.crop.CropImage.class);

//		String local = "/mnt/sdcard/Pictures/Mirror's Cam/IMG_20140811_101020.jpg";
		
		Bundle extras = new Bundle();

		extras.putString("circleCrop", "true");
		extras.putInt("aspectX", 200);
		extras.putInt("aspectY", 200);
		extras.putString("Local", localFoto);
		
//		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtras(extras);
		startActivityForResult(intent, PHOTO_CROP_DATA);
		
	}
	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK) {
//			switch (requestCode) {
//			case PHOTO_PICKED_WITH_DATA:
//
//				Uri uri = data.getData();
//				Intent intent = new Intent(this,
//						name.zhaoweihua.crop.CropImage.class);
//				Bundle extras = new Bundle();
//				extras.putString("circleCrop", "true");
//				extras.putInt("aspectX", 200);
//				extras.putInt("aspectY", 200);
//				intent.setDataAndType(uri, "image/jpeg");
//				intent.putExtras(extras);
//				startActivityForResult(intent, PHOTO_CROP_DATA);
//				break;
//			case PHOTO_CROP_DATA: // show back data
//
//				String srcData = data.getExtras().getString("data-src");
//				myViewhandler.sendEmptyMessage(SHOW_LOAD_DIALOG);
//				BitmapWorkerTask task = new BitmapWorkerTask(img_shower);
//				task.execute(srcData);
//
//			}
//		}
//
//	}

	public static Bitmap decodeBitmapFromFile(String src,

	int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions

		final BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(src, options);

		// Calculate inSampleSize

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// options.inPreferredConfig =Bitmap.Config.RGB_565;
		// Decode bitmap with inSampleSize set

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(src, options);

	}

	public static int calculateInSampleSize(

	BitmapFactory.Options options, int reqWidth, int reqHeight) {

		// Raw height and width of image

		final int height = options.outHeight;

		final int width = options.outWidth;

		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			if (width > height) {

				inSampleSize = Math.round((float) height / (float) reqHeight);

			} else {

				inSampleSize = Math.round((float) width / (float) reqWidth);

			}

		}

		return inSampleSize;

	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;

		// for define to close avoid warning leak.
		// AlertDialog ImageDialog;
		public BitmapWorkerTask(ImageView imageView) {

			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);

		}

		// Decode image in background.

		@Override
		protected Bitmap doInBackground(String... params) {

			final Bitmap bitmap = decodeBitmapFromFile(params[0], 300, 300);

			return bitmap;

		}

		// Once complete, see if ImageView is still around and set bitmap.

		@Override
		protected void onPostExecute(Bitmap bitmap) {

			if (imageViewReference != null && bitmap != null) {

				final ImageView imageView = imageViewReference.get();

				if (imageView != null) {

					Log.d("imageview------------------",
							"debug view image load");
					imageView.setImageBitmap(bitmap);
					myViewhandler.sendEmptyMessage(DISMISS_LOAD_DIALOG);
				}

			}

		}

	}    
    
}