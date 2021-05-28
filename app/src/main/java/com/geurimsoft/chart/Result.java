package com.geurimsoft.chart;

import java.util.Date;

public class Result implements Comparable<Result>
{

	private Date date;
    private Double value;
    
    public Result(Date date, Double value) 
    {
        this.date = date;
        this.value = value;
    }
    
    public Date getDate() 
    {
        return date;
    }
    
    public Double getValue() 
    {
        return value;
    }
    
    @Override
    public int compareTo(Result rs) 
    {
        return value.compareTo(rs.getValue());
    }
    
}
