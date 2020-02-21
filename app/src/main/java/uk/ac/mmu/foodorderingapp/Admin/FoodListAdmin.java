package uk.ac.mmu.foodorderingapp.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;
import uk.ac.mmu.foodorderingapp.Common.Common;
import uk.ac.mmu.foodorderingapp.Interface.ItemClickListener;
import uk.ac.mmu.foodorderingapp.Model.Category;
import uk.ac.mmu.foodorderingapp.Model.Food;
import uk.ac.mmu.foodorderingapp.R;
import uk.ac.mmu.foodorderingapp.ViewHolder.FoodViewHolderAdmin;
//import uk.ac.mmu.foodorderingapp.ViewHolder.FoodViewHolderAdmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

/**
 * FoodList activity created for the admin side
 */

public class FoodListAdmin extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FloatingActionButton fab;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolderAdmin> adapter;

    //add new food
    MaterialEditText editName, editDescription, editPrice, editDiscount;
    FButton btnSelect, btnUpload;

    Food newFood;

    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_admin);

        //init firebase
        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Restaurants").child("01")
                .child("detail").child("Food");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //init
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);


        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty())
            loadListFood(categoryId);
    }

    /**
     * Attempted to create a method which would allow admin to add food directly from the app however did not manage to get it running
     *
     * @param categoryId
     */
//
//    private void showAddFoodDialog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodListAdmin.this);
//        alertDialog.setTitle("Add a new item of food");
//        alertDialog.setMessage("Please fill in all of the information");
//
//        LayoutInflater inflater = this.getLayoutInflater();
//        View add_new_menu_layout = inflater.inflate(R.layout.add_new_food_admin_layout, null);
//
//        editName = add_new_menu_layout.findViewById(R.id.editName);
//        editDescription = add_new_menu_layout.findViewById(R.id.editDescription);
//        editPrice = add_new_menu_layout.findViewById(R.id.editPrice);
//        editDiscount = add_new_menu_layout.findViewById(R.id.editDiscount);
//
//        btnSelect = add_new_menu_layout.findViewById(R.id.btnSelect);
//        btnUpload = add_new_menu_layout.findViewById(R.id.btnUpload);
//
//        //event for button
//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chooseImage();
//            }
//        });
//
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                uploadImage();
//            }
//        });
//
//        alertDialog.setView(add_new_menu_layout);
//        alertDialog.setIcon(R.drawable.ic_playlist_add_black_24dp);
//
//        //set buttons
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.dismiss();
//
//                if(newFood != null)
//                {
//                    foodList.push().setValue(newFood);
//                    Snackbar.make(rootLayout, "New Food "+newFood.getName()+" was added", Snackbar.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        });
//
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.dismiss();
//
//            }
//        });
//
//        alertDialog.show();
//    }
//
//    private void uploadImage() {
//        if(saveUri != null)
//        {
//            final ProgressDialog mDialog = new ProgressDialog(this);
//            mDialog.setMessage("Uploading...");
//            mDialog.show();
//
//            String imageName = UUID.randomUUID().toString();
//            final StorageReference imageFolder = storageReference.child("images/"+imageName);
//            imageFolder.putFile(saveUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            mDialog.dismiss();
//                            Toast.makeText(FoodListAdmin.this, "Uploaded!", Toast.LENGTH_SHORT).show();
//                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    //set value for newCategory if image is uploaded
//                                    newFood = new Food();
//                                    newFood.setName(editName.getText().toString());
//                                    newFood.setDescription(editDescription.getText().toString());
//                                    newFood.setPrice(editPrice.getText().toString());
//                                    newFood.setDiscount(editDiscount.getText().toString());
//                                    newFood.setMenuId(categoryId);
//                                    newFood.setImage(uri.toString());
//                                }
//                            });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            mDialog.dismiss();
//                            Toast.makeText(FoodListAdmin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mDialog.setMessage("Uploaded"+progress+"%");
//                        }
//                    });
//        }
//    }
//
//    private void chooseImage() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE__REQUEST);
//    }

    //loads the list of food from the database
    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolderAdmin>(
                Food.class,
                R.layout.food_item_admin,
                FoodViewHolderAdmin.class,
                foodList.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolderAdmin foodViewHolderAdmin, Food model, int position) {
                foodViewHolderAdmin.food_name.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(foodViewHolderAdmin.food_image);

                foodViewHolderAdmin.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //code later
                    }
                });

            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    /**
     * This is carrying on from the attempted method as above
     * Would allow user to upload a picture
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == Common.PICK_IMAGE__REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null)
//        {
//            saveUri = data.getData();
//            btnSelect.setText("Image Selected");
//        }
//    }
//
}
