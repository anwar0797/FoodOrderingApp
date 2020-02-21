package uk.ac.mmu.foodorderingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uk.ac.mmu.foodorderingapp.Common.Common;
import uk.ac.mmu.foodorderingapp.Common.Config;
import uk.ac.mmu.foodorderingapp.Database.Database;
import uk.ac.mmu.foodorderingapp.Helper.RecyclerItemTouchHelper;
import uk.ac.mmu.foodorderingapp.Interface.RecyclerItemTouchHelperListener;
import uk.ac.mmu.foodorderingapp.Model.Collection;
import uk.ac.mmu.foodorderingapp.Model.Order;
import uk.ac.mmu.foodorderingapp.Model.Request;
import uk.ac.mmu.foodorderingapp.ViewHolder.CartAdapter;
import uk.ac.mmu.foodorderingapp.ViewHolder.CartViewHolder;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * main class for cart page
 * here button clicks are implemented for user to choose deliverly methods and payment methods
 * When user clicks delivery, can choose either paypal or card, both methods do not charge
 */

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final int PAYPAL_REQUEST_CODE = 9999;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    //paypal service
    static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    String address, comment;

    RelativeLayout rootLayout;


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

        setContentView(R.layout.activity_cart);

        //init paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Orders");

        //init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.btnPlaceOrder);


        //creating button for place order
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make a new request

                if (cart.size() > 0)
                    ShowAlertDialog();
                else {
                    Toast.makeText(Cart.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loadListFood();


    }

    private void ShowAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step...");
        alertDialog.setMessage("Please enter your address");

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_address_comment, null);

        final MaterialEditText edtAddress = (MaterialEditText) order_address_comment.findViewById(R.id.edtAddress);
        final MaterialEditText edtComment = (MaterialEditText) order_address_comment.findViewById(R.id.edtComment);

        final RadioButton btnCollection = (RadioButton) order_address_comment.findViewById(R.id.btnCollection);
        final RadioButton btnDelivery = (RadioButton) order_address_comment.findViewById(R.id.btnDelivery);


        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //show paypal screen for payment
                //first get address and comment from alert dialog
                address = edtAddress.getText().toString();
                comment = edtComment.getText().toString();

                if (TextUtils.isEmpty(address) && (btnCollection.isChecked() || btnDelivery.isChecked())) {
                    Toast.makeText(Cart.this, "Please enter address", Toast.LENGTH_SHORT).show();
                } else if (!btnCollection.isChecked() && !btnDelivery.isChecked()) {
                    Toast.makeText(Cart.this, "Please select a delivery option", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(address) && btnDelivery.isChecked()) {

                    String formatAmount = txtTotalPrice.getText().toString()
                            .replace("£", "")
                            .replace(",", "");

                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(formatAmount),
                            "GBP",
                            "Food App Order",
                            PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);


                /*
                //create new request
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getUsername(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        "0", //status
                        edtComment.getText().toString(),
                        cart
                );

                //submit information to firebase
                //using System.milli to key
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                //delete cart
                new Database(getBaseContext()).cleanCart(Common.currentUser.getPhone());
                Toast.makeText(Cart.this, "Thank you, your order has been placed.", Toast.LENGTH_SHORT).show();
                finish();

                 */


                } else if (!TextUtils.isEmpty(address) && btnCollection.isChecked()) {
                    //create new request
                    Collection request = new Collection(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getUsername(),
                            address,
                            txtTotalPrice.getText().toString(),
                            "0", //status
                            comment,
                            "Pay on Collection",
                            "Unpaid",
                            cart
                    );

                    //submit information to firebase
                    //using System.milli to key
                    requests.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);

                    //delete cart
                    new Database(getBaseContext()).cleanCart(Common.currentUser.getPhone());
                    Toast.makeText(Cart.this, "Thank you, your order has been placed.", Toast.LENGTH_SHORT).show();
                    finish();

                    Intent intent = new Intent(Cart.this, CollectionConfirmation.class);
                    startActivity(intent);

                    Toast.makeText(Cart.this, "Your order for collection has been placed!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetail);


                        //create new request
                        Request request = new Request(
                                Common.currentUser.getPhone(),
                                Common.currentUser.getUsername(),
                                address,
                                txtTotalPrice.getText().toString(),
                                "0", //status
                                comment,
                                "Paypal",
                                jsonObject.getJSONObject("response").getString("state"),
                                cart
                        );

                        //submit information to firebase
                        //using System.milli to key
                        requests.child(String.valueOf(System.currentTimeMillis()))
                                .setValue(request);

                        //delete cart
                        new Database(getBaseContext()).cleanCart(Common.currentUser.getPhone());
                        Toast.makeText(Cart.this, "Thank you, your order has been placed.", Toast.LENGTH_SHORT).show();
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText(this, "Invalid payment", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadListFood() {
        cart = new Database(this).getCarts(Common.currentUser.getPhone());
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        //calculating the total price
        int total = 0;
        for (Order order : cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "GB");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //remove item from cart based on position
        cart.remove(position);
        //after, delete all old data from SQLite
        new Database(this).cleanCart(Common.currentUser.getPhone());
        //then update new data from List<Order> to SQLite
        for (Order item : cart)
            new Database(this).addToCart(item);
        //refresh
        loadListFood();

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();

            final Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(), Common.currentUser.getPhone());

            //update txttotal
            //calculating the total price
            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
            for (Order item : orders)
                total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
            Locale locale = new Locale("en", "GB");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));

            //make snackbar
            Snackbar snackbar = Snackbar.make(rootLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    //update txttotal
                    //calculating the total price
                    int total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
                    for (Order item : orders)
                        total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("en", "GB");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    txtTotalPrice.setText(fmt.format(total));

                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}
