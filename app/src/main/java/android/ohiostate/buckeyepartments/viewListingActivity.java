package android.ohiostate.buckeyepartments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class viewListingActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private ImageView previewImage;
    private TextView streetAddress;
    private TextView restOfAddress;
    private TextView rent;
    private TextView bedBath;
    private TextView email;
    private TextView url;
    private TextView phone;
    private final String TAG = "viewListingActivity";
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        key = (String) extras.get("listingKey");

        previewImage = findViewById(R.id.listing_image);

        streetAddress = findViewById(R.id.street_address);
        restOfAddress = findViewById(R.id.rest_of_address);

        rent = findViewById(R.id.rent);
        bedBath = findViewById(R.id.bed_and_bath);

        email = findViewById(R.id.email);
        url = findViewById(R.id.url);
        phone = findViewById(R.id.phone);

        // init database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (!key.equals("")) {
            ref = database.getReference(key);
            ref.addValueEventListener(fillValues);
            findViewById(R.id.edit_button).setOnClickListener(this::editListing);
            findViewById(R.id.delete_button).setOnClickListener(this::deleteListing);
        } else {
            ref = database.getReference();
        }
    }

    private final ValueEventListener fillValues = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.d(TAG, "In onDataChange");
            try {
                if (snapshot.getValue() != null) {
                    String previewImageUrl = getSnapshotValue(snapshot, "previewPicture");
                    Log.d(TAG, previewImageUrl);
                    Picasso.get().load(previewImageUrl).into(previewImage);

                    streetAddress.setText(getSnapshotValue(snapshot, "address/streetAddress"));
                    String city = getSnapshotValue(snapshot, "address/city");
                    String zip = getSnapshotValue(snapshot, "address/zipCode");
                    String restOfAddressString = String.format("%s, %s OH", city, zip);
                    restOfAddress.setText(restOfAddressString);

                    rent.setText(String.format("$%s", getSnapshotValue(snapshot, "costOfRent")));

                    String bed = getSnapshotValue(snapshot,"bedBath/roomCount");
                    String bath = getSnapshotValue(snapshot, "bedBath/bathroomCount");
                    bedBath.setText(String.format("%s Bed, %s Bath", bed, bath));

                    email.setText(getSnapshotValue(snapshot, "contactInfo/email"));
                    url.setText(getSnapshotValue(snapshot, "contactInfo/listingUrl"));
                    phone.setText(getSnapshotValue(snapshot, "contactInfo/phoneNumber"));
                }
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w(viewListingActivity.this.getClass().getSimpleName(),
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

    public void editListing(View v)
    {
        Intent intent = new Intent(this, createEditDeleteListingActivity.class);
        intent.putExtra("listingKey", key);
        this.startActivity(intent);
    }

    public void deleteListing(View v)
    {
        ref.removeValue().addOnSuccessListener(aVoid -> {
            Toast success = Toast.makeText(getApplicationContext(),
                    "Successfully deleted!", Toast.LENGTH_SHORT);
            success.show();
            finish();
        });
    }

}
