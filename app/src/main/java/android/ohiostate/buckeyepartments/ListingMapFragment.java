package android.ohiostate.buckeyepartments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.location.Geocoder;
import android.location.Address;

import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class ListingMapFragment extends Fragment {
    String city,address;
    Geocoder gc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        city = this.getArguments().getString("city");
        address = this.getArguments().getString("street");
        gc = new Geocoder(getContext());
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.title(city);
            markerOptions.snippet(address);

            // get marker latitude and longitude from geocoder
            try {
                List<Address> list = gc.getFromLocationName(String.format("%s, %s", address, city),1);
                if (list != null && list.size() > 0) {
                    Address result = list.get(0);
                    LatLng latLng=new LatLng( result.getLatitude(), result.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    Marker marker= googleMap.addMarker(markerOptions);
                    Log.d(ListingMapFragment.this.getClass().getSimpleName(), "Marker added!");
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                    marker.showInfoWindow();
                }
            } catch (IOException e) {
                Log.e(ListingMapFragment.this.getClass().getSimpleName(), e.toString());
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_listing_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.listing_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}