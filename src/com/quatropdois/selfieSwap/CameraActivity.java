package com.quatropdois.selfieSwap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;

import com.quatropdois.selfieSwap.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CameraActivity extends Activity {

	    static Camera mCamera;
	    private Camera_preview mPreview;
	    private FrameLayout preview;
//	    private Intent ShareActivity;
	    
	    private String local_foto;
	    
	    public static int orientation;
		public static final int HORIZONTAL=0;
		public static final int VERTICAL=1;
		public int w,h;       	
	    
	    public Button btnVoltar;
	    
	    private int opcao, numerofoto;
	    
	    private ImageView img_rosto;
	    
	    WindowManager.LayoutParams layoutParams;
	    
//	    ______________________________________
	    
		private Context m_context;
		
//		private Button btn_choose;
		private ImageView img_shower;
		
		private static final int PHOTO_PICKED_WITH_DATA = 0x10;
		
		private static final int PHOTO_CROP_DATA = 0x11;
		
		protected static final int SHOW_LOAD_DIALOG = 0x201;
		
		protected static final int DISMISS_LOAD_DIALOG = 0x202;
		
		RelativeLayout progressBar;
		
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
	    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        
		        getWindow().setFormat(PixelFormat.TRANSLUCENT);
		        requestWindowFeature(Window.FEATURE_NO_TITLE);
		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		        
		        setContentView(R.layout.activity_camera);
		        progressBar = (RelativeLayout) findViewById(R.id.ProgressBar);
		        
			    View decorView = getWindow().getDecorView();
				 // Hide both the navigation bar and the status bar.
				 // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
				 // a general rule, you should design your app to hide the status bar whenever you
				 // hide the navigation bar.
				 int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				               | View.SYSTEM_UI_FLAG_FULLSCREEN;
				 decorView.setSystemUiVisibility(uiOptions);


//		      progressBar.bringToFront();
		        
		        try {
		        	toggleAutoBrightness();
    		    } catch (SettingNotFoundException e) {
    		        // TODO Auto-generated catch block
    		        e.printStackTrace();
    		    }
		        
//		        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//		        requestWindowFeature(Window.FEATURE_NO_TITLE);
//		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		        
//		        setContentView(R.layout.activity_camera);
//		        progressBar = (RelativeLayout) findViewById(R.id.ProgressBar);
		        w=h=0;
		   
		        // Create an instance of Camera
		        mCamera = getCameraInstance();
		      
		        // Create our Preview view and set it as the content of our activity.
		        mPreview = new Camera_preview(this, mCamera);
		        preview = (FrameLayout) findViewById(R.id.camera_preview);
		        preview.addView(mPreview);
		        
		        orientation=1;
		        	   
		        init();
		        
				m_context = CameraActivity.this;
				
//				progressBar.setVisibility(View.INVISIBLE);
					
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
				progressBar.setVisibility(View.INVISIBLE);
				Log.i("rotation", "rotation:" + getResources().getConfiguration().orientation);
//				mostraMsg();
		}




	    private void mostraMsg(){

					// Creating alert Dialog with one Button

        	AlertDialog.Builder builder1 = new AlertDialog.Builder(CameraActivity.this);
        	builder1.setTitle("Title");
        	builder1.setMessage("my message");
        	builder1.setCancelable(true);
        	builder1.setNeutralButton(android.R.string.ok,
        	        new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int id) {
        	        dialog.cancel();
        	    }
        	});

        	AlertDialog alert11 = builder1.create();
        	alert11.show();

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
					setListeners();
