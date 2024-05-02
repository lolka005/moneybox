package com.example.dip.Classes;

public class IncExcListViewClass
{
    private Integer ID;
    private String CategoryName;
    private Float Sum;
    private String CurrencyName;
    private String Date;

    public IncExcListViewClass(Integer id, String categoryName, Float sum, String cur_name, String date)
    {
        ID = id;
        CategoryName = categoryName;
        Sum = sum;
        CurrencyName = cur_name;
        Date = date;
    }

    public Integer getID() {
        return ID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public Float getSum() {
        return Sum;
    }

    public String getCurrencyName() { return CurrencyName; }

    public String getDate() { return Date;}
}