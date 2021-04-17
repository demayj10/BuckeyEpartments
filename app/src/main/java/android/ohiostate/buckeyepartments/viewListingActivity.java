package android.ohiostate.buckeyepartments;



import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    // added new
    ImageView imageViewFavorite;
    Database localDatabase=new Database(this);
    String imageURL;
    String streetAddressString,city,zip,restOfAddressString,costOfRent,bed,bath,emailString,urlString,phoneString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_listing);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        key = (String) extras.get("listingKey");
        imageViewFavorite=findViewById(R.id.imageViewFavorite);

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

        // new added lines
        Cursor cursor=localDatabase.getSingleApartment(key);
        if(cursor.moveToFirst()){
            imageViewFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
        }else {
            imageViewFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_un_favorite));
        }

        imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor1=localDatabase.getSingleApartment(key);
                if(cursor1.moveToFirst()){
                    imageViewFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_un_favorite));
                    localDatabase.deleteApartment(cursor1.getInt(0));
                }else {
                    imageViewFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                    localDatabase.InsertApartment(key,streetAddressString,costOfRent,bed,bath,imageURL,city,zip);
                }

            }
        });
    }


    private void LoadFragment(String city,String street) {
        Fragment fragment;
        fragment=new ListingMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city",city);
        bundle.putString("street",street);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapFragment, fragment)
                .commit();
    }

    private final ValueEventListener fillValues = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Log.d(TAG, "In onDataChange");
            try {
                if (snapshot.getValue() != null) {
                    String previewImageUrl = getSnapshotValue(snapshot, "previewPicture");
                    // globalize image url
                    imageURL=previewImageUrl;
                    Log.d(TAG, previewImageUrl);
                    Picasso.get().load(previewImageUrl).into(previewImage);
                    streetAddressString = getSnapshotValue(snapshot, "address/streetAddress");
                    streetAddress.setText(streetAddressString);
                    city = getSnapshotValue(snapshot, "address/city");
                    zip = getSnapshotValue(snapshot, "address/zipCode");
                    restOfAddressString = String.format("%s, %s OH", city, zip);
                    restOfAddress.setText(restOfAddressString);
                    LoadFragment(streetAddressString,restOfAddressString);
                    costOfRent= getSnapshotValue(snapshot, "costOfRent");
                    rent.setText(String.format("$%s", getSnapshotValue(snapshot, "costOfRent")));

                    bed = getSnapshotValue(snapshot,"bedBath/roomCount");
                    bath = getSnapshotValue(snapshot, "bedBath/bathroomCount");
                    bedBath.setText(String.format("%s Bed, %s Bath", bed, bath));
                    emailString=getSnapshotValue(snapshot, "contactInfo/email");
                    email.setText(emailString);
                    urlString=getSnapshotValue(snapshot, "contactInfo/listingUrl");
                    url.setText(urlString);
                    phoneString=getSnapshotValue(snapshot, "contactInfo/phoneNumber");
                    phone.setText(phoneString);




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
