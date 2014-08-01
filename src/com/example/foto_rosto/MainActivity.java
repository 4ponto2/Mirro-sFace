package com.example.foto_rosto;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends Activity {

		private boolean wasExecuted = false;
	    private Intent olhosActivity, narizActivity, bocaActivity;
	    
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			init();
			setContentView(R.layout.activity_main);
		}	
		
	    public void init()
	    {
//	    	mainActivity = new Intent(getApplicationContext(), CameraActivity.class);
	    	olhosActivity = new Intent(getApplicationContext(), OlhosActivity.class);
	    	narizActivity = new Intent(getApplicationContext(), NarizActivity.class);
	    	bocaActivity = new Intent(getApplicationContext(), BocaActivity.class);
	        wasExecuted = false;
	    }

	    public void proxima(View view){
	        
	    	if(!wasExecuted){
	    	    wasExecuted = true;
	    	    
	    	    switch (view.getId()) {
				case R.id.button1:
					
					startActivity(olhosActivity);
					
					break;
					
				case R.id.button2:
					
					startActivity(narizActivity);
					
					break;
					
				case R.id.button3:
					
					startActivity(bocaActivity);
					
					break;

				default:
					
					Toast.makeText(getApplicationContext(), "Botão não funcionando!", Toast.LENGTH_SHORT).show();
					
					break;
				}
	            
	        }
	    }
	    
	    public void onBackPressed(){
	 	   Log.i("HA", "Finishing");
	 	   Intent intent = new Intent(Intent.ACTION_MAIN);
	 	   intent.addCategory(Intent.CATEGORY_HOME);
	 	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 	   startActivity(intent);
	 }

	}
