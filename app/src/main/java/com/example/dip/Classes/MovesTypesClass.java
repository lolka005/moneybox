package com.example.dip.Classes;

public class MovesTypesClass
{
    private Integer TypeID;
    private String Type_Name;

    public MovesTypesClass(Integer id, String name)
    {
        TypeID = id;
        Type_Name = name;
    }

    public Integer getTypeID() {
        return TypeID;
    }

    public String getType_Name() {
        return Type_Name;
    }
}