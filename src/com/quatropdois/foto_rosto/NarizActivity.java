package com.quatropdois.foto_rosto;

import com.example.foto_rosto.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NarizActivity extends Activity {

	private int nariz = 0;
	private int rosto = 2;
    private Intent cameraActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nariz);
		init();
	}
	
	private void init(){
		
		AdView adView = (AdView) findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder()
       .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .build();
	    adView.loadAd(adRequest);	
	    
		cameraActivity = new Intent(getApplicationContext(), CameraActivity.class);
		
	}
	
	public void selectNariz(View view){
		
		switch (view.getId()) {
		
		case R.id.BtnNariz1:
			
			nariz = 1;
			mudatela();
			
			break;
			
		case R.id.BtnNariz2:
			
			nariz = 2;
			mudatela();
				
			break;
			
		case R.id.BtnNariz3:
			
			nariz = 3;
			mudatela();
			
			break;
		
		case R.id.BtnNariz4:
			
			nariz = 4;
			mudatela();
			
			break;
		
		case R.id.BtnNariz5:
			
			nariz = 5;
			mudatela();
			
			break;
		
		case R.id.BtnNariz6:
			
			nariz = 6;
			mudatela();
			
			break;
		
		case R.id.BtnNariz7:
			
			nariz = 7;
			mudatela();
			
			break;
			
		case R.id.BtnNariz9:
			
			nariz = 9;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz10:
			
			nariz = 10;
			mudatela();
			
			break;
			
		case R.id.BtnNariz11:
			
			nariz = 11;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz12:
			
			nariz = 12;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz13:
			
			nariz = 13;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz14:
			
			nariz = 14;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz15:
			
			nariz = 15;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz16:
			
			nariz = 16;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz17:
			
			nariz = 17;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz18:
			
			nariz = 18;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz19:
			
			nariz = 19;
			mudatela();
			
			break;		
			
		case R.id.BtnNariz20:
			
			nariz = 20;
			mudatela();
			
			break;	
			
		case R.id.BtnNariz21:
			
			nariz = 21;
			mudatela();
			
			break;	
			
		case R.id.BtnNariz22:
			
			nariz = 22;
			mudatela();
			
			break;	
			
		case R.id.BtnNariz23:
			
			nariz = 23;
			mudatela();
			
			break;	
			
		case R.id.BtnNariz24:
			
			nariz = 24;
			mudatela();
			
			break;					

		default:
			break;
		}
		
	}
	
	private void mudatela(){

	    cameraActivity.putExtra("Rosto", rosto);
    	cameraActivity.putExtra("img_n", nariz);
		startActivity(cameraActivity);
		
	}

    public void onBackPressed(){
        NarizActivity.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        getParent().finish();
    }
}

