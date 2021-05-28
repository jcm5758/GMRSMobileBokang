package com.geurimsoft.data;

/*
	<RESPONSE>
		<ID>65</ID>
		<Type>0</Type>
		<VehicleNum>02�� 5881</VehicleNum>
		<Product>40mm</Product>
		<Customer>�׸�����</Customer>
		<Unit>15.0</Unit>
		<ServiceDate>20140218</ServiceDate>
		<ServiceHour>163756</ServiceHour>
		</RESPONSE>
	<RESPONSE>
*/
public class SearchResult {
	private String id;
	private String type;
	private String vehicleNum;
	private String product;
	private String customer;
	private String unit;
	private String serviceDate;
	private String serviceHour;
	public int getIdI(){
		try{
			return Integer.parseInt(this.id);
		}catch(Exception e){
			return 0;
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVehicleNum() {
		return vehicleNum;
	}
	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getUnit() {
		return unit;
	}
	public double getUnitD(){
		try{
			return Double.parseDouble(this.unit);
		}catch(Exception e){
			return 0.0;
		}
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getServiceDate() {
		return serviceDate;
	}
	public int getServiceDateI() {
		 
		try{
			return Integer.parseInt(this.serviceDate);
		}catch(Exception e){
			return 0;
		}
	}
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
	
	public String getServiceHour() {
		return serviceHour;
	}
	public void setServiceHour(String serviceHour) {
		this.serviceHour = serviceHour;
	}
	
}
