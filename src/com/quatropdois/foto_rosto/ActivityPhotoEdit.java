package com.quatropdois.foto_rosto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.example.foto_rosto.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class ActivityPhotoEdit extends MyActivity{
	
	// UI Elements
	RelativeLayout rlPhoto;
	ImageView ivPhotoOriginal;
	LinearLayout llShareDialog;
	ScrollView svShareDialog;
	Dialog shareDialog;
	SeekBar sbSaturation, sbContrast, sbBrightness; // sbRotateColor;
	
	// Variables
	private static final int SHARE_REQUEST = 1;
	boolean shareDialogCreated = false;
	boolean photoSet = false;
	
	private String localFoto;
	
	
	@Override
	public void onCreate(Bundle savedInstanceBundle) {
		super.onCreate(savedInstanceBundle);
		super.initialize(R.layout.activity_photo_edit, "COMPARTILHE",
			R.id.ShareTitleBar);
		
    	setAdMob();
		setPhoto();
		setTools();
	}
	
	private void setAdMob(){
		
		AdView adView = (AdView) findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder()
       .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .build();
	    adView.loadAd(adRequest);
	    
	}
	                                                                            
	@Override
	public void onResume(){
		super.onResume();
		if(!photoSet){
			setPhoto();
		}
		if(!shareDialogCreated){
			createShareDialog();
		}
	}
	
	@Override
	public void onStop(){
		super.onStop();
		photoSet = false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == SHARE_REQUEST) {
		}
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
		
		// Set the rotate on blue axis bar
//		sbRotateColor = (SeekBar)findViewById(R.id.sbRotateColor);
//		sbRotateColor.setOnSeekBarChangeListener(rotateColorSeekBarChangeListener);
		
	}
	
	private void setPhoto(){
		
		Intent iin = getIntent();
	    Bundle extras = iin.getExtras();
	    localFoto = (String) extras.getString("Foto_Local");
		
		String dirPath = localFoto;
//		dirPath = getIntent().getStringExtra(ActivityPhotoCrop.PHOTO_DIR_PATH_TAG);
//		Log.i("LOCAL: ", "LOCAL: " + dirPath);
//		String imageName = getIntent().getStringExtra(ActivityPhotoCrop.PHOTO_IMAGE_NAME_TAG);
//		Log.i("LOCAL: ", "LOCAL: " + imageName);
		ivPhotoOriginal = (ImageView)findViewById(R.id.ivPhotoOriginal);
		
		try{
			// Load photo from storage
//	        File f = new File(dirPath, imageName);
	        File f = new File(dirPath);
	        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
	        
	        // Resize the photo and imageview
	        int side = metrics.widthPixels;
	        
	        rlPhoto = (RelativeLayout)findViewById(R.id.rlPhoto);
	        rlPhoto.getLayoutParams().width = side;
	        rlPhoto.getLayoutParams().height = side;
	        rlPhoto.requestLayout();
	        
	        ivPhotoOriginal.getLayoutParams().width =  side;
	        ivPhotoOriginal.getLayoutParams().height =  side;
	        ivPhotoOriginal.setImageMatrix(getTransformationMatrix(b, side, side));
	        ivPhotoOriginal.requestLayout();
	        ivPhotoOriginal.setImageBitmap(b);
	        
	        photoSet = true;
	        
	    }catch (FileNotFoundException e){
	        e.printStackTrace();
	    }
	}
	
	// Creates custom share dialog for image sharing INSTEAD of chooser
	// Might make custom implementation for Facebook and Instagram since
	// Intent.EXTRA_TEXT is blocked so we cannot add hashtag and link
	private void createShareDialog(){
		
		// Inflate the layout
		LayoutInflater inflater = LayoutInflater.from(this);
		svShareDialog = (ScrollView)inflater.inflate(R.layout.share_dialog, null);
		llShareDialog = (LinearLayout)svShareDialog.findViewById(R.id.llShareDialogList);
		
		// Get available share image intents
	    List<Intent> targets = new ArrayList<Intent>();
	    Intent template = new Intent(Intent.ACTION_SEND);
	    template.setType("image/png");
	    List<ResolveInfo> candidates = this.getPackageManager().
		  queryIntentActivities(template, 0);
	    
	    for (ResolveInfo candidate : candidates) {
	    	
	    	Drawable appIcon = candidate.activityInfo.loadIcon(getPackageManager());
	    	final String appName = candidate.activityInfo.loadLabel(getPackageManager()).toString();
	    	final String packageName = candidate.activityInfo.packageName;
	    	
	    	if(appIcon != null && appName != null){
		    	RelativeLayout rlDialogListItem = (RelativeLayout)inflater.inflate(R.layout.dialog_list_item, null);
		        LinearLayout itemLayout = (LinearLayout)rlDialogListItem.findViewById(R.id.llDialogListItem);
		        ImageView itemImageView = (ImageView)rlDialogListItem.findViewById(R.id.ivDialogListItem);
		        TextView itemTextView = (TextView)rlDialogListItem.findViewById(R.id.tvDialogListItem);
	        
		        itemImageView.setImageDrawable(appIcon);
		        itemTextView.setText(appName);
		        itemLayout.setClickable(true);
		        itemLayout.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						onShareItem(appName, packageName);
					}
		        });
		        llShareDialog.addView(rlDialogListItem);
	    	}
	        
	    }
	    shareDialog = new Dialog(this);
	    shareDialog.setContentView(svShareDialog);
		shareDialog.setTitle("Share with...");
		shareDialogCreated = true;
	}
	
	private void onShareItem(String appName, String packageName){
		shareDialog.dismiss();
		
		// Get the image
		rlPhoto.setDrawingCacheEnabled(true);
		rlPhoto.setDrawingCacheQuality(RelativeLayout.DRAWING_CACHE_QUALITY_HIGH);
	    Bitmap shareBitmap = Bitmap.createBitmap(rlPhoto.getDrawingCache());
	    rlPhoto.setDrawingCacheEnabled(false);
	    
	    // Save image to sdcard
	    String filePath = saveToExternalStorage(this, shareBitmap);
    	File file = new File(filePath);
    	
    	// Adding text to facebook and instagram message is blocked
    	// separate implementations can be added here 
    	/*if(appName.equals("Facebook")){
    	}else if(appName.equals("Instagram")){
    	}else{*/
    		Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            //shareIntent.putExtra(Intent.EXTRA_TEXT, "http://axwellingrosso.com #OnMyWay");
            //shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Axwell \u039B Ingrosso Presents Departures"); // subject for email
            shareIntent.setType("image/png");
            shareIntent.setPackage(packageName);
            startActivityForResult(shareIntent, SHARE_REQUEST);
    	//}
	}
	
	public void onUp(View v){
		Log.i("btn", "btn");
		onBackPressed();
	}
	
    public void onBackPressed(){
        ActivityPhotoEdit.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        getParent().finish();
    }
	
	public void onShare(View v){
		shareDialog.show();
	}
	
	int colorIndex = 0;
	int [] colors = new int []{
			R.color.Dark_Seagreen, R.color.Deep_Coral, R.color.Hot_Seagreen, R.color.Mango_Sorbet
	};
	
	int filterIndex = 0;
	PorterDuff.Mode [] modes = new PorterDuff.Mode []{	
			Mode.ADD, Mode.DARKEN, Mode.MULTIPLY,
			Mode.OVERLAY, Mode.LIGHTEN, 
			Mode.SCREEN};
	
	public void updateImage(){
		
		// Reset Bitmap for saturation bar and contrast 
		bitmapSaturationMaster = null;
		bitmapContrastMaster = null;
		bitmapBrightnessMaster = null;
		bitmapRotateColorMaster = null;
		
		int color = getResources().getColor(colors[colorIndex]);
		System.out.println("Mode : " + modes[filterIndex].name());
		ivPhotoOriginal.setColorFilter(
			new PorterDuffColorFilter(color, modes[filterIndex]));
	}
	
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
			bitmapSaturationMaster = (Bitmap)((BitmapDrawable)ivPhotoOriginal.getDrawable()).getBitmap();
		}
		int progressSat = sbSaturation.getProgress();

		// Saturation: 0 = gray-scale, 1 = identity
		float sat = (float) progressSat / 256;
		System.out.println("Saturation: " + String.valueOf(sat));
		ivPhotoOriginal.setImageBitmap(updateSat(bitmapSaturationMaster, sat));
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
			bitmapContrastMaster = (Bitmap)((BitmapDrawable)ivPhotoOriginal.getDrawable()).getBitmap();
		}
		int progress = sbContrast.getProgress();
		float contrast = (float)progress;
		System.out.println("Contrast: " + String.valueOf(contrast));
		ivPhotoOriginal.setImageBitmap(changeBitmapContrastBrightness(bitmapContrastMaster, contrast, 0));
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
			bitmapBrightnessMaster = (Bitmap)((BitmapDrawable)ivPhotoOriginal.getDrawable()).getBitmap();
		}
		int progress = sbBrightness.getProgress();
		float brightness = (float)progress - 255;
		System.out.println("Brightness: " + String.valueOf(brightness));
		ivPhotoOriginal.setImageBitmap(changeBitmapContrastBrightness(bitmapBrightnessMaster, 1, brightness));
	}
	
	/**
	 * 
	 * @param bmp input bitmap
	 * @param contrast 0..10 1 is default
	 * @param brightness -255..255 0 is default
	 * @return new bitmap
	 */
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
	
	OnSeekBarChangeListener rotateColorSeekBarChangeListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			loadBitmapRotateColor();
		}
	};
	
	Bitmap bitmapRotateColorMaster = null;
	
	private void loadBitmapRotateColor() {
		
		if(bitmapRotateColorMaster == null){
			bitmapRotateColorMaster = (Bitmap)((BitmapDrawable)ivPhotoOriginal.getDrawable()).getBitmap();
		}
//		int progress = sbRotateColor.getProgress();

		// Saturation: 0 = gray-scale, 1 = identity
//		float degrees = (float) progress;
//		System.out.println("Rotate Color by : " + String.valueOf(degrees));
//		ivPhotoOriginal.setImageBitmap(updateSat(bitmapRotateColorMaster, degrees));
	}
	
}
