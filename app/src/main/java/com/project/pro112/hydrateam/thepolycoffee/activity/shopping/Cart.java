package com.project.pro112.hydrateam.thepolycoffee.activity.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.pro112.hydrateam.thepolycoffee.R;
import com.project.pro112.hydrateam.thepolycoffee.adapter.RecyclerViewAdapterCartInfo;
import com.project.pro112.hydrateam.thepolycoffee.adapter.RecyclerViewAdapterCartProduct;
import com.project.pro112.hydrateam.thepolycoffee.models.OrderedFood;
import com.project.pro112.hydrateam.thepolycoffee.tempdatabase.tempdatabase;

import java.util.ArrayList;

public class Cart extends AppCompatActivity implements View.OnClickListener {
    private Button btnDelivery;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private LinearLayoutManager mLayoutManager;
    private FragmentManager fragmentManager;

    private ImageView cartisEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        setUpToolbar();
        setUpRecyclerView();
        // Dấu nút giao hàng khi rê xuống bottom
//        setUpHideButtonWhenSrollToTheBottom();

    }

    //Recycler View
    private void setUpRecyclerView() {
        //Recycler view cho list product trên cùng
        // không đổi size của card trong content
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // chọn adapter
        RecyclerViewAdapterCartProduct mAdapter = new RecyclerViewAdapterCartProduct(this, fragmentManager);
        mRecyclerView.setAdapter(mAdapter);

        //Recycler view cho phần info bên dưới
        mRecyclerView2.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager);

        // chọn adapter
        RecyclerViewAdapterCartInfo mAdapter2 = new RecyclerViewAdapterCartInfo(this, fragmentManager);
        mRecyclerView2.setAdapter(mAdapter2);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView2.setNestedScrollingEnabled(false);
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
        txtTitle.setText("Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //init
    private void initView() {
        btnDelivery = (Button) findViewById(R.id.btnS);
        cartisEmpty = (ImageView) findViewById(R.id.showCartisEmpty);
        btnDelivery.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.mRecyclerView2);
    }

    //delivery and purchase click
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Cart.this, Purchase.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpRecyclerView();
        setViewCartEmpty();
    }

//    private void setUpHideButtonWhenSrollToTheBottom() {
//        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                View child = scrollView.getChildAt(0);
//                if (child != null) {
//                    int childHeight = child.getHeight();
//                    if (scrollView.getHeight() < childHeight) {
//                        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                            @Override
//                            public void onScrollChanged() {
//                                if (scrollView != null) {
//                                    if (scrollView.getChildAt(0).getBottom() == (scrollView.getHeight() + scrollView.getScrollY())) {
//                                        btnDelivery.setVisibility(View.INVISIBLE);
//                                    } else {
//                                        btnDelivery.setVisibility(View.VISIBLE);
//                                    }
//                                }
//                            }
//                        });
//                    } else {
//                        btnDelivery.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        });
//    }

    private void setViewCartEmpty(){
        tempdatabase tempdatabase = new tempdatabase(Cart.this);
        ArrayList<OrderedFood> orderedFoods =  tempdatabase.getOrderedFoods();
        if(orderedFoods.size()==0){
            cartisEmpty.setVisibility(View.VISIBLE);
            btnDelivery.setText("Continue purchase");
            btnDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            cartisEmpty.setVisibility(View.INVISIBLE);
            btnDelivery.setText("Delivery and purchase");
        }
    }
}
