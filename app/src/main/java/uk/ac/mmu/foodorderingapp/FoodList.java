package uk.ac.mmu.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import uk.ac.mmu.foodorderingapp.Common.Common;
import uk.ac.mmu.foodorderingapp.Database.Database;
import uk.ac.mmu.foodorderingapp.Interface.ItemClickListener;
import uk.ac.mmu.foodorderingapp.Model.Favourites;
import uk.ac.mmu.foodorderingapp.Model.Food;
import uk.ac.mmu.foodorderingapp.Model.Order;
import uk.ac.mmu.foodorderingapp.ViewHolder.FoodViewHolder;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //search functionality
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //favourites
    Database localDB;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/restaurant_font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_food_list);

        //firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        //local db
        localDB = new Database(this);

        //swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get intent from home activity
                if(getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty() && categoryId != null)
                {

                    if(Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
                    else
                    {
                        Toast.makeText(FoodList.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //get intent from home activity
                if(getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty() && categoryId != null)
                {
                    if(Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
                    else
                    {
                        Toast.makeText(FoodList.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //search
                materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
                materialSearchBar.setHint("Enter Food Name");
                //materialSearchBar.setSpeechMode(false);
                loadSuggest();


                materialSearchBar.setCardViewElevation(10);
                materialSearchBar.addTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        //when user types then suggest list will chaneg

                        List<String> suggest = new ArrayList<String>();
                        for(String search:suggestList) //loop of suggest list
                        {
                            if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                                suggest.add(search);
                        }
                        materialSearchBar.setLastSuggestions(suggest);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                    @Override
                    public void onSearchStateChanged(boolean enabled) {
                        //when search bar is closed
                        //restore original adapter
                        if(!enabled)
                            recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onSearchConfirmed(CharSequence text) {
                        //when search finishes
                        //show result of search adapter
                        startSearch(text);

                    }



                    @Override
                    public void onButtonClicked(int buttonCode) {

                    }
                });
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }


        //SEACRH VIEW HOLDER
    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                viewHolder.food_price.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);


                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent foodDetail = new Intent (FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",searchAdapter.getRef(position).getKey()); //send food id to new activity
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName()); //add name to suggest list
                        }

                        materialSearchBar.setLastSuggestions(suggestList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    //MAIN VIEWHOLDER

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)
                ) {
            @Override
            protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.food_name.setText(model.getName());
                viewHolder.food_price.setText(String.format("£ %s", model.getPrice().toString()));
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);

                //quick cart
                    viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean isExists = new Database(getBaseContext()).checkFoodExists(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
                            if (!isExists) {
                                new Database(getBaseContext()).addToCart(new Order(
                                        Common.currentUser.getPhone(),
                                        adapter.getRef(position).getKey(),
                                        model.getName(),
                                        "1",
                                        model.getPrice(),
                                        model.getDiscount(),
                                        model.getImage()

                                ));

                            } else {
                                new Database(getBaseContext()).increaseCart(Common.currentUser.getPhone(), adapter.getRef(position).getKey());
                            }

                            Toast.makeText(FoodList.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                        }
                    });





                //Add favourites
                if(localDB.isFavourite(adapter.getRef(position).getKey(),Common.currentUser.getPhone()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                //click to change state of favourites
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Favourites favourites = new Favourites();
                        favourites.setFoodId(adapter.getRef(position).getKey());
                        favourites.setFoodName(model.getName());
                        favourites.setFoodDescription(model.getDescription());
                        favourites.setFoodDiscount(model.getDiscount());
                        favourites.setFoodImage(model.getImage());
                        favourites.setFoodMenuId(model.getMenuId());
                        favourites.setUserPhone(Common.currentUser.getPhone());
                        favourites.setFoodPrice(model.getPrice());


                        if(!localDB.isFavourite(adapter.getRef(position).getKey(),Common.currentUser.getPhone()))
                        {
                            localDB.addToFavourites(favourites);
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodList.this, ""+model.getName()+" was added to favourites ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            localDB.removeFromFavourites(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(FoodList.this, ""+model.getName()+" was removed from favourites ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent foodDetail = new Intent (FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey()); //send food id to new activity
                        startActivity(foodDetail);
                    }
                });

            }
        };

        //set adapter
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }
}
