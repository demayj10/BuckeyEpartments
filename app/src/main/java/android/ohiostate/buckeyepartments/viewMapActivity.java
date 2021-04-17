package android.ohiostate.buckeyepartments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class viewMapActivity extends AppCompatActivity {

    private ViewMapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        viewModel = new ViewModelProvider(this).get(ViewMapViewModel.class);
        viewModel.setRadius(-1);

        SearchView search_bar = findViewById(R.id.search);
        search_bar.setOnQueryTextListener(updateViewModel);

        // init database
        // FirebaseDatabase database = FirebaseDatabase.getInstance();
        // database.getReference().addValueEventListener(buildMap);
        loadFragment();
    }

    private void loadFragment() {
        Fragment fragment;
        fragment=new ViewMapFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapFragment, fragment)
                .commit();
    }

//    private final ValueEventListener buildMap = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            loadFragment();
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//            Log.w(viewMapActivity.this.getClass().getSimpleName(),
//                    "Failed to read value.", error.toException());
//        }
//    };

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

    public void openRadiusMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(radiusClick);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.radius_menu, popup.getMenu());
        popup.show();
    }

    PopupMenu.OnMenuItemClickListener radiusClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getTitle().toString()) {
                case "100 Meters":
                    viewModel.setRadius(100);
                    break;
                case "500 Meters":
                    viewModel.setRadius(500);
                    break;
                case "1,000 Meters":
                    viewModel.setRadius(1000);
                    Log.d(viewMapActivity.this.getClass().getSimpleName(),
                            "Radius set to 1000!");
                    break;
                case "5,000 Meters":
                    viewModel.setRadius(5000);
                    break;
                case "10,000 Meters":
                    viewModel.setRadius(10000);
                    break;
                case "Unlimited":
                    viewModel.setRadius(-1);
                    break;
            }
            return true;
        }
    };
}