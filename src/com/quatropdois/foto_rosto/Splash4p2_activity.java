package com.quatropdois.foto_rosto;

import com.example.foto_rosto.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Splash4p2_activity extends Activity {

	protected static final String TAG = "SplashScreen";
	private static final long TEMPO_DURACAO_ENTRADA = 1000;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash4p2);
		
		new Handler().postDelayed(new Runnable() {
			
			public void run() {
				try{
					//Metodo que invoca a outra activity
					startActivity(new Intent(Splash4p2_activity.this, Splash_selfieSwapActivity.class));
					Log.d(TAG, "Tela 1");
				}catch(ActivityNotFoundException e){
					Log.e(TAG, e.getMessage());
				}
			}
		}, TEMPO_DURACAO_ENTRADA);		
	}
}
