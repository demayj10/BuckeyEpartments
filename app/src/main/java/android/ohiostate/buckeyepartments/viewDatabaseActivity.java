package android.ohiostate.buckeyepartments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class viewDatabaseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(viewDatabaseActivity.class.getSimpleName(), "Called onCreate");
        setContentView(R.layout.activity_view_database);

        recyclerView = findViewById(R.id.recycler_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ListingCard> options = new FirebaseRecyclerOptions.Builder<ListingCard>()
                .setQuery(ref, new SnapshotParser<ListingCard>() {
                    @NonNull
                    @Override
                    public ListingCard parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new ListingCard(snapshot.child("address/streetAddress").getValue().toString(),
                                snapshot.child("address/city").getValue().toString(),
                                snapshot.child("address/zipCode").getValue().toString(),
                                snapshot.child("bedBath/roomCount").getValue().toString(),
                                snapshot.child("bedBath/bathroomCount").getValue().toString(),
                                snapshot.child("costOfRent").getValue().toString(),
                                snapshot.getKey());
                    }
                })
                .build();
        adapter = new RecyclerViewAdapter(options, this);
        recyclerView.setAdapter(adapter);
        Log.d(viewDatabaseActivity.class.getSimpleName(), "Adapter set! " + recyclerView.toString());
        ref.addValueEventListener(buildList);
    }

    private final ValueEventListener buildList = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d(viewDatabaseActivity.class.getSimpleName(),
                    "DataSnapshot arrived.");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(viewDatabaseActivity.class.getSimpleName(),
                    "Failed to read value.", error.toException());
        }
    };

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    public void createListing(View v) {
        Intent intent = new Intent(this, viewListingActivity.class);
        intent.putExtra("listingKey", "");
        this.startActivity(intent);
    }
}