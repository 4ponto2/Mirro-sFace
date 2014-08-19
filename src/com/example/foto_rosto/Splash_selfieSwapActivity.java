package com.example.foto_rosto;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Splash_selfieSwapActivity extends Activity {
	
	protected static final String TAG = "SplashScreenSelfieSwap";
	private static final long TEMPO_DURACAO_ENTRADA = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_selfie_swap);
		
		new Handler().postDelayed(new Runnable() {
			
			public void run() {
				try{
					//Metodo que invoca a outra activity
					startActivity(new Intent(Splash_selfieSwapActivity.this, MainActivity.class));
					Log.d(TAG, "Tela 1");
				}catch(ActivityNotFoundException e){
					Log.e(TAG, e.getMessage());
				}
			}
		}, TEMPO_DURACAO_ENTRADA);		
	}
}
