package android.ohiostate.buckeyepartments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Geocoder;
import android.location.Address;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class ViewMapFragment extends Fragment {
    String city,address;
    FirebaseDatabase database;
    GoogleMap map;
    Geocoder gc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for now, just show all listings on the map

        // init database
        database = FirebaseDatabase.getInstance();
        gc = new Geocoder(getContext());
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d(ViewMapFragment.this.getClass().getSimpleName(), "Map readied!");
            map = googleMap;
            database.getReference().addValueEventListener(addMarkers);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_view_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.view_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private final ValueEventListener addMarkers = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d(ViewMapFragment.this.getClass().getSimpleName(), "Snapshot obtained!");
            for (DataSnapshot listing : snapshot.getChildren()) {
                MarkerOptions markerOptions = new MarkerOptions();

                // set up marker strings
                String streetAddressString = getSnapshotValue(listing, "address/streetAddress");
                markerOptions.title(streetAddressString);

                String city = getSnapshotValue(listing, "address/city");
                String zip = getSnapshotValue(listing, "address/zipCode");
                String cityZipState = String.format("%s, %s OH", city, zip);
                markerOptions.snippet(cityZipState);

                // get marker latitude and longitude from geocoder
                try {
                    List<Address> list = gc.getFromLocationName(String.format("%s, %s", streetAddressString, cityZipState),1);
                    if (list != null && list.size() > 0) {
                        Address result = list.get(0);
                        LatLng latLng=new LatLng( result.getLatitude(), result.getLongitude());
                        markerOptions.position(latLng);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        map.addMarker(markerOptions);
                        Log.d(ViewMapFragment.this.getClass().getSimpleName(), "Marker added!");
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                    }
                } catch (IOException e) {
                    Log.e(ViewMapFragment.this.getClass().getSimpleName(), e.toString());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(ViewMapFragment.this.getClass().getSimpleName(),
                    "Failed to read value.", error.toException());
        }
    };

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
}