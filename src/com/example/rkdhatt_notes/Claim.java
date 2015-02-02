/* Class for claims object.
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

import java.io.Serializable;
import java.util.ArrayList;

import android.widget.Toast;

// need for from and to dates
// import java.util.Date;

public class Claim implements Serializable{
	/**
	 * Claim serialization
	 */
	private static final long serialVersionUID = 3345824350399046179L;
	private String from_date = "N/A";
	private String to_date = "N/A"; 
	private String describe_claim = "N/A";
	private String c_name = "N/A";
	private String status = "in progress";
	
	// A claim is associated with expenses, so need to allocate space for expenses
	private static ArrayList<Expense> expense_list = new ArrayList<Expense>();
	
	public Claim (String from_date, String to_date, String describe_claim, String c_name, String status, ArrayList<Expense> expense_list) {
		super();
		this.from_date = from_date;
		this.to_date = to_date;
		this.describe_claim = describe_claim;
		this.c_name = c_name;
		this.status = status;
		Claim.expense_list = expense_list;
	}
	

	// need for the claim list view to show the name of the claim
	@Override
	public String toString() {
		String output = this.getClaimName()+" - Status: "+this.getStatus();
		return output;
	}

	// create getters and setters because need to be about to add/edit/delete.
	public String getFromDate() {
		return from_date;
	}
	
	public void setFromDate(String new_from_date) {
		this.from_date = new_from_date;
	}
	
	public String getToDate() {
		return to_date;
	}
	
	public void setToDate(String new_to_date){
		this.to_date = new_to_date;
	}
	
	public String getClaimDescription() {
		return describe_claim;
	}
	
	public void setClaimDescription(String new_describe_claim) {
		this.describe_claim = new_describe_claim;
	}
	
	public String getClaimName() {
		return c_name;
	}
	
	public void setClaimName(String new_name) {
		this.c_name = new_name;
	}
	
	public String getStatus(){
		return status;
	}
	
	//make submit and approved constant somehow.
	public void setStatusInProgress(){
		status = "in progress"; 	
	}
	
	public final void setStatusSubmitted(){
		
		// make sure status is not approved before submitting
		if (this.getStatus() != "approved") {
			this.status = "submitted";
		} 
	}
	
	public void setStatusReturned(){
		
		// cannot return once approved
		if (this.getStatus() != "approved") {
			status = "returned";
		}
	}
	
	public final void setStatusApproved(){
		status = "approved";
	}

	public ArrayList<Expense> getExpenseList() {
		return expense_list;
	}
	
	public void setExpenseList(ArrayList<Expense> expense_list) {
		Claim.expense_list = expense_list;
	}

	public void addExpense(Expense new_expense) {
		Claim.expense_list.add(new_expense);
	}
	
	// confirm that an expense exists (making sure an expense has been added to a selected claim)
	public boolean confirmExpense(Expense expense) {
		return expense_list.contains(expense); 
	}
	
	// Need to check if object is a claim
	public boolean equalsObject(Claim claimCompare) {
		if (claimCompare != null && claimCompare.getClass() == this.getClass()) {
			return this.equals((Claim)claimCompare);
			
		} else {
			return false;
		}
	}
	
	public boolean equalsClaim(Claim claimCompare) {
		if (claimCompare == null) {
			return false;
		}
		return getClaimName().equals(claimCompare.getClaimName());
	}
	
	public int hashCode() {
		return ("Claim:"+getClaimName()).hashCode();
	}
}