/* Class for expenses containing setters and getters.
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

public class Expense implements Serializable{

	private static final long serialVersionUID = 1344903020799401397L;
	private static String ex_name = "unknown";
	private static String ex_date = "unknown";
	private static String ex_category = "unknown";
	private static String ex_info = "unknown";
	private static float ex_amount = 0;
	private static String ex_currency = "unknown";
	
	// Expense constructor
	public Expense (String name, String date, String category, String info, float amount, String currency) {
		super();
		Expense.ex_name = name;
		Expense.ex_date = date;
		Expense.ex_category = category;
		Expense.ex_info = info;
		Expense.ex_amount = amount;
		Expense.ex_currency =currency;	
	}
	
	// Create getter and setter methods for each private variable of expense.
	public String getExpenseName() {
		return ex_name;
	}
	
	public void setExpenseName(String new_name) {
		Expense.ex_name = new_name;
	}
	
	public String getExpenseDate()  {
		return ex_date;
	}
	
	public void setExpenseDate(String string) {
		Expense.ex_date = string;
	}
	
	public String getExpenseCat() {
		return ex_category;
	}
	
	public void setExpenseCat(String new_category) {
		Expense.ex_category = new_category;
	}
	
	public String getExpenseInfo() {
		return ex_info;
	}
	
	public void setExpenseInfo(String new_info) {
		Expense.ex_info = new_info;
	}
	
	public float getExpenseAmt() {
		return ex_amount;
	}
	
	public void setExpenseAmt(float new_amount) {
		Expense.ex_amount = new_amount;
	}
	
	public String getExpenseCurr() {
		return ex_currency;
	}
	
	public void setExpenseCurr(String new_currency) {
		Expense.ex_currency = new_currency;
	}
}