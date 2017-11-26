package com.project.pro112.hydrateam.thepolycoffee.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.pro112.hydrateam.thepolycoffee.R;

public class Purchase extends AppCompatActivity implements View.OnClickListener{
    private ScrollView scrollView;
    private Button order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        initView();
        setUpToolbar();
        //như bên cart
        setUpHideButtonWhenSrollToTheBottom();
    }


    //init
    private void initView() {
        order = (Button) findViewById(R.id.btnViewCart);
        order.setText("Order");
        order.setOnClickListener(this);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }
    //Setup tool bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView txtTitle = (TextView) findViewById(R.id.tvTitleToolbar);
        toolbar.setTitle("");
        txtTitle.setText("Information and purchase");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpHideButtonWhenSrollToTheBottom() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView != null) {
                    if (scrollView.getChildAt(0).getBottom() == (scrollView.getHeight() + scrollView.getScrollY())) {
                        order.setVisibility(View.INVISIBLE);
                    } else{
                        order.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    // click vào nút order
    @Override
    public void onClick(View v) {
        Toast.makeText(this, "click vào order trong purcahse activity", Toast.LENGTH_SHORT).show();
    }
}