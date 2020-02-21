package uk.ac.mmu.foodorderingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import uk.ac.mmu.foodorderingapp.Common.Common;
import uk.ac.mmu.foodorderingapp.Interface.ItemClickListener;
import uk.ac.mmu.foodorderingapp.Model.Category;
import uk.ac.mmu.foodorderingapp.Model.Restaurant;
import uk.ac.mmu.foodorderingapp.ViewHolder.MenuViewHolder;
import uk.ac.mmu.foodorderingapp.ViewHolder.RestaurantViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Loads up the list of restaurants from database
 */

public class RestaurantList extends AppCompatActivity {

    AlertDialog waitingDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseDatabase database;
    DatabaseReference category;

    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
//
//
        //view
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
//
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//
//
                if (Common.isConnectedToInternet((getBaseContext())))
                    loadRestaurant();
                else {
                    Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
//                    return;
                }
//
            }
        });


        //default, load for the first time
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (Common.isConnectedToInternet(getBaseContext()))
                    loadRestaurant();
                else {
                    Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        //init firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Restaurants");

//        adapter = new FirebaseRecyclerAdapter<Category, RestaurantViewHolder>(Restaurant.class,R.layout.menu_item,RestaurantViewHolder.class,category) {
//            @Override
//            protected void populateViewHolder(RestaurantViewHolder viewHolder, Restaurant model, int position) {
//                viewHolder.txt_restaurant_name.setText(model.getName());
//                Picasso.with(getBaseContext()).load(model.getImage())
//                        .into(viewHolder.img_restaurant);
//                final Restaurant clickItem = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        //get category id and send to new activity
//                        Intent foodList = new Intent(RestaurantList.this,Home.class);
//                        //category is key so get key of item
//                        foodList.putExtra("CategoryId",  adapter.getRef(position).getKey());
//                        startActivity(foodList);
//                    }
//                });
//            }
//        };

        adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(Restaurant.class, R.layout.restaurant_item, RestaurantViewHolder.class, category) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder viewHolder, Restaurant model, int position) {
                viewHolder.txt_restaurant_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.img_restaurant);
                final Restaurant clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get category id and send to new activity
                        Intent foodList = new Intent(RestaurantList.this, Home.class);
                        //save restaurant id when restaurant is selected
                        Common.restaurantSelected = adapter.getRef(position).getKey();
                        startActivity(foodList);
                    }
                });

            }
        };

        //load menu
        recyclerView = (RecyclerView) findViewById(R.id.recycler_restaurant);
        //layoutManager = new LinearLayoutManager(this);
        //recycler_menu.setLayoutManager(layoutManager);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//
    }

    private void loadRestaurant() {


        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
