package com.example.foto_rosto;

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
			
			olho = 8;
			mudatela();
			
			break;
			
		case R.id.BtnOlhos10:
			
			olho = 9;
			mudatela();
			
			break;

		case R.id.BtnOlhos11:
			
			olho = 11;
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
