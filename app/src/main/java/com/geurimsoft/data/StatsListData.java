package com.geurimsoft.data;

import java.util.ArrayList;

public class StatsListData {

	private int header_count;
	private String[] header_titles;
	
	private int item_count;
	private ArrayList<String[]> items_list;
	private String[] total_items;

	public StatsListData() {
		
	}

	public int getHeader_count() {
		return header_count;
	}

	public void setHeader_count(int header_count) {
		this.header_count = header_count;
	}

	public int getItem_count() {
		return item_count;
	}

	public void setItem_count(int item_count) {
		this.item_count = item_count;
	}

	public String[] getTotal_items() {
		return total_items;
	}

	public void setTotal_items(String[] total_items) {
		this.total_items = total_items;
	}

	public String[] getHeader_titles() {
		return header_titles;
	}

	public void setHeader_titles(String[] header_titles) {
		this.header_titles = header_titles;
	}

	public ArrayList<String[]> getItems_list() {
		return items_list;
	}

	public void setItems_list(ArrayList<String[]> items_list) {
		this.items_list = items_list;
	}
	
	
}
