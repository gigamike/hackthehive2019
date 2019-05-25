package com.gigabytes.freebee.login.models;

import java.util.List;
import java.util.Objects;

public class OFWResponse {

    private int item_count_per_page;
    private int page;
    private int total;

    private List<OFW> results;

    public int getItem_count_per_page() {
        return item_count_per_page;
    }

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    public List<OFW> getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OFWResponse that = (OFWResponse) o;
        return item_count_per_page == that.item_count_per_page &&
                page == that.page &&
                total == that.total &&
                Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_count_per_page, page, total, results);
    }

    @Override
    public String toString() {
        return "OFWResponse{" +
                "item_count_per_page=" + item_count_per_page +
                ", page=" + page +
                ", total=" + total +
                ", results=" + results +
                '}';
    }
}
