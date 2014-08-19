package com.quatropdois.foto_rosto;

import com.example.foto_rosto.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Splash_selfieSwapActivity extends Activity {
	
	protected static final String TAG = "SplashScreenSelfieSwap";
	private static final long TEMPO_DURACAO_ENTRADA = 1000;
	
	private Intent main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_selfie_swap);
		

    	AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        
		
		main = new Intent(getApplicationContext(), MainActivity.class);
		
		
		 final Button button = (Button) findViewById(R.id.button1);
         button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	 
            	 startActivity(main);
            	 
             }
         });
		
		
//		new Handler().postDelayed(new Runnable() {
//			
//			public void run() {
//				try{
//					//Metodo que invoca a outra activity
//					startActivity(new Intent(Splash_selfieSwapActivity.this, MainActivity.class));
//					Log.d(TAG, "Tela 1");
//				}catch(ActivityNotFoundException e){
//					Log.e(TAG, e.getMessage());
//				}
//			}
//		}, TEMPO_DURACAO_ENTRADA);		
	}
	
}