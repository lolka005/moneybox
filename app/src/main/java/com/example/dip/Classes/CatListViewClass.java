package com.example.dip.Classes;

public class CatListViewClass
{
    private Integer CatID;
    private String CatName;
    private Boolean IncOn;
    private Boolean ExcOn;

    public CatListViewClass(Integer id, String name, Boolean inc, Boolean exc)
    {
        CatID = id;
        CatName = name;
        IncOn = inc;
        ExcOn = exc;
    }

    public String getCatName()
    {
        return CatName;
    }

    public Boolean getIncOn()
    {
        return IncOn;
    }

    public Boolean getExcOn()
    {
        return ExcOn;
    }

    public Integer getCatID() { return CatID; }
}