/* Custom ArrayList Adpater for expense list of claim
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

//Source: 
// http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/ 2015-01-26

import java.util.ArrayList;

import com.example.rkdhatt_notes.R;
import com.example.rkdhatt_notes.Expense;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomExpenseListViewAdapter extends ArrayAdapter<Expense> {

	
	private Context context;
	private ArrayList<Expense> list = null;
	private int loID;

	public CustomExpenseListViewAdapter(Context context,
			int textViewResourceId, ArrayList<Expense> items) {

		super(context, textViewResourceId, items);
		this.context = context;
		this.loID = textViewResourceId;
		this.list = items;

	}

	/* private view holder class */
	private class ViewHolder {
		//Expense expense;
		TextView txtTitle;
		TextView txtDesc;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(loID, parent, false);
			holder = new ViewHolder();
			
			holder.txtDesc = (TextView) row.findViewById(R.id.price_info);
			holder.txtTitle = (TextView) row.findViewById(R.id.title);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		
		Expense expense = list.get(position);
		String cur_amount = Float.valueOf(expense.getExpenseAmt()).toString() + " " + expense.getExpenseCurr();
		holder.txtDesc.setText(cur_amount); // gives more info about currency and amount
		
		holder.txtTitle.setText(expense.getExpenseName());
		return row;
	}
}
