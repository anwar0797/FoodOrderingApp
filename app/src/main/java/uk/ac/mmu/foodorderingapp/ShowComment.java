//
//package uk.ac.mmu.foodorderingapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import uk.ac.mmu.foodorderingapp.Common.Common;
//import uk.ac.mmu.foodorderingapp.Model.Rating;
//import uk.ac.mmu.foodorderingapp.ViewHolder.ShowCommentViewHolder;
//import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//
//public class ShowComment extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//
//    FirebaseDatabase database;
//    DatabaseReference ratingTbl;
//
//    SwipeRefreshLayout mSwipeRefreshLayout;
//
//    String foodId="";
//
//    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;
//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//        .setDefaultFontPath("fonts/cf.otf")
//        .setFontAttrId(R.attr.fontPath)
//        .build());
//        setContentView(R.layout.activity_show_comment);
//
//        //firebase
//        database = FirebaseDatabase.getInstance();
//        ratingTbl = database.getReference("Rating");
//
//        recyclerView = (RecyclerView)findViewById(R.id.recyclerComment);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if(getIntent() != null)
//                    foodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
//                if(!foodId.isEmpty() && foodId != null)
//                {
//                    //create request query
//                    Query query = ratingTbl.orderByChild("foodId").equalTo(foodId);
//
//
//                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
//                            .setQuery(query,Rating.class)
//                            .build();
//
//
//                }
//            }
//        });
//    }
//}
