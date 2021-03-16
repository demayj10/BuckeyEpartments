package android.ohiostate.buckeyepartments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class viewListingActivity extends AppCompatActivity {

    private String key;
    private TextInputEditText address;
    private TextInputEditText city;
    private TextInputEditText zip;
    private TextInputEditText bed;
    private TextInputEditText bath;
    private TextInputEditText email;
    private TextInputEditText url;
    private TextInputEditText phone;
    private TextInputEditText rent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing);
        // get listing id from intent bundle
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        key = (String)extras.get("listingKey");

        // get all text fields
        address = findViewById(R.id.address_bar);
        city = findViewById(R.id.city_bar);
        zip = findViewById(R.id.zip_bar);
        bed = findViewById(R.id.bedroom_bar);
        bath = findViewById(R.id.bathroom_bar);
        email = findViewById(R.id.email_bar);
        url = findViewById(R.id.url_bar);
        phone = findViewById(R.id.phone_bar);
        rent = findViewById(R.id.rent_bar);

        // init database and listener
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(key);
        ref.addValueEventListener(fillValues);
    }

    private final ValueEventListener fillValues = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            address.setText(getDataString(snapshot, "address/streetAddress"));
            city.setText(getDataString(snapshot, "address/city"));
            zip.setText(getDataString(snapshot, "address/zipCode"));

            bed.setText(getDataString(snapshot,"bedBath/roomCount"));
            bath.setText(getDataString(snapshot, "bedBath/bathroomCount"));

            email.setText(getDataString(snapshot, "contactInfo/email"));
            url.setText(getDataString(snapshot, "contactInfo/listingUrl"));
            phone.setText(getDataString(snapshot, "contactInfo/phoneNumber"));

            rent.setText(getDataString(snapshot, "costOfRent"));
        }

        private String getDataString(DataSnapshot data, String key) {
            return Objects.requireNonNull(data.child(key).getValue()).toString();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(viewListingActivity.this.getClass().getSimpleName(), "Failed to read value.", error.toException());
        }
    };
}
