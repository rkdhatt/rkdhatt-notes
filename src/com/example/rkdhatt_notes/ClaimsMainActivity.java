/* A main activity for list of claims and adding new claims, 
 * allows user to go to edit and delete acitivty to edit/delete claims
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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class ClaimsMainActivity extends Activity {
	
	final Context context = this;
	private String from_date;
	private String to_date; 
	private String describe_claim;
	private String c_name;
	private String status;
	private ArrayList<Expense> expense_list;
	private static final String claimFileName ="claim_file";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claims_main);
		
		ClaimListManager.initManager(this.getApplicationContext());
		
		// Create a list view for the claims list in activity_claims_main.xml
		ListView listView = (ListView) findViewById(R.id.claimListView);
		Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
		// need final so that the reference will always stay with list whether or not the contents change
		// need for update() in Listener interface
		final ArrayList<Claim> list = new ArrayList<Claim>(claims);
		final ArrayAdapter<Claim> claimAdapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1, list);
		listView.setAdapter(claimAdapter);
		
		// Now get listeners to keep the ListView updated and have the ListView show items right away
		ClaimListController.getClaimList().addListener(new Listener() {

			@Override
			public void update() {
				list.clear();
				Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
				list.addAll(claims);
				// notify changes have occurred
				claimAdapter.notifyDataSetChanged();
			}
			
		});
		
		// set a listener for when clicking on a claim from the claim list view in activity_claims_main.xml
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// Create an intent to go to ClaimInfoActivity.java
				// Should also pass on the claim object selected
				Claim claim = list.get(position);
				Intent status = new Intent(ClaimsMainActivity.this, ClaimInfoActivity.class );
			    status.putExtra("selected claim", claim);
			    startActivity(status);
				
			}
			
		});
		
        // ListView for claims
        //http://developer.android.com/guide/topics/ui/declaring-layout.html#AdapterViews
        
	}
	
    // Respond to the "Add Claim" Button (activity_claims_main.xml) by calling a function 
	// that will create a dialog which will get the user to add claim name, from date, 
	// to date, and description of the claim.
	// Source: http://developer.android.com/guide/topics/ui/dialogs.html#DialogFragment
	public void sendAddClaimMessage(View view) 
	{
		LayoutInflater li_add_claim = LayoutInflater.from(context);
		View addClaimView = li_add_claim.inflate(R.layout.add_claim_dialog, null);
		//EditTexts from add_claim_dialog
		final EditText claim_name = (EditText) addClaimView
				.findViewById(R.id.addClaimName);
		final EditText first_date = (EditText) addClaimView
				.findViewById(R.id.fromDateText);
		final EditText last_date = (EditText) addClaimView
				.findViewById(R.id.toDateText);
		final EditText claim_descr = (EditText) addClaimView
				.findViewById(R.id.claimDescriptionText);
		
		//create AlertDialog and link to add_claim_dialog.xml
		AlertDialog.Builder Builder = new AlertDialog.Builder(context);
		Builder.setView(addClaimView);
		
		// Create cancel and save buttons, need to save data as well via files
		Builder.setCancelable(false)
			.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// Save the input entered. Make sure from and to dates are appropriate
					// and nothing is null.
					
					// Create new Claim and save data user has entered
					// Assumption: set claim status = "in progress"
					expense_list = new ArrayList<Expense>();
					Claim new_claim = new Claim(from_date, to_date, describe_claim, c_name, status, expense_list);
					new_claim.setFromDate(first_date.getText().toString());
					new_claim.setToDate(last_date.getText().toString());
					new_claim.setClaimDescription(claim_descr.getText().toString());
					new_claim.setClaimName(claim_name.getText().toString());
					new_claim.setStatusInProgress();
					
					ClaimListController clc = new ClaimListController();
					clc.addClaim(new_claim);
					
				}
			})
			
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		
		// create and show resulting dialog based on add_claim_dialog.xml which
		// now includes cancel and save buttons
		AlertDialog alertDialog = Builder.create();
		alertDialog.show();
	}
	

	public void editClaim (MenuItem menu) {
		// go to EditClaimActivity to edit a claim
		Intent intent = new Intent(ClaimsMainActivity.this, EditClaimActivity.class);
		startActivity(intent);
	}
	
	public void deleteClaim (MenuItem menu){
		// go to DeleteClaimActivity to delete a claim
		Intent intent = new Intent(ClaimsMainActivity.this, DeleteClaimActivity.class);
		startActivity(intent);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claims_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// action_settings2 is from menu/claims_main.xml
		if (id == R.id.editClaimOption) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}

