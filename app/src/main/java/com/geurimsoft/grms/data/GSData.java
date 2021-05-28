package com.geurimsoft.grms.data;

public class GSData 
{

	private int id = -1;
    private String name = "";

    public GSData()
    {
    }

    public GSData(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public int getID()
    {
        return this.id;
    }

    public String getIDString()
    {
        return String.valueOf(this.id);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean checkUserByID(int id)
    {
        return (this.id == id);
    }

    public boolean checkByName(String name)
    {
        return (this.name.equals(name));
    }

    public String makeQueryString()
    {
        if (this.getID() == -1)
            return makeInsertString();
        else
            return makeUpdateString();
    }

    public String makeUpdateString()
    {
        return null;
    }

    public String makeInsertString()
    {
        return null;
    }
    
}
