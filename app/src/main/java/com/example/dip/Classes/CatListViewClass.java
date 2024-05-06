package com.example.dip.Classes;

public class CatListViewClass {
    private final Integer CatID;
    private final String CatName;
    private final Boolean IncOn;
    private final Boolean ExcOn;

    public CatListViewClass(Integer id, String name, Boolean inc, Boolean exc) {
        CatID = id;
        CatName = name;
        IncOn = inc;
        ExcOn = exc;
    }

    public String getCatName() {
        return CatName;
    }

    public Boolean getIncOn() {
        return IncOn;
    }

    public Boolean getExcOn() {
        return ExcOn;
    }

    public Integer getCatID() {
        return CatID;
    }
}