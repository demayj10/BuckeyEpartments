package android.ohiostate.buckeyepartments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewMapActivity extends AppCompatActivity {

    int radius;
    private ViewMapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        viewModel = new ViewModelProvider(this).get(ViewMapViewModel.class);

        SearchView search_bar = findViewById(R.id.search);
        search_bar.setOnQueryTextListener(updateViewModel);
        radius = 1000;

        // init database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //
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

    SearchView.OnQueryTextListener updateViewModel = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            viewModel.setSearchText(query);
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };
}