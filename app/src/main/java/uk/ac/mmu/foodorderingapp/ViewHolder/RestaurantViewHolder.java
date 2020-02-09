package uk.ac.mmu.foodorderingapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.mmu.foodorderingapp.Interface.ItemClickListener;
import uk.ac.mmu.foodorderingapp.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_restaurant_name;
    public ImageView img_restaurant;

    private ItemClickListener itemClickListener;


    public RestaurantViewHolder(View itemView) {
        super(itemView);

        txt_restaurant_name = (TextView)itemView.findViewById(R.id.restaurant_name);
        img_restaurant = (ImageView)itemView.findViewById(R.id.restaurant_image);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
