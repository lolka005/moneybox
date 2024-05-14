package com.example.dip.Classes;

public class IncExcListViewClass {
    private final Integer ID;
    private final String CategoryName_Rus;
    private final String CategoryName_Eng;
    private final Float Sum;
    private final String CurrencyName;
    private final String Date;

    public IncExcListViewClass(Integer id, String categoryName_Rus,String categoryName_Eng, Float sum, String cur_name, String date) {
        ID = id;
        CategoryName_Rus = categoryName_Rus;
        CategoryName_Eng = categoryName_Eng;
        Sum = sum;
        CurrencyName = cur_name;
        Date = date;
    }

    public Integer getID() {
        return ID;
    }

    public String getCategoryNameRus() {
        return CategoryName_Rus;
    }
    public String getCategoryNameEng() {
        return CategoryName_Eng;
    }

    public Float getSum() {
        return Sum;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public String getDate() {
        return Date;
    }
}