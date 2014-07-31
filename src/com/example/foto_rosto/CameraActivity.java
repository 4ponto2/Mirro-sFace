package com.example.foto_rosto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CameraActivity extends Activity {

	    static Camera mCamera;
	    private Camera_preview mPreview;
	    private FrameLayout preview;
	    private Intent ShareActivity;
	    
	    private String local_foto;
	    
	    public static int orientation;
		public static final int HORIZONTAL=0;
		public static final int VERTICAL=1;
		public int w,h;       	
	    
	    public Button btnVoltar;
	    
	    private int opcao, numerofoto;
	    
	    private ImageView img_rosto;
	    
	    WindowManager.LayoutParams layoutParams;
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        
		        try {
		        	toggleAutoBrightness();
    		    } catch (SettingNotFoundException e) {
    		        // TODO Auto-generated catch block
    		        e.printStackTrace();
    		    }
		        
		        getWindow().setFormat(PixelFormat.TRANSLUCENT);
		        requestWindowFeature(Window.FEATURE_NO_TITLE);
		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		        
		        setContentView(R.layout.activity_camera);
		        w=h=0;
		        
		        ShareActivity = new Intent(getApplicationContext(), ShareActivity.class);
		   
		        // Create an instance of Camera
		        mCamera = getCameraInstance();
		      
		        // Create our Preview view and set it as the content of our activity.
		        mPreview = new Camera_preview(this, mCamera);
		        preview = (FrameLayout) findViewById(R.id.camera_preview);
		        preview.addView(mPreview);
		        
		        orientation=1;
		        	   
		        init();
		    }
	    
	    private void init(){
	    	
	    	opcao = 0;
	    	numerofoto = 0;
	    	
	        Intent iin = getIntent();
	        Bundle extras = iin.getExtras();
	        opcao = (int) extras.getInt("Rosto");
	        numerofoto = (int) extras.getInt("img_n");
	        Log.i("PARTE ROSTO", "PARTE:" + opcao);
	        Log.i("PARTE ROSTO", "FOTO:" + numerofoto);
	        
	        setImage();
	        
	    }
			
	    protected void onResume() {
				super.onResume();
		}
		
	    private void TirarFoto(){
//		    	mCamera.takePicture(null, null, mPicture);
		        mCamera.takePicture(shutterCallback, null, mPicture);        
		}
		
	    ShutterCallback shutterCallback = new ShutterCallback() {
	    	public void onShutter() {
	    		Log.d("FOTO", "onShutter'd");
	    	}
		};
			    
			
		PictureCallback mPicture = new PictureCallback(){
			public void onPictureTaken(byte[] data, Camera camera) {
				try{
					Log.d("FOTO", "onPictureTaken");
					salvafoto(data,camera);
					releaseCamera();
					chamaShare();
						
				}catch(Error e){
					Log.d("FOTO ERRO", "ERRO");
					releaseCamera();
					
				}
			}
		};
			
		
		public void salvafoto(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();
		    Log.i("LOCAL", "MEDIAFILE: " + pictureFile);
		    if (pictureFile == null) {
		    	return;
		    }
		    try {
		    	FileOutputStream fos = new FileOutputStream(pictureFile);
		        fos.write(data);
		        fos.close();
		        releaseCamera();
		        local_foto = pictureFile.toString();
		        Log.i("LOCAL_FOTO", "TO STRING: " + local_foto);
		    } catch (FileNotFoundException e) {

		    } catch (IOException e) {
		    
		    }
		}
		    
		private static File getOutputMediaFile() {
			File mediaStorageDir = new File(
					Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Mirror's Cam");
		    if (!mediaStorageDir.exists()) {
		    	if (!mediaStorageDir.mkdirs()) {
			        Log.d("MyCameraApp", "failed to create directory");
		            return null;
		        }
		    }
		    // Create a media file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
		    File mediaFile;
		    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		    return mediaFile;
		}		    
		
		private void releaseCamera() {
			if (mCamera != null){
				mCamera.release();
				mCamera = null;
			}
		} 
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			int action = event.getAction();
			int keyCode = event.getKeyCode();
			switch (keyCode) {
			
				case KeyEvent.KEYCODE_VOLUME_UP:
					Log.i("BOTAO", "key event up");
		            if (action == KeyEvent.ACTION_DOWN) {
		                    //TODO
		            	TirarFoto();
		            }
		            return true;
		        
				case KeyEvent.KEYCODE_VOLUME_DOWN:
		        
					Log.i("BOTAO", "key event down");
		            
					if (action == KeyEvent.ACTION_DOWN) {
		            
						//TODO
		                
						TirarFoto();
		                
					}
					return true;
		            
				default:        
					return super.dispatchKeyEvent(event);
		    }
		}    	    
		    
		public static Camera getCameraInstance(){
			Camera c = null;
		    try {
		    	c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
		            //c = Camera.open(); // attempt to get a Camera instance
		    }
		    catch (Exception e){
		        	
		    }
		    return c;
		    
		}
		    
		
		public void chamaShare(){
			ShareActivity.putExtra("Foto_Local", local_foto);
		    startActivity(ShareActivity);	        
		}		    
		    
		
		private void toggleAutoBrightness() throws SettingNotFoundException {

			int brightnessMode = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE);
		    	
		    if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
		    	Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		    }
		    	
		    layoutParams = getWindow().getAttributes();
		    layoutParams.screenBrightness = 0.18F;
		    getWindow().setAttributes(layoutParams);
		    
		}
		
		private void setImage(){
			
			img_rosto = (ImageView) findViewById(R.id.rosto);
			
			switch (opcao) {
			
			// OLHOS
			case 1:
				
				// ver qual imagem aparece 
				switch (numerofoto) {
				case 1:
					img_rosto.setImageResource(R.drawable.olhos1);
					break;
					
				case 2:
					img_rosto.setImageResource(R.drawable.olhos2);
					break;
					
				case 3:
					img_rosto.setImageResource(R.drawable.olhos3);
					break;
					
				case 4:
					img_rosto.setImageResource(R.drawable.olhos4);
					break;
					
				case 5:
					img_rosto.setImageResource(R.drawable.olhos5);
					break;
					
				case 6:
					img_rosto.setImageResource(R.drawable.olhos6);
					break;
					
				case 7:
					img_rosto.setImageResource(R.drawable.olhos7);
					break;
					
				case 8:
					img_rosto.setImageResource(R.drawable.olhos8);
					break;

				default:
					break;
				}
				
				
				break;
				
			// NARIZ
				
			case 2:
				
				break;
				
			case 3:
				
				break;

			default:
				break;
			}
			
		}
			
	}