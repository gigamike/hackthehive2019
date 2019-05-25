package com.gigabytes.freebee.credits.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gigabytes.freebee.R;
import com.gigabytes.freebee.credits.views.adapters.BuyCreditsMenuListRecyclerViewAdapter;

import static android.widget.LinearLayout.VERTICAL;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_credits);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Credits");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.buttonBuyCreditsMenuRecyclerView);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BuyCreditsMenuListRecyclerViewAdapter(getApplicationContext(), this));


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
