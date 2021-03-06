package com.quatropdois.selfieSwap;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.quatropdois.selfieSwap.R;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class MyActivity extends FragmentActivity{
	
	public static DisplayMetrics metrics = null;
	public String flurryEventName = null;
	
	// UI elements
	public static View lTitleBar;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// add the menu items here
		return true;
	}
	
	protected void initialize(int id) {
		setContentView(id);
	}
	
	protected void initialize(int id, String title, int menuId) {
		setContentView(id);
		setActionBar(title, menuId);
		setMetrics();
	}
	
	public void setMetrics(){
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
	}
	
	public int getStatusBarHeight(){ 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	} 
	
	private void setActionBar(String title, int id) {
		lTitleBar = findViewById(id);
		if(title != null){
			com.quatropdois.selfieSwap.MyTextView tvTitle = 
				(com.quatropdois.selfieSwap.MyTextView)lTitleBar.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
		}
	}
	
	protected boolean isOnline() {
		try {
			ConnectivityManager cm = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} catch (Exception e) {
			return false;
		}
	}
	
	public Matrix getTransformationMatrix(Bitmap imageBitmap, int fitInWidth, int fitInHeight){
		Bitmap originalImage = imageBitmap;
		float originalWidth = originalImage.getWidth();
		float originalHeight = originalImage.getHeight();
		float scale = 1;
		float xTranslation  = 0.0f;
		float yTranslation  = 0.0f;
		
		if(fitInWidth <= fitInHeight){
			scale = fitInWidth/originalWidth;
			if(fitInHeight > originalHeight){
				yTranslation = (fitInHeight - originalHeight * scale)/2.0f;
			}
		}else{
			scale = fitInHeight/originalHeight;
			if(fitInWidth > originalWidth){
				xTranslation = (fitInWidth - originalWidth * scale)/2.0f;
			}
		}
		
		Matrix transformation = new Matrix();
		transformation.postTranslate(xTranslation, yTranslation);
		transformation.preScale(scale, scale);
		return transformation;
	}
	
	public String saveToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        
        // Path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        
        // Create imageDir
        File mypath=new File(directory,name);
        FileOutputStream fos = null;
        try{           
            fos = new FileOutputStream(mypath);
            
	        // Use the compress method on the BitMap object to write image to the OutputStream
	        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
	        fos.close();
	        
        }catch(Exception e){
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
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
}
