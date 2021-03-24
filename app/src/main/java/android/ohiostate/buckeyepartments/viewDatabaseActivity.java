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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewDatabaseActivity extends AppCompatActivity {

    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(viewDatabaseActivity.class.getSimpleName(), "Called onCreate");
        setContentView(R.layout.activity_view_database);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ListingCard> options = new FirebaseRecyclerOptions.Builder<ListingCard>()
                .setQuery(ref, snapshot -> {
                    String previewUrl = getSnapshotValue(snapshot, "previewPicture");
                    String streetAddress = getSnapshotValue(snapshot, "address/streetAddress");
                    String city = getSnapshotValue(snapshot, "address/city");
                    String zipCode = getSnapshotValue(snapshot, "address/zipCode");
                    String roomCount = getSnapshotValue(snapshot, "bedBath/roomCount");
                    String bathroomCount = getSnapshotValue(snapshot, "bedBath/bathroomCount");
                    String costOfRent = getSnapshotValue(snapshot, "costOfRent");

                    return new ListingCard(previewUrl, streetAddress, city, zipCode, roomCount,
                            bathroomCount, costOfRent, snapshot.getKey());
                })
                .build();

        adapter = new RecyclerViewAdapter(options, this);
        recyclerView.setAdapter(adapter);
        Log.d(viewDatabaseActivity.class.getSimpleName(), "Adapter set! " + recyclerView.toString());
        ref.addValueEventListener(buildList);
    }

    @NonNull
    private String getSnapshotValue(DataSnapshot snapshot, String key)
    {
        Object keyValue = snapshot.child(key).getValue();
        if (keyValue == null) {
            return "null";
        } else {
            return keyValue.toString();
        }
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

    public void createListing(View v)
    {
        Intent intent = new Intent(this, viewListingActivity.class);
        intent.putExtra("listingKey", "");
        this.startActivity(intent);
    }
}