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

// Source: https://www.youtube.com/watch?v=uLnoI7mbuEo 2015-01-06
package com.example.rkdhatt_notes;

import java.io.IOException;

public class ClaimListController {
	
	// Only one claim list made.
	private static ClaimList claimList = null;
	
	// Throw RuntimeEXception
	// Need to have listener so that data can be saved
	static public ClaimList getClaimList() {
		if (claimList == null) {
			try {
				claimList = ClaimListManager.getManager().loadClaimList();
				claimList.addListener(new Listener() {
					@Override
					public void update() {
						saveClaimList();	
					}	
				});
				
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not deserialize ClaimList from ClaimListManager");
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not deserialize ClaimList from ClaimListManager");
			}
		}
		
		return claimList;	
	}
	
	static public void saveClaimList () {
		try {
			ClaimListManager.getManager().saveClaimList(getClaimList());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not deserialize ClaimList from ClaimListManager");
		}
	}

	public Claim chooseClaim() throws IllegalStateException {
		return getClaimList().chooseClaim();
	}

	public void addClaim(Claim new_claim) {
		getClaimList().addClaim(new_claim);
	
		
	}
}
