package uk.ac.mmu.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.mmu.foodorderingapp.Common.Common;
import uk.ac.mmu.foodorderingapp.Database.Database;
import uk.ac.mmu.foodorderingapp.Helper.RecyclerItemTouchHelper;
import uk.ac.mmu.foodorderingapp.Interface.RecyclerItemTouchHelperListener;
import uk.ac.mmu.foodorderingapp.Model.Favourites;
import uk.ac.mmu.foodorderingapp.Model.Order;
import uk.ac.mmu.foodorderingapp.ViewHolder.FavouritesAdapter;
import uk.ac.mmu.foodorderingapp.ViewHolder.FavouritesViewHolder;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rey.material.widget.SnackBar;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * this class will load the favourites of the user
 */

public class FavouritesActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FavouritesAdapter adapter;

    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        rootLayout = (RelativeLayout)findViewById(R.id.root_layout);


        recyclerView = (RecyclerView)findViewById(R.id.recycler_fav);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        loadFavourites();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Favourites");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    private void loadFavourites() {
        adapter = new FavouritesAdapter(this,new Database(this).getAllFavourites(Common.currentUser.getPhone()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof FavouritesViewHolder)
        {
            String name = ((FavouritesAdapter)recyclerView.getAdapter()).getItem(position).getFoodName();

            final Favourites deleteItem = ((FavouritesAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(viewHolder.getAdapterPosition());
            new Database(getBaseContext()).removeFromFavourites(deleteItem.getFoodId(), Common.currentUser.getPhone());

            //make snackbar
            Snackbar snackbar = Snackbar.make(rootLayout, name + " removed from favourites!",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addToFavourites(deleteItem);

                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            FavouritesActivity.super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
