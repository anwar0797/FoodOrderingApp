package uk.ac.mmu.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;
import uk.ac.mmu.foodorderingapp.Model.Request;
import uk.ac.mmu.foodorderingapp.ViewHolder.OrderViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CollectionConfirmation extends AppCompatActivity {

    TextView confirmationText, orderDetails, collection, collectionNumber, collectTime, collectionTime, confirmationDetailText;
    FButton btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_confirmation);

        confirmationText = (TextView)findViewById(R.id.confirmationText);
        orderDetails = (TextView)findViewById(R.id.orderDetails);
        collection = (TextView)findViewById(R.id.collection);
        //collectionNumber = (TextView)findViewById(R.id.collectionNumber);
        collectTime = (TextView)findViewById(R.id.collectTime);
        collectionTime = (TextView)findViewById(R.id.collectionTime);
        confirmationDetailText = (TextView)findViewById(R.id.confirmationDetailText);
        btnConfirm = (FButton)findViewById(R.id.btnConfirm);

        collection.setText("Collection Number: " + randInt(1000, 2000));


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollectionConfirmation.this, Home.class);
                startActivity(intent);
            }
        });



    }

    public static int randInt(int start, int finish)
    {
        Random rnd = new Random();
        int randInt = rnd.nextInt(finish + 1 - start ) + start;
        return randInt;
    }

}
