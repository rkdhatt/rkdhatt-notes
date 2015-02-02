/* Class for a list of claims.
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
import java.util.Collection;

public class ClaimList implements Serializable {

	/**
	 * Need serial ID for ClaimList.java
	 */
	private static final long serialVersionUID = 3510593593848515051L;
	protected ArrayList<Claim> claimList = null;
	// Transient: don't need to save listener to disk
	protected transient ArrayList<Listener> listeners = null;

	public ClaimList(){
		super();
		claimList = new ArrayList<Claim>();
		listeners = new ArrayList<Listener>();
	}
	
	// To prevent issues from serialization make sure all listeners are initialized
	// so that all changes are saved to the disk except the listeners themselves
	private ArrayList<Listener> getListeners() {
		if (listeners == null) {
			listeners = new ArrayList<Listener>();
		}
		return listeners;
	}
	
	public Collection<Claim> getClaims() {
		return claimList;
	}
	
	public void addClaim (Claim new_claim) {
		claimList.add(new_claim);
		// notify listeners that claim has been updated (added)
		notifyListeners();
	}

	protected void notifyListeners() {
		for (Listener listener: getListeners()) {
			listener.update();
		}	
	}
	

	public void deleteClaim (Claim claim) {
		claimList.remove(claim);
		// notify listeners that claim has been updated (deleted)
		notifyListeners();
	}
	
	public int size() {
		return claimList.size();
	}
	
	// public boolean contains(Claim claim) {return studentList.contains(claim);}

	// Need to find the claim chosen by student first (find the right index)...
	public Claim chooseClaim() throws IllegalStateException{
		int size = claimList.size();
		if (size <= 0) {
			throw new IllegalStateException();
		}
		int index = claimList.size();
		for(int i = 0; i<claimList.size();) {
			if (claimList.get(index) != null) {
				return claimList.get(index);
			}
			
			else {
				return claimList.get(index+1);
			}
		}
		return claimList.get(index);
	}
	
	public Claim getClaim(Claim selected_claim) {
		if (claimList.contains(selected_claim)) {
			return selected_claim;
		}
		return selected_claim;
	}
	
	public ArrayList<Expense> selectExpense(Claim claim) throws IllegalStateException{
		// Grabbing expense from a selected claim
		try {
			return claim.getExpenseList();
			
		} catch (IllegalStateException e){
			throw new IllegalStateException();
		}
}
	
	public void addListener(Listener l) {
		getListeners().add(l);
	}
	
	public void removeListeners(Listener l) {
		getListeners().remove(l);
	}
	
}
