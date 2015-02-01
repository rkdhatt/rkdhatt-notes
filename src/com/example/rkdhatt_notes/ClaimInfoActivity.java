/* An activity for expenses of corresponding claims and settings regarding claim status,
 * email claim information, edit/delete claims, and add/edit/delete expenses.
 * 
 * Problems: Saving expenses and updating the listview is still an issue. Also, for some reason when editing one expense,
 * the rest of the expenses save th same changes. Still need to fix. This results in sending only claim information and
 * not expense infomation once you leave the app and come back to the ClaimInfoActivity. This also results in changing statuses of claims
 * to not be saved throughout all activities, even though I have used toasts to show that changes have occurred.
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ClaimInfoActivity extends Activity implements OnItemSelectedListener{
	private String cat_selected;
	private String curr_selected;
	private static Claim selected_claim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claim_info);
		ClaimListManager.initManager(this.getApplicationContext());
		
		// Gab selected claim from ClaimsMainActivity.java
		Intent intent = getIntent();
		selected_claim = (Claim) intent.getSerializableExtra("selected claim");
		
		// Create list view for expenses
		Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
		Collection<Expense> expenses = ClaimListController.getClaimList().selectExpense(selected_claim);
		
		final ArrayList<Expense> expenseList = new ArrayList<Expense>(expenses);
		final ArrayList<Claim> clist = new ArrayList<Claim>(claims);
		final ArrayAdapter<Claim> claimAdapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1, clist);
        final CustomExpenseListViewAdapter adapter = new CustomExpenseListViewAdapter(this,
                R.layout.expense_items, expenseList);
        ListView listView = (ListView) findViewById(R.id.expenseListView);
        
		// want to only deal with the expense list from the selected claim and show it.
		listView.setAdapter(adapter);

		
		
		ClaimListController.getClaimList().addListener(new Listener() {

			@Override
			public void update() {
				clist.clear();
				expenseList.clear();
				Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
				Collection<Expense> expenses = ClaimListController.getClaimList().selectExpense(selected_claim);
				clist.addAll(claims);
				expenseList.addAll(expenses);
				// notify changes have occurred
				claimAdapter.notifyDataSetChanged();
				adapter.notifyDataSetChanged();
			}
			
		});
		
		// Clicking on expense, show choices for user to edit/delete claim, or cancel changes.
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				
				final Expense expense = expenseList.get(position);
				AlertDialog.Builder builder = new AlertDialog.Builder(ClaimInfoActivity.this);
				builder.setMessage("Edit or Delete Expense?");
				builder.setPositiveButton("Edit Expense", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
								sendEditExpenseMessage(expense);
				           }
				       });
				
				builder.setNeutralButton("Delete Expense", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
							sendDeleteExpenseMessage(expense);
			           }
			       });
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();
				           }
				       });

				// Create the AlertDialog
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			
		});
		
		// Email Claim information button -> EmailActivity.java
		// Source: http://stackoverflow.com/questions/8284706/send-email-via-gmail 2015-01-31
	    Button sendBtn = (Button) findViewById(R.id.emailButton);
	    sendBtn.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View view) {
	     		String message = "";
	   	  		ArrayList<String> claim_info = new ArrayList<String>();
	   	  		claim_info.add("Claim name: \n"+selected_claim.getClaimName()+" \n"+"Claim start date: \n"+selected_claim.getFromDate()+" \n"
	   	  				+"Claim end date: \n"+selected_claim.getToDate()+" \n"+"Claim Description: \n"+
	   	  				selected_claim.getClaimDescription()+" \n\n"+"Expenses: \n");
	    	    		
	    	    ArrayList<Expense> expenseList = selected_claim.getExpenseList();
	    	    
	    	    Toast.makeText(ClaimInfoActivity.this, 
  	    	          Float.valueOf(expenseList.size()).toString(), Toast.LENGTH_SHORT).show();
	    	    for (int i = 0; i < expenseList.size(); i++) {
	    	    	Expense expense = expenseList.get(i);
	    	    	claim_info.add("Expense name: \n"+expense.getExpenseName().toString()+" \n"+"Expense date: \n"+
	    	    	expense.getExpenseDate().toString()+
	    	    	"Expense Description: \n"+expense.getExpenseInfo().toString()+" \n"+"Category: \n"+expense.getExpenseCat().toString()+" \n"
	    	    	+"Amount: \n"+Float.valueOf(expense.getExpenseAmt()).toString()+" \n"+"Currency: \n"+expense.getExpenseCurr().toString()+" \n\n");
	    	    }
	    	    
	    	    message = claim_info.toString().replace("[", "").replace("]", "").replace(",", "");
	    	    
	    	    
	    	    		
	    	    final Intent email = new Intent(Intent.ACTION_SEND);
	    	    email.setType("text/plain");
	    	    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "rkdhatt@ualberta.ca"});
	    	    email.putExtra(Intent.EXTRA_TEXT, message); 
	    	    		
	    	    try {
	    	    	ClaimInfoActivity.this.startActivity(Intent.createChooser(email, "Sending mail..."));
	    	    	finish();
	    	    	          
	    	    } catch (android.content.ActivityNotFoundException ex) {
	    	    	Toast.makeText(ClaimInfoActivity.this, 
	    	    	          "There is no email client installed.", Toast.LENGTH_SHORT).show();
	    	    }
	    	      	      
	    	}
	    });
		
		
	}
	
	// create add expense dialog
	@SuppressLint("InflateParams")
	public void sendAddExpenseMessage(View view) {
		LayoutInflater li = LayoutInflater.from(getBaseContext());
		View addExpenseView = li.inflate(R.layout.add_expense_dialog, null);
		
		//EditTexts and spinner stuff from add_expense_dialog
		final EditText ex_name = (EditText) addExpenseView
				.findViewById(R.id.expenseNameText);
		final EditText ex_date = (EditText) addExpenseView
				.findViewById(R.id.expenseDateText);
		final EditText ex_info = (EditText) addExpenseView
				.findViewById(R.id.expenseInfoText);
		final EditText ex_amount = (EditText) addExpenseView
				.findViewById(R.id.amountText);
		
		// Create adapters for currency and category spinners
		// Source: http://developer.android.com/guide/topics/ui/controls/spinner.html 2015-01-25
		Spinner cat_spinner = (Spinner) addExpenseView.findViewById(R.id.cat_spinner);
		Spinner curr_spinner = (Spinner) addExpenseView.findViewById(R.id.curr_spinner);
		cat_spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
		curr_spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
		
		// Create an ArrayAdapter 
		ArrayAdapter<CharSequence> cat_adapter = ArrayAdapter.createFromResource(this,
		        R.array.category_array, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> curr_adapter = ArrayAdapter.createFromResource(this,
		        R.array.currency_array, android.R.layout.simple_spinner_item);
		
		// Specify the layout 
		cat_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		curr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinners
		cat_spinner.setAdapter(cat_adapter);
		curr_spinner.setAdapter(curr_adapter);
		
		AlertDialog.Builder Builder = new AlertDialog.Builder(ClaimInfoActivity.this);
		Builder.setView(addExpenseView);
		
		// Create cancel and save buttons, need to save data as well via files
				Builder.setCancelable(false)
					.setPositiveButton("Save", new DialogInterface.OnClickListener() {
						private String category;
						private String name;
						private String date;
						private float amount;
						private String currency;
						private String info;

						@Override
						public void onClick(DialogInterface dialog, int id) {
							Expense new_expense = new Expense(name, date, category, info, amount, currency); 
							new_expense.setExpenseName(ex_name.getText().toString());
							new_expense.setExpenseDate(ex_date.getText().toString());
							new_expense.setExpenseCat(cat_selected);
							new_expense.setExpenseInfo(ex_info.getText().toString());
							try{
							new_expense.setExpenseAmt(Float.valueOf(ex_amount.getText().toString()));
							} catch (NumberFormatException e) {
								Toast.makeText(getApplicationContext(), "Didn't add Price! Making default Price = 0.0",
					        			Toast.LENGTH_LONG).show();
								new_expense.setExpenseAmt(0);
								
							}
							new_expense.setExpenseCurr(curr_selected);
							selected_claim.addExpense(new_expense);
							ClaimListController.getClaimList().notifyListeners();
							
							Toast.makeText(getApplicationContext(), 
									"New expense successfully added!", Toast.LENGTH_LONG).show();
							
						}
					})
					
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
		
		AlertDialog alertDialog = Builder.create();
		alertDialog.show();
	}
	
	// create edit expense dialog
	@SuppressLint("InflateParams")
	public void sendEditExpenseMessage(final Expense edit_expense) {
		LayoutInflater li = LayoutInflater.from(getBaseContext());
		View editExpenseView = li.inflate(R.layout.edit_expense_dialog, null);
		
		//EditTexts and spinner stuff from add_expense_dialog
		final EditText ex_name = (EditText) editExpenseView
				.findViewById(R.id.expenseNameText);
		ex_name.setHint("Name was: "+edit_expense.getExpenseName().toString());
		final EditText ex_date = (EditText) editExpenseView
				.findViewById(R.id.expenseDateText);
		ex_date.setHint("Date was: "+edit_expense.getExpenseDate().toString());
		final EditText ex_info = (EditText) editExpenseView
				.findViewById(R.id.expenseInfoText);
		ex_info.setHint("Info was: "+edit_expense.getExpenseInfo().toString());
		final EditText ex_amount = (EditText) editExpenseView
				.findViewById(R.id.amountText);
		ex_amount.setHint("Price was: " +Float.valueOf(edit_expense.getExpenseAmt()).toString());
		
		Spinner cat_spinner = (Spinner) editExpenseView.findViewById(R.id.cat_spinner);
		Spinner curr_spinner = (Spinner) editExpenseView.findViewById(R.id.curr_spinner);
		cat_spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
		curr_spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
		
		ArrayAdapter<CharSequence> cat_adapter = ArrayAdapter.createFromResource(this,
		        R.array.category_array, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> curr_adapter = ArrayAdapter.createFromResource(this,
		        R.array.currency_array, android.R.layout.simple_spinner_item);
		
		cat_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		curr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		cat_spinner.setAdapter(cat_adapter);
		curr_spinner.setAdapter(curr_adapter);
		
		AlertDialog.Builder Builder = new AlertDialog.Builder(ClaimInfoActivity.this);
		Builder.setView(editExpenseView);
		
				Builder.setCancelable(false)
					.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int id) {
							edit_expense.setExpenseName(ex_name.getText().toString());
							edit_expense.setExpenseDate(ex_date.getText().toString());
							edit_expense.setExpenseCat(cat_selected);
							edit_expense.setExpenseInfo(ex_info.getText().toString());
							try {
							edit_expense.setExpenseAmt(Float.valueOf(ex_amount.getText().toString()));
							} catch (NumberFormatException e) {
								Toast.makeText(getApplicationContext(), 
										"Didn't add Price! Making default Price = 0.0", Toast.LENGTH_LONG).show();
								edit_expense.setExpenseAmt(0);
							}
							edit_expense.setExpenseCurr(curr_selected);
							
							ClaimListController.getClaimList().notifyListeners();
							
							Toast.makeText(getApplicationContext(), 
									"Expense successfully edited!", Toast.LENGTH_LONG).show();
						}
					})
					
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
		
		AlertDialog alertDialog = Builder.create();
		alertDialog.show();
	}
	
	// create delete expense dialog
	private void sendDeleteExpenseMessage(Expense expense) {
		
		final Expense delete_expense = expense;
		AlertDialog.Builder Builder = new AlertDialog.Builder(ClaimInfoActivity.this);
		Builder.setMessage("Are you sure?");
		Builder.setCancelable(true);
		Builder.setPositiveButton("Delete", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selected_claim.getExpenseList().remove(delete_expense);
				ClaimListController.getClaimList().notifyListeners();
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
	
	// Create setOnItemSelectedListener for each spinner
	// Source: http://stackoverflow.com/questions/10777977/android-multiple-spinners 2015-01-25
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
        switch (parent.getId()) {
        
        case R.id.cat_spinner:
        	cat_selected = parent.getItemAtPosition(position).toString();
            break;
            
        case R.id.curr_spinner:
        	curr_selected = parent.getItemAtPosition(position).toString();
            break;
            
        default:
            break;
        }
		
	}

	public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {
        
        case R.id.cat_spinner:
        	Toast.makeText(getApplicationContext(), "No category selected!",
        			Toast.LENGTH_LONG).show();
            break;
            
        case R.id.curr_spinner:
        	Toast.makeText(getApplicationContext(), "No currency selected!",
        			Toast.LENGTH_LONG).show();
            break;
            
        default:
            break;
        }
		
	}
	
	// Submitting claim - does work, but when you go back to main claim activity it won't show changes
	public void submitClaim (MenuItem menu){
	
		selected_claim.setStatusSubmitted();
		Toast.makeText(getApplicationContext(),selected_claim.getStatus(),
    			Toast.LENGTH_LONG).show();
		
		ClaimListController.getClaimList().notifyListeners();
		
	}
	
	// Approving claim - same issue as submit claim method
	public void approveClaim (MenuItem menu){
	
		selected_claim.setStatusApproved();
		
		Toast.makeText(getApplicationContext(),selected_claim.getStatus()+": Cannot return once approved.",
    			Toast.LENGTH_LONG).show();
		
		ClaimListController.getClaimList().notifyListeners();
		
	}
	
	// Returning claim - same issue as submit claim method
	public void returnClaim(MenuItem item) {
		
		selected_claim.setStatusReturned();
		
		Toast.makeText(getApplicationContext(),selected_claim.getStatus()+". Cannot return once approved.",
    			Toast.LENGTH_LONG).show();
		
		ClaimListController.getClaimList().notifyListeners();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.claim_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == R.id.statusSubmitted) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	

}
