package com.example.foto_rosto;

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
		
		case R.id.BtnNariz8:
			
			nariz = 8;
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

