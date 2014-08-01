package com.example.foto_rosto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BocaActivity extends Activity {

	private int boca = 0;
	private int rosto = 3;
    private Intent cameraActivity;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boca);
	}

	private void init(){
		
		cameraActivity = new Intent(getApplicationContext(), CameraActivity.class);
		
	}
	
	public void selectBoca(View view){
		
		switch (view.getId()) {
		
		case R.id.BtnBoca1:
			
			boca = 1;
			mudatela();
			
			break;
			
		case R.id.BtnBoca2:
			
			boca = 2;
			mudatela();
				
			break;
			
		case R.id.BtnBoca3:
			
			boca = 3;
			mudatela();
			
			break;
		
		case R.id.BtnBoca4:
			
			boca = 4;
			mudatela();
			
			break;
		
		case R.id.BtnBoca5:
			
			boca = 5;
			mudatela();
			
			break;
		
		case R.id.BtnBoca6:
			
			boca = 6;
			mudatela();
			
			break;
		
		case R.id.BtnBoca7:
			
			boca = 7;
			mudatela();
			
			break;
		
		case R.id.BtnBoca8:
			
			boca = 8;
			mudatela();
			
			break;

		default:
			break;
		}
		
	}
	
	private void mudatela(){

	    cameraActivity.putExtra("Rosto", rosto);
    	cameraActivity.putExtra("img_n", boca);
		startActivity(cameraActivity);
		
	}
	
    public void onBackPressed(){
        BocaActivity.this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        getParent().finish();
    }

}
