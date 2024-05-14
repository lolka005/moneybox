package com.example.dip.Classes;

public class CatListViewClass {
    private final Integer CatID;
    private final String CatNameRus;
    private final String CatNameEng;
    private final Boolean IncOn;
    private final Boolean ExcOn;

    public CatListViewClass(Integer id, String nameRus,String nameEng, Boolean inc, Boolean exc) {
        CatID = id;
        CatNameRus = nameRus;
        CatNameEng = nameEng;
        IncOn = inc;
        ExcOn = exc;
    }

    public String getCatNameRus() {
        return CatNameRus;
    }
    public String getCatNameEng() {
        return CatNameEng;
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