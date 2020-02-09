//package uk.ac.mmu.foodorderingapp;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import uk.ac.mmu.foodorderingapp.Common.Common;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//public class RestaurantList extends AppCompatActivity {
//
//    AlertDialog waitingDialog;
//    RecyclerView recyclerView;
//    SwipeRefreshLayout swipeRefreshLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_restaurant_list);
//
//
//        //view
//        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark
//        );
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//
//                if(Common.isConnectedToInternet((getBaseContext())))
//                    loadRestaurant();
//                else
//                {
//                    Toast.makeText(getBaseContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//        });
//
//    }
//}
