package android.ohiostate.buckeyepartments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<ListingCard, RecyclerViewAdapter.ListingCardHolder> {

    private Context mContext;

    public RecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<ListingCard> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ListingCardHolder holder, int position, @NonNull ListingCard model) {
        Log.d(RecyclerViewAdapter.this.getClass().getSimpleName(), "onBindViewHolder called for " + model.toString());

        holder.rent.setText(String.format("$%s", model.getRent()));

        holder.bedBath.setText(String.format("%s Bed, %s Bath", model.getBed(), model.getBath()));

        holder.address.setText(String.format("%s, %s OH, %s", model.getStreetAddress(), model.getCity(), model.getZip()));

        holder.key = "" + position;
    }

    @NonNull
    @Override
    public ListingCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_database_item,
                parent, false);
        return new ListingCardHolder(view);
    }

//    private DataSnapshot getDataSnapshot(DataSnapshot data, int stringResId) {
//        return Objects.requireNonNull(data.child(mContext.getString(stringResId)));
//    }
//
//    private String getDataStringValue(DataSnapshot data, int stringResId) {
//        return Objects.requireNonNull(data.child(mContext.getString(stringResId)).getValue())
//                .toString();
//    }

//    @Override
//    public int getItemCount() {
//        return (int)snapshot.getChildrenCount();
//    }

    class ListingCardHolder extends RecyclerView.ViewHolder{
        TextView address;
        TextView bedBath;
        TextView rent;
        String key;

        public ListingCardHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this::viewListing);
            address = itemView.findViewById(R.id.address);
            bedBath = itemView.findViewById(R.id.bed_bath);
            rent = itemView.findViewById(R.id.rent);
        }

        public void viewListing(View view) {
            Intent intent = new Intent(mContext, viewListingActivity.class);
            intent.putExtra("listingKey", key);
            mContext.startActivity(intent);
        }
    }
}

