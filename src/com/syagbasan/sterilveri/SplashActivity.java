/*
 * Copyright (c) 2014 Semih YAGBASAN, YAGBASAN HO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syagbasan.sterilveri;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SplashActivity extends Activity{

	FrameLayout startingFrame;
	Intent intent;
	boolean  touching = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);
		startingFrame = (FrameLayout) findViewById(R.id.startingFrame);
		intent = new Intent(getApplicationContext(),MainActivity.class);
		
		startingFrame.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				touching = true;
				startActivity(intent);
			    finish();
				return false;
			}
		});
		
		new Handler().postDelayed(new Runnable(){
		      @Override
		      public void run() {
		          if (!touching) {
		        	  startActivity(intent);
		        	  finish();
		          }
		      }
		}, 1000);
	}
}
