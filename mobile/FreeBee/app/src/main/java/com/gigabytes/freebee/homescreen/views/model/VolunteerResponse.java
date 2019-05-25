package com.gigabytes.freebee.homescreen.views.model;

import java.util.List;

public class VolunteerResponse {

    int total;
    int page;
    int item_count_per_page;

    List<Volunteer> results;

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getItem_count_per_page() {
        return item_count_per_page;
    }

    public List<Volunteer> getResults() {
        return results;
    }
}
