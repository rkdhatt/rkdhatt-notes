/* An activity for deleting a selected claim and updates corresponding list view
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DeleteClaimActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_claim);
		
		ClaimListManager.initManager(this.getApplicationContext());

		
		// Create a list view for the claims list in activity_delete_claims.xml
		ListView lv = (ListView) findViewById(R.id.deleteClaimListView);
		Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
		// need final so that the reference will always stay with list whether or not the contents change
		// need for update() in Listener interface
		final ArrayList<Claim> clist = new ArrayList<Claim>(claims);
		final ArrayAdapter<Claim> delClaimAdapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1, clist);
		lv.setAdapter(delClaimAdapter);
		
		// Now get listeners to keep the ListView updated and have the ListView show items right away
		ClaimListController.getClaimList().addListener(new Listener() {
			@Override
			public void update() {
				clist.clear();
				Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
				clist.addAll(claims);
				// notify changes have occurred
				delClaimAdapter.notifyDataSetChanged();
			}
		});
		
		// now want to delete a claim 
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// need activityname.this so makeText doesn't give error
				// Use alert dialog to make sure deletion doesn't occur automatically.
				final int finalPos = position;
				AlertDialog.Builder Builder = new AlertDialog.Builder(DeleteClaimActivity.this);
				Builder.setMessage("Are you sure?");
				Builder.setCancelable(true);
				Builder.setPositiveButton("Delete", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Claim claim = clist.get(finalPos);
						ClaimListController.getClaimList().deleteClaim(claim);
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
		getMenuInflater().inflate(R.menu.delete_claim, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
