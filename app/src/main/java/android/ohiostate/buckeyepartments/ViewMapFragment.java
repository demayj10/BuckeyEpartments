package android.ohiostate.buckeyepartments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Geocoder;
import android.location.Address;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class ViewMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseDatabase database;
    private GoogleMap map;
    private Geocoder gc;
    private Context fContext;
    private Marker currentPos;

    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fContext = context;
    }

import java.util.List;

public class ViewMapFragment extends Fragment {
    String city,address;
    FirebaseDatabase database;
    GoogleMap map;
    Geocoder gc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(fContext);

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

            // we're not expecting this to update in real-time, as we don't want to have to
            // manage updating/removing/adding markers
            database.getReference().addListenerForSingleValueEvent(addMarkers);

            if (ActivityCompat.checkSelfPermission(fContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d(ViewMapFragment.this.getClass().getSimpleName(), "No location perms!");
                return;
            }

            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setOnInfoWindowClickListener(ViewMapFragment.this::onInfoWindowClick);

            // get current location
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                // got last known location, so build marker for it (assuming it's not null)
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng myPosition = new LatLng(latitude, longitude);
                    // now build marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("Your Location");
                    markerOptions.position(myPosition);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    currentPos = map.addMarker(markerOptions);
                    Log.d(ViewMapFragment.this.getClass().getSimpleName(), "Device position marker added!");
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 12));
                }
            });

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

                if (!getSnapshotValue(listing, "latlong/lat").equals("null")) {
                    LatLng latLng = new LatLng( Double.parseDouble(getSnapshotValue(listing,"latlong/lat")),
                            Double.parseDouble(getSnapshotValue(listing, "latlong/long")));
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    Marker marker = map.addMarker(markerOptions);
                    marker.setTag(listing.getKey());
                    Log.d(ViewMapFragment.this.getClass().getSimpleName(), "Marker added!");
                } else {
                    // no latitude or longitude held for this listing, use geocoder
                    try {
                        List<Address> list = gc.getFromLocationName(String.format("%s, %s", streetAddressString, cityZipState),1);
                        if (list != null && list.size() > 0) {
                            Address result = list.get(0);
                            LatLng latLng=new LatLng( result.getLatitude(), result.getLongitude());
                            // code to push the location we find to the db
                            HashMap<String, Object> update = new HashMap<String, Object>();
                            update.put("latlong/lat", Double.toString(result.getLatitude()));
                            update.put("latlong/long", Double.toString(result.getLongitude()));
                            listing.getRef().updateChildren(update);
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            Marker marker = map.addMarker(markerOptions);
                            marker.setTag(listing.getKey());
                            Log.d(ViewMapFragment.this.getClass().getSimpleName(), "Marker added!");
                        }
                    } catch (IOException e) {
                        Log.e(ViewMapFragment.this.getClass().getSimpleName(), e.toString());
                    }
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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(fContext, viewListingActivity.class);
        intent.putExtra("listingKey", marker.getTag().toString());
        fContext.startActivity(intent);
    }
}