package com.example.dip.Classes;

public class MovesTypesClass {
    private final Integer TypeID;
    private final String Type_Name_Rus;
    private final String Type_Name_Eng;

    public MovesTypesClass(Integer id, String name_Rus, String name_Eng) {
        TypeID = id;
        Type_Name_Rus = name_Rus;
        Type_Name_Eng = name_Eng;
    }

    public Integer getTypeID() {
        return TypeID;
    }

    public String getType_Name_Rus() {
        return Type_Name_Rus;
    }
    public String getType_Name_Eng() {
        return Type_Name_Eng;
    }
}