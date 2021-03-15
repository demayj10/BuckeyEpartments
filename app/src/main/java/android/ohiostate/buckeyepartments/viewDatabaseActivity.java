package android.ohiostate.buckeyepartments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewDatabaseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        recyclerView = findViewById(R.id.recycler_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.addValueEventListener(buildList);
    }

    private ValueEventListener buildList = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d(viewDatabaseActivity.this.getClass().getSimpleName(), "DataSnapshot arrived.");
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(viewDatabaseActivity.this, snapshot);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(viewDatabaseActivity.this));
            Log.d(viewDatabaseActivity.this.getClass().getSimpleName(), "RecyclerView initialized.");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(viewDatabaseActivity.this.getClass().getSimpleName(), "Failed to read value.", error.toException());
        }
    };
}