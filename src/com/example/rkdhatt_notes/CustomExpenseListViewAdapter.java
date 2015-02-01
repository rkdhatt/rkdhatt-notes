package com.example.rkdhatt_notes;

//Source: 
// http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/ 2015-01-26

import java.util.ArrayList;
import java.util.List;

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
	private ArrayList<Expense> list;
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
		Expense expense;
		TextView txtTitle;
		TextView txtDesc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			
			row = LayoutInflater.from(getContext()).inflate(R.layout.expense_items, parent, false);
			//LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			//row = inflater.inflate(loID, parent, false);
			holder = new ViewHolder();
			holder.expense = list.get(position);
			holder.txtDesc = (TextView) row.findViewById(R.id.price_info);
			holder.txtTitle = (TextView) row.findViewById(R.id.title);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		String cur_amount = Float.valueOf(holder.expense.getExpenseAmt()).toString() + " " + holder.expense.getExpenseCurr();
		holder.txtDesc.setText(cur_amount); // gives more info about currency and amount
		
		holder.txtTitle.setText(holder.expense.getExpenseName());
		return row;
	}
}
