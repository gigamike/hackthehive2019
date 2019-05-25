package com.gigabytes.freebee.homescreen.views.model;

import java.util.Comparator;
import java.util.Objects;

public class LocalContactsModel {
    private String name;
    private String number;

    public static Comparator<LocalContactsModel> ALPHABETICAL_ORDER = (o1, o2) -> {
        int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
        if (res == 0) {
            res = o1.getName().compareTo(o2.getName());
        }
        return res;
    };

    public LocalContactsModel(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalContactsModel that = (LocalContactsModel) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, number);
    }

    @Override
    public String toString() {
        return "LocalContactsModel{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
