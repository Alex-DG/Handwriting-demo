package com.unit9.test.handwriting_demo;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
 * Drawing board
 */
public class MainActivity extends Activity {

    private String gesturesFilePath = "/storage/emulated/0/gestures";
    private File f;    
	private GestureLibrary gesturesLib;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);       

        f = new File(gesturesFilePath);
        
        // You have to create gestures file if it doesn't exist
        if(!f.exists()){        	
        	CreateGestures();      	
        }
        
        //Gesture view
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);        
        gestures.addOnGesturePerformedListener(handleGestureListener);
        
        //Button add gesture
        Button addGesture = (Button) findViewById(R.id.addGesture_button);       
        addGesture.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, GestureBuilderActivity.class);
				startActivity(intent);		
			}
		});
    }

	/*
	 * Gesture listener
	 */
	private OnGesturePerformedListener handleGestureListener = new OnGesturePerformedListener() {
		
		@Override
		public void onGesturePerformed(GestureOverlayView gestureView, Gesture gesture) {

			//Update gestures
	        UpdateGestures();
	        
	        //List of gestures
			ArrayList<Prediction> predictions = gesturesLib.recognize(gesture);
	
			// one prediction needed
			if (predictions.size() > 0) {
				Prediction prediction = predictions.get(0);				
				// checking prediction
				if (prediction.score > 1.0) {
					// toast message with result
					Toast.makeText(MainActivity.this, prediction.name, Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	/*
	 * Update Gestures
	 */
	private void UpdateGestures() {

        //Loading the gestures library with a a set of pre-defined gestures
        if(f.exists()){
        	
    		gesturesLib = GestureLibraries.fromFile(f);
            if (!gesturesLib.load()){
              finish();
            } 
            
        }
	}
	
	/*
	 * Create gesture(s) or close the application
	 */
	private void CreateGestures() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    	builder.setMessage(R.string.dialog_message)
    	       .setTitle(R.string.dialog_title)
    	       .setPositiveButton(R.string.create_button, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   
		               // User clicked OK button
		        	   Intent intent = new Intent(MainActivity.this, GestureBuilderActivity.class);
		   			   startActivity(intent);     
		           }
		       })
		       .setNegativeButton(R.string.close_button, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog	
		        	   finish();			        	  
		           }
		       });
    		
    	AlertDialog dialog = builder.create();	
    	dialog.show();
	}
}
