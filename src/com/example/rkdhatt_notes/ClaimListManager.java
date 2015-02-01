/* Manages the singleton claim list for all activities, loads and saves data of claim list.
 * Idea obtained from Student picker video: https://www.youtube.com/watch?v=gmNfc6u1qk0
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class ClaimListManager {

	Context context;
	static final String prefFile = "ClaimListInfo";
	static final String clKey = "claimList";
	
	// Need to create singleton version of ClaimListManager
	static private ClaimListManager claimListManager = null;
	
	// need to make sure Manager is initialized
	public static void initManager(Context context) {
		if (claimListManager == null) {
			// context is null when we never called getManager before
			if (context == null) {
				throw new RuntimeException("Missing context for ClaimListManager");
			}
			claimListManager = new ClaimListManager(context);
		}
	}
	
	
	// getManager will tell you if Manager is initialized or not
	public static ClaimListManager getManager() {
		if (claimListManager == null) {
			throw new RuntimeException("Didn't initialize Manager!");
		}
		return claimListManager;
	}
	
	
	public ClaimListManager(Context context) {
		super();
		this.context = context;
	}
	

	public ClaimList loadClaimList() throws IOException, ClassNotFoundException {
		SharedPreferences app_settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
		String claimListData = app_settings.getString(clKey, "");
		
		if (claimListData.equals("")) {
			return new ClaimList();
		} else {
			return claimListFromString(claimListData);
		}
	}
	
	static public ClaimList claimListFromString(String claimListData) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(claimListData, Base64.DEFAULT));
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (ClaimList)ois.readObject();
	}

	public void saveClaimList(ClaimList cl) throws IOException {
		SharedPreferences app_settings = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
		Editor editor = app_settings.edit();
		editor.putString(clKey, claimListToString(cl));
		editor.commit();
	}

	static public String claimListToString(ClaimList cl) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(cl);
		oos.close();
		byte bytes [] = bos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}
}
