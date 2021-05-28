package com.geurimsoft.data;
/*
 * <RESPONSE>
<ID>5</ID>
<NAME>�׸�����</NAME>
</RESPONSE>
 * */
public class Product {
	private int id;
	private String name;
	
	public Product(){
		this.id = 0;
		this.name ="--선택--";
	}
	 
	public Product(int id, String name){
		this.id = id;
		this.name = name;
		
	}
	public int getId() {
		return id;
	}
	 
	public void setId(String id) {
		try{
			this.id = Integer.parseInt(id);			
		}catch(Exception e){
			this.id=0;
		}
	}
	public void setId(int id){
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
