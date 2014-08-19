package com.quatropdois.foto_rosto;

import com.example.foto_rosto.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OlhosActivity extends Activity {

	private int olho = 0;
	private int rosto = 1;
    private Intent cameraActivity;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_olhos);
		
		init();
	}
	
	private void init(){
		
    	AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
		
		cameraActivity = new Intent(getApplicationContext(), CameraActivity.class);
		
	}
	
	public void selectOlhos(View view){
		
		switch (view.getId()) {
		
		case R.id.BtnOlhos1:
			
			olho = 1;
			mudatela();
			
			break;
			
		case R.id.BtnOlhos2:
			
			olho = 2;
			mudatela();
				
			break;
			
		case R.id.BtnOlhos3:
			
			olho = 3;
			mudatela();
			
			break;
		
		case R.id.BtnOlhos4:
			
			olho = 4;
			mudatela();
			
			break;
		
		case R.id.BtnOlhos5:
			
			olho = 5;
			mudatela();
			
			break;
		
		case R.id.BtnOlhos6:
			
			olho = 6;
			mudatela();
			
			break;
		
		case R.id.BtnOlhos7:
			
			olho = 7;
			mudatela();
			
			break;
		
		case R.id.BtnOlhos8:
			
			olho = 8;
			mudatela();
			
			break;

			
		case R.id.BtnOlhos9:
			
			olho = 9;
			mudatela();
			
			break;
			
		case R.id.BtnOlhos10:
			
			olho = 10;
			mudatela();
			
			break;

		case R.id.BtnOlhos11:
			
			olho = 11;
			mudatela();
			
			break;

		case R.id.BtnOlhos12:
			
			olho = 12;
			mudatela();
			
			break;	

		case R.id.BtnOlhos13:
			
			olho = 13;
			mudatela();
			
			break;	

		case R.id.BtnOlhos14:
			
			olho = 14;
			mudatela();
			
			break;	

		case R.id.BtnOlhos15:
			
			olho = 15;
			mudatela();
			
			break;	

		case R.id.BtnOlhos16:
			
			olho = 16;
			mudatela();
			
			break;	

		case R.id.BtnOlhos17:
			
			olho = 17;
			mudatela();
			
			break;	

		case R.id.BtnOlhos18:
			
			olho = 18;
			mudatela();
			
			break;	

		case R.id.BtnOlhos19:
			
			olho = 19;
			mudatela();
			
			break;	

		case R.id.BtnOlhos20:
			
			olho = 20;
			mudatela();
			
			break;	

		case R.id.BtnOlhos21:
			
			olho = 21;
			mudatela();
			
			break;	

		case R.id.BtnOlhos22:
			
			olho = 22;
			mudatela();
			
			break;	

		case R.id.BtnOlhos23:
			
			olho = 23;
			mudatela();
			
			break;	

		case R.id.BtnOlhos24:
			
			olho = 24;
			mudatela();
			
			break;	

		case R.id.BtnOlhos25:
			
			olho = 25;
			mudatela();
			
			break;	

		case R.id.BtnOlhos26:
			
			olho = 26;
			mudatela();
			
			break;				

		default:
			break;
		}
		
	}
	
	private void mudatela(){

	    cameraActivity.putExtra("Rosto", rosto);
    	cameraActivity.putExtra("img_n", olho);
		startActivity(cameraActivity);
		
	}
	
    public void onBackPressed(){
        OlhosActivity.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        getParent().finish();
    }

}
