package android.ohiostate.buckeyepartments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AllFavouriteApartmentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Database database=new Database(this);
    List<ListingCard> listingCards;
    FavouriteAdapter favouriteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favourite_apartments);
        recyclerView=findViewById(R.id.recyclerView);
        listingCards=new ArrayList<>();
        favouriteAdapter =new FavouriteAdapter(listingCards,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(favouriteAdapter);
        favouriteAdapter.setOnItemClickListener(new FavouriteAdapter.onItemClickListener() {
            @Override
            public void remove(int position) {
                Cursor cursor=database.getSingleApartment(listingCards.get(position).getKey());
                if(cursor.moveToFirst()) {
                    database.deleteApartment(cursor.getInt(0));
                }
                getAllFavouriteItems();
            }
            @Override
            public void showFullDetails(int position) {
                Intent intent = new Intent(AllFavouriteApartmentsActivity.this, viewListingActivity.class);
                intent.putExtra("listingKey", listingCards.get(position).getKey());
                startActivity(intent);
            }
        });
        getAllFavouriteItems();
    }

    private void getAllFavouriteItems() {
        listingCards.clear();
        Cursor cursor=database.getAllApartments();
        if(cursor.moveToFirst()){
            do {
                ListingCard listingCard=new ListingCard();
                listingCard.setKey(cursor.getString(1));
                listingCard.setStreetAddress(cursor.getString(2));
                listingCard.setCity(cursor.getString(7));
                listingCard.setZip(cursor.getString(8));
                listingCard.setPreviewImageUrl(cursor.getString(6));
                listingCard.setBath(cursor.getString(5));
                listingCard.setBed(cursor.getString(4));
                listingCard.setRent(cursor.getString(3));
                listingCards.add(listingCard);
            }while (cursor.moveToNext());
            favouriteAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getApplicationContext(),"No Favourite items found",Toast.LENGTH_LONG).show();
        }
    }
}