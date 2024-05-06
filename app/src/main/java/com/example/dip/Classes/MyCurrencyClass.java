package com.example.dip.Classes;

public class MyCurrencyClass {
    private final String Name;
    private final Float Value;

    public MyCurrencyClass(String name, Float value) {
        Name = name;
        Value = value;
    }

    public String getName() {
        return Name;
    }

    public Float getValue() {
        return Value;
    }

}