package com.geurimsoft.data;
/*
 * <RESPONSE>
<ID>5</ID>
<NAME>�׸�����</NAME>
</RESPONSE>
 * */
public class Vehicle {
	private int id;
	private String name;
	private String vehiclenum;
	
	public String getVehiclenum() {
		return vehiclenum;
	}

	public void setVehiclenum(String vehiclenum) {
		this.vehiclenum = vehiclenum;
	}

	public Vehicle(){
		this.id = 0;
		this.name ="--선택--";
		this.vehiclenum ="--선택--";
	}
	 
	public Vehicle(int id, String name){
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
