/* An activity for editting claim information and save changes. Also updates ListView for Both activity_edit_claim.xml
 * and activity_main.xml
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class EditClaimActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		
		ClaimListManager.initManager(this.getApplicationContext());
		
		// Create a list view for the claims list in activity_delete_claims.xml
		ListView lv = (ListView) findViewById(R.id.editClaimListView);
		Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
		// need final so that the reference will always stay with list whether or not the contents change
		// need for update() in Listener interface
		final ArrayList<Claim> clist = new ArrayList<Claim>(claims);
		final ArrayAdapter<Claim> editClaimAdapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1, clist);
		lv.setAdapter(editClaimAdapter);
		
		
		// Now get listeners to keep the ListView updated and have the ListView show items right away
		ClaimListController.getClaimList().addListener(new Listener() {
			@Override
			public void update() {
				clist.clear();
				Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
				clist.addAll(claims);
				// notify changes have occurred
				editClaimAdapter.notifyDataSetChanged();
			}
		});
		
		// Edit a selected claim
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			LayoutInflater li = LayoutInflater.from(getBaseContext());
			View editClaimView = li.inflate(R.layout.edit_claim_dialog, null);

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				final Claim claim = clist.get(position);
				// Edit Texts from edit claim dialog
				final EditText name = (EditText) editClaimView
						.findViewById(R.id.editClaimName);
				name.setHint("Name was: "+claim.getClaimName());
				final EditText start = (EditText) editClaimView
						.findViewById(R.id.editFromDateText);
				start.setHint("Start date was: "+claim.getFromDate());
				final EditText end = (EditText) editClaimView
						.findViewById(R.id.editToDateText);
				end.setHint("End date was: "+claim.getToDate());
				final EditText info = (EditText) editClaimView
						.findViewById(R.id.editClaimInfoText);
				info.setHint("Info was: "+claim.getClaimDescription());
			
				AlertDialog.Builder Builder = new AlertDialog.Builder(EditClaimActivity.this);
				Builder.setView(editClaimView);
				Builder.setCancelable(true);
				Builder.setPositiveButton("Save", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						// check status of claim, if its submitted or approved, you cannot make changes
						final String status = claim.getStatus();
						
						if (status != "approved" && status != "submitted") {
							claim.setFromDate(start.getText().toString());
							claim.setToDate(end.getText().toString());
							claim.setClaimDescription(info.getText().toString());
							claim.setClaimName(name.getText().toString());
						
							ClaimListController.getClaimList().notifyListeners(); 
						}
						
						else {
							// send toast message that the edit wasn't successful.
							Toast.makeText(getApplicationContext(),"Edit Claim wasn't successful due to Claim status= "+claim.getStatus(),
					    			Toast.LENGTH_LONG).show();
						}
					}
					
				});
				
				Builder.setNegativeButton("Cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Do Nothing
					}
				});
				Builder.show();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_claim, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
