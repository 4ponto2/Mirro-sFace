package com.quatropdois.selfieSwap;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quatropdois.selfieSwap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Splash_selfieSwapActivity extends Activity {
	
	protected static final String TAG = "SplashScreenSelfieSwap";
	
	private Intent main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_selfie_swap);
		
		main = new Intent(getApplicationContext(), MainActivity.class);
		
        AdView adView = (AdView) findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder()
       .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .build();
	    adView.loadAd(adRequest);	
	    
		 final Button button = (Button) findViewById(R.id.button1);
         button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	 
            	 startActivity(main);
            	 
             }
         });
		
	}
	
    public void onBackPressed(){
	 	   Log.i("HA", "Finishing");
	 	   Intent intent = new Intent(Intent.ACTION_MAIN);
	 	   intent.addCategory(Intent.CATEGORY_HOME);
	 	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 	   startActivity(intent);
	 }	
	
}
