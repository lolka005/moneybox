package com.example.dip.Classes;

public class DatesListClass {
    private final String nameOfMonth;
    private final String startDateOfMonth;
    private final String endDateOfMonth;

    public DatesListClass(String name, String start, String end) {
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
