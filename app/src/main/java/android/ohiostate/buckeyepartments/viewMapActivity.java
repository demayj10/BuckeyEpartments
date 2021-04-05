package android.ohiostate.buckeyepartments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewMapActivity extends AppCompatActivity {

    int radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        radius = 1000;

        // init database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().addValueEventListener(buildMap);
    }

    private void loadFragment(int radius) {
        Fragment fragment;
        fragment=new ViewMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("radius", ""+radius);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapFragment, fragment)
                .commit();
    }

    private final ValueEventListener buildMap = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            loadFragment(radius);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(viewMapActivity.this.getClass().getSimpleName(),
                    "Failed to read value.", error.toException());
        }
    };
}