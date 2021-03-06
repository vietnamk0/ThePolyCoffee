package com.project.pro112.hydrateam.thepolycoffee.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.pro112.hydrateam.thepolycoffee.R;
import com.project.pro112.hydrateam.thepolycoffee.adapter.RecyclerViewAdapterPolularDish;
import com.project.pro112.hydrateam.thepolycoffee.models.Food;
import com.project.pro112.hydrateam.thepolycoffee.tool.SpaceBetweenGrid;

import java.util.ArrayList;

import static com.project.pro112.hydrateam.thepolycoffee.activity.shopping.Order.linearButtonViewCart;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularDish extends Fragment{


    public PopularDish() {
        // Required empty public constructor
    }
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private FragmentManager fragmentManager;
    private int numberOfColums = 2;
    public static boolean imHere = true;
    private ArrayList<Food> foods;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_dish, container, false);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        fragmentManager = getFragmentManager();
        setUpRecyclerView();
//        hideButtonViewCart();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        RecyclerViewAdapterPolularDish mAdapter = new RecyclerViewAdapterPolularDish(getContext(), fragmentManager, foods);
        if(mAdapter!=null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void setUpRecyclerView() {
        // không đổi size của card trong content
        foods = new ArrayList<>();
        mLayoutManager = new GridLayoutManager(getContext(),numberOfColums);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        myRef = database.getReference("Foods/Popular");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foods = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String name = (String) messageSnapshot.child("name").getValue();
                    String des = (String) messageSnapshot.child("discription").getValue();
                    String image = (String) messageSnapshot.child("image").getValue();
                    String price = (String) messageSnapshot.child("price").getValue();
                    String keyNe = (String) messageSnapshot.getKey();
                    Food food = new Food(des, image, name, price,keyNe);
                    foods.add(food);
                }
                RecyclerViewAdapterPolularDish mAdapter = new RecyclerViewAdapterPolularDish(getContext(), fragmentManager, foods);
                if(mAdapter!=null) {
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        int spanCount = 2; // 2 columns
        int spacing = 20; // 20px
        boolean includeEdge = true;
        mRecyclerView.addItemDecoration(new SpaceBetweenGrid(spanCount, spacing, includeEdge));
    }

//    private void hideButtonViewCart() {
//        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                // có data số 7 đổi lại lại số lượng - 1
//                if(dy > 0 && linearButtonViewCart.getVisibility() == View.VISIBLE)
//                    linearButtonViewCart.setVisibility(View.INVISIBLE);
//                else if(dy < 0 && linearButtonViewCart.getVisibility() == View.INVISIBLE)
//                    linearButtonViewCart.setVisibility(View.VISIBLE);
//            }
//
//        });
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
