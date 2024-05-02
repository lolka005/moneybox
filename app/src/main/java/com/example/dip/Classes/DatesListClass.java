package com.example.dip.Classes;

public class DatesListClass
{
    private String nameOfMonth;
    private String startDateOfMonth;
    private String endDateOfMonth;

    public DatesListClass(String name, String start, String end)
    {
        nameOfMonth = name;
        startDateOfMonth = start;
        endDateOfMonth = end;
    }

    public String getNameOfMonth() {
        return nameOfMonth;
    }

    public String getStartDateOfMonth() {
        return startDateOfMonth;
    }

    public String getEndDateOfMonth() {
        return endDateOfMonth;
    }
}