//					chamaShare();
						
				}catch(Error e){
					Log.d("FOTO ERRO", "ERRO");
					releaseCamera();
					
				}
			}
		};
		
		private void setListeners() {
			
				pickPhotoFromGallery();
				
		}
		
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
		
		private void toggleAutoBrightness() throws SettingNotFoundException {

			int brightnessMode = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE);
		    	
		    if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
		    	Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		    }
		    	
		    layoutParams = getWindow().getAttributes();
		    layoutParams.screenBrightness = 0.25F;
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
					
				case 9:
					img_rosto.setImageResource(R.drawable.olho9);
					break;
					
				case 10:
					img_rosto.setImageResource(R.drawable.olho10);
					break;
					
				case 11:
					img_rosto.setImageResource(R.drawable.olho11);
					break;
					
				case 12:
					img_rosto.setImageResource(R.drawable.olho12);
					break;
					
				case 13:
					img_rosto.setImageResource(R.drawable.olho13);
					break;
					
				case 14:
					img_rosto.setImageResource(R.drawable.olho14);
					break;
					
				case 15:
					img_rosto.setImageResource(R.drawable.olho15);
					break;
					
				case 16:
					img_rosto.setImageResource(R.drawable.olho16);
					break;
					
				case 17:
					img_rosto.setImageResource(R.drawable.olho17);
					break;
					
				case 18:
					img_rosto.setImageResource(R.drawable.olho18);
					break;
					
				case 19:
					img_rosto.setImageResource(R.drawable.olho19);
					break;
					
				case 20:
					img_rosto.setImageResource(R.drawable.olho20);
					break;
					
				case 21:
					img_rosto.setImageResource(R.drawable.olhos21);
					break;
					
				case 22:
					img_rosto.setImageResource(R.drawable.olhos22);
					break;
					
				case 23:
					img_rosto.setImageResource(R.drawable.olhos23);
					break;
					
				case 24:
					img_rosto.setImageResource(R.drawable.olhos24);
					break;
					
				case 25:
					img_rosto.setImageResource(R.drawable.olhos25);
					break;
					
				case 26:
					img_rosto.setImageResource(R.drawable.olhos26);
					break;

				default:
					break;
				}
				
				
				break;
				
			// NARIZ
				
			case 2:
				
				// ver qual imagem aparece 
				switch (numerofoto) {
				case 1:
					img_rosto.setImageResource(R.drawable.nariz1);
					break;
					
				case 2:
					img_rosto.setImageResource(R.drawable.nariz2);
					break;
					
				case 3:
					img_rosto.setImageResource(R.drawable.nariz3);
					break;
					
				case 4:
					img_rosto.setImageResource(R.drawable.nariz4);
					break;
					
				case 5:
					img_rosto.setImageResource(R.drawable.nariz5);
					break;
					
				case 6:
					img_rosto.setImageResource(R.drawable.nariz6);
					break;
					
				case 7:
					img_rosto.setImageResource(R.drawable.nariz7);
					break;
					
				case 8:
					img_rosto.setImageResource(R.drawable.nariz8);
					break;

				case 9:
					img_rosto.setImageResource(R.drawable.nariz9);
					break;			

				case 10:
					img_rosto.setImageResource(R.drawable.nariz10);
					break;

				case 11:
					img_rosto.setImageResource(R.drawable.nariz11);
					break;

				case 12:
					img_rosto.setImageResource(R.drawable.nariz12);
					break;

				case 13:
					img_rosto.setImageResource(R.drawable.nariz13);
					break;

				case 14:
					img_rosto.setImageResource(R.drawable.nariz14);
					break;

				case 15:
					img_rosto.setImageResource(R.drawable.nariz15);
					break;

				case 16:
					img_rosto.setImageResource(R.drawable.nariz16);
					break;

				case 17:
					img_rosto.setImageResource(R.drawable.nariz17);
					break;

				case 18:
					img_rosto.setImageResource(R.drawable.nariz18);
					break;

				case 19:
					img_rosto.setImageResource(R.drawable.nariz19);
					break;

				case 20:
					img_rosto.setImageResource(R.drawable.nariz20);
					break;

				case 21:
					img_rosto.setImageResource(R.drawable.nariz21);
					break;

				case 22:
					img_rosto.setImageResource(R.drawable.nariz22);
					break;

				case 23:
					img_rosto.setImageResource(R.drawable.nariz23);
					break;

				case 24:
					img_rosto.setImageResource(R.drawable.nariz24);
					break;

				default:
					break;
				}
				
				break;
				
		// BOCA
				
			case 3:
				
				// ver qual imagem aparece 
				switch (numerofoto) {
				case 1:
					img_rosto.setImageResource(R.drawable.boca1);
					break;
					
				case 2:
					img_rosto.setImageResource(R.drawable.boca2);
					break;
					
				case 3:
					img_rosto.setImageResource(R.drawable.boca3);
					break;
					
				case 4:
					img_rosto.setImageResource(R.drawable.boca4);
					break;
					
				case 5:
					img_rosto.setImageResource(R.drawable.boca5);
					break;
					
				case 6:
					img_rosto.setImageResource(R.drawable.boca6);
					break;
					
				case 7:
					img_rosto.setImageResource(R.drawable.boca7);
					break;
					
				case 8:
					img_rosto.setImageResource(R.drawable.boca8);
					break;
					
				case 9:
					img_rosto.setImageResource(R.drawable.boca9);
					break;

				case 10:
					img_rosto.setImageResource(R.drawable.boca10);
					break;

				case 11:
					img_rosto.setImageResource(R.drawable.boca11);
					break;

				case 12:
					img_rosto.setImageResource(R.drawable.boca12);
					break;

				case 13:
					img_rosto.setImageResource(R.drawable.boca13);
					break;

				case 14:
					img_rosto.setImageResource(R.drawable.boca14);
					break;

				case 15:
					img_rosto.setImageResource(R.drawable.boca15);
					break;

				case 16:
					img_rosto.setImageResource(R.drawable.boca16);
					break;

				case 17:
					img_rosto.setImageResource(R.drawable.boca17);
					break;

				case 18:
					img_rosto.setImageResource(R.drawable.boca18);
					break;

				case 19:
					img_rosto.setImageResource(R.drawable.boca19);
					break;

				case 20:
					img_rosto.setImageResource(R.drawable.boca20);
					break;

				case 21:
					img_rosto.setImageResource(R.drawable.boca21);
					break;

				case 22:
					img_rosto.setImageResource(R.drawable.boca22);
					break;

				case 23:
					img_rosto.setImageResource(R.drawable.boca23);
					break;

				case 24:
					img_rosto.setImageResource(R.drawable.boca24);
					break;

				default:
					break;
				}				
				
				break;

			default:
				
				break;
			}
	}

	    public void onBackPressed(){
		        CameraActivity.this.finish();
		        android.os.Process.killProcess(android.os.Process.myPid());
		        System.exit(0);
		        getParent().finish();
		    }
	
	    
	    
	//   ________________________________________________________________________
	//  ________________________________________________________________________
	//  ________________________________________________________________________
	//  ________________________________________________________________________

		
		private void pickPhotoFromGallery() {

			Intent intent = new Intent(this,
					com.quatropdois.crop.CropImage.class);

			Bundle extras = new Bundle();

			extras.putString("circleCrop", "true");
			extras.putInt("aspectX", 200);
			extras.putInt("aspectY", 200);
			extras.putString("Local", local_foto);
			
			intent.putExtras(extras);
			
			startActivity(intent);
		}
		
//		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//			super.onActivityResult(requestCode, resultCode, data);
//			if (resultCode == RESULT_OK) {
//				switch (requestCode) {
//				case PHOTO_PICKED_WITH_DATA:
//
//					Uri uri = data.getData();
//					Intent intent = new Intent(this,
//							name.zhaoweihua.crop.CropImage.class);
//					Bundle extras = new Bundle();
//					extras.putString("circleCrop", "true");
//					extras.putInt("aspectX", 200);
//					extras.putInt("aspectY", 200);
//					intent.setDataAndType(uri, "image/jpeg");
//					intent.putExtras(extras);
//					startActivityForResult(intent, PHOTO_CROP_DATA);
//					break;
//				case PHOTO_CROP_DATA: // show back data
//
//					String srcData = data.getExtras().getString("data-src");
//					myViewhandler.sendEmptyMessage(SHOW_LOAD_DIALOG);
//					BitmapWorkerTask task = new BitmapWorkerTask(img_shower);
//					task.execute(srcData);
//
//				}
//			}
//
//		}

		public static Bitmap decodeBitmapFromFile(String src, int reqWidth, int reqHeight) {

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