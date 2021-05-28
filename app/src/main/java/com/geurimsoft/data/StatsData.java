package com.geurimsoft.data;

import java.util.ArrayList;

public class StatsData {
	private int stock_header_count;
	private String[] stock_header;
	private int stock_count;
	private String stock_total;
	private String stock_several;
	private ArrayList<String[]> stock_list;
	
	private int release_header_count;
	private String[] release_header;
	private int release_count;
	private String release_total;
	private String release_several;
	private ArrayList<String[]> release_list;
	
	private int petosa_header_count;
	private String[] petosa_header;
	private int petosa_count;
	private String petosa_total;
	private String petosa_several;
	private ArrayList<String[]> petosa_list;

	public StatsData() {
		
	}

	public int getStock_header_count() {
		return stock_header_count;
	}

	public void setStock_header_count(int stock_header_count) {
		this.stock_header_count = stock_header_count;
	}

	public String[] getStock_header() {
		return stock_header;
	}

	public void setStock_header(String[] stock_header) {
		this.stock_header = stock_header;
	}

	public int getStock_count() {
		return stock_count;
	}

	public void setStock_count(int stock_count) {
		this.stock_count = stock_count;
	}

	public ArrayList<String[]> getStock_list() {
		return stock_list;
	}

	public void setStock_list(ArrayList<String[]> stock_list) {
		this.stock_list = stock_list;
	}

	public int getRelease_header_count() {
		return release_header_count;
	}

	public void setRelease_header_count(int release_header_count) {
		this.release_header_count = release_header_count;
	}

	public String[] getRelease_header() {
		return release_header;
	}

	public void setRelease_header(String[] release_header) {
		this.release_header = release_header;
	}

	public int getRelease_count() {
		return release_count;
	}

	public void setRelease_count(int release_count) {
		this.release_count = release_count;
	}

	public ArrayList<String[]> getRelease_list() {
		return release_list;
	}

	public void setRelease_list(ArrayList<String[]> release_list) {
		this.release_list = release_list;
	}

	public int getPetosa_header_count() {
		return petosa_header_count;
	}

	public void setPetosa_header_count(int petosa_header_count) {
		this.petosa_header_count = petosa_header_count;
	}

	public String[] getPetosa_header() {
		return petosa_header;
	}

	public void setPetosa_header(String[] petosa_header) {
		this.petosa_header = petosa_header;
	}

	public int getPetosa_count() {
		return petosa_count;
	}

	public void setPetosa_count(int petosa_count) {
		this.petosa_count = petosa_count;
	}

	public ArrayList<String[]> getPetosa_list() {
		return petosa_list;
	}

	public void setPetosa_list(ArrayList<String[]> petosa_list) {
		this.petosa_list = petosa_list;
	}

	public String getStock_total() {
		return stock_total;
	}

	public void setStock_total(String stock_total) {
		this.stock_total = stock_total;
	}

	public String getRelease_total() {
		return release_total;
	}

	public void setRelease_total(String release_total) {
		this.release_total = release_total;
	}

	public String getPetosa_total() {
		return petosa_total;
	}

	public void setPetosa_total(String petosa_total) {
		this.petosa_total = petosa_total;
	}

	public String getStock_several() {
		return stock_several;
	}

	public void setStock_several(String stock_several) {
		this.stock_several = stock_several;
	}

	public String getRelease_several() {
		return release_several;
	}

	public void setRelease_several(String release_several) {
		this.release_several = release_several;
	}

	public String getPetosa_several() {
		return petosa_several;
	}

	public void setPetosa_several(String petosa_several) {
		this.petosa_several = petosa_several;
	}
	
	
}
