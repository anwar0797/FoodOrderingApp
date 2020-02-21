package uk.ac.mmu.foodorderingapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.mmu.foodorderingapp.Common.Common;
import uk.ac.mmu.foodorderingapp.Database.Database;
import uk.ac.mmu.foodorderingapp.FoodDetail;
import uk.ac.mmu.foodorderingapp.FoodList;
import uk.ac.mmu.foodorderingapp.Interface.ItemClickListener;
import uk.ac.mmu.foodorderingapp.Model.Favourites;
import uk.ac.mmu.foodorderingapp.Model.Food;
import uk.ac.mmu.foodorderingapp.Model.Order;
import uk.ac.mmu.foodorderingapp.R;

/**
 * Favoruites adapter class which allows to add or remove items from their favourites
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesViewHolder> {

    private Context context;
    private List<Favourites> favouritesList;

    public FavouritesAdapter(Context context, List<Favourites> favouritesList) {
        this.context = context;
        this.favouritesList = favouritesList;
    }

    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favourites_item, parent, false);
        return new FavouritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouritesViewHolder viewHolder, final int position) {
        viewHolder.food_name.setText(favouritesList.get(position).getFoodName());
        viewHolder.food_price.setText(String.format("£ %s", favouritesList.get(position).getFoodPrice().toString()));
        Picasso.with(context).load(favouritesList.get(position).getFoodImage())
                .into(viewHolder.food_image);

        //quick cart
        viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExists = new Database(context).checkFoodExists(favouritesList.get(position).getFoodId(), Common.currentUser.getPhone());
                if (!isExists) {
                    new Database(context).addToCart(new Order(
                            Common.currentUser.getPhone(),
                            favouritesList.get(position).getFoodId(),
                            favouritesList.get(position).getFoodName(),
                            "1",
                            favouritesList.get(position).getFoodPrice(),
                            favouritesList.get(position).getFoodDiscount(),
                            favouritesList.get(position).getFoodImage()

                    ));

                } else {
                    new Database(context).increaseCart(Common.currentUser.getPhone(),
                            favouritesList.get(position).getFoodId());
                }

                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();

            }
        });


        final Favourites local = favouritesList.get(position);
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //Start new activity
                Intent foodDetail = new Intent(context, FoodDetail.class);
                foodDetail.putExtra("FoodId", favouritesList.get(position).getFoodId()); //send food id to new activity
                context.startActivity(foodDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public void removeItem(int position) {
        favouritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favourites item, int position) {
        favouritesList.add(position, item);
        notifyItemInserted(position);
    }

    public Favourites getItem(int position) {
        return favouritesList.get(position);
    }

}
