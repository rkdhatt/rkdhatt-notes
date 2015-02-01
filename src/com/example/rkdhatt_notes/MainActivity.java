/* A main activity for the start menu of the ExpenseTracker application.
 * 
 * 
 * 
 * Copyright (C) 2015 Ramandeep Dhatt rkdhatt@ulberta.ca
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.*/

package com.example.rkdhatt_notes;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ClaimListManager.initManager(this.getApplicationContext());
		
		// Send intent to press the start button to ClaimsMainActivity.java
		final Button start_button = (Button) findViewById(R.id.startButton);
        start_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
                // Perform action on clicking the start button
        		// Will trigger ClaimsMainActivity to show claims_main.xml view
        		// Source: http://www.warriorpoint.com/blog/2009/05/24/android-how-to-switch-between-activities/
        		Intent startIntent = new Intent(view.getContext(), ClaimsMainActivity.class);
        		startActivityForResult(startIntent, 0);
            }
        });
	}
	
	// for activity_claims_main.xml when a user tries to choose an existing claim from the list view
	public void chooseClaim(View v) {
		Toast.makeText(this, "Choose a claim", Toast.LENGTH_SHORT).show();
		ClaimListController cc = new ClaimListController();
		try{
			Claim claim = cc.chooseClaim();
			TextView view = (TextView) findViewById(R.id.chooseClaimTextView);
			view.setText(claim.getClaimName());
		} catch (IllegalStateException e) {
			Toast.makeText(this, "No claims available!", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.main_action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
