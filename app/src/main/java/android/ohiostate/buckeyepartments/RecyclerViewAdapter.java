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

import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final DataSnapshot snapshot;

    public RecyclerViewAdapter(Context context, DataSnapshot snap) {
        mContext = context;
        snapshot = snap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_database_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(RecyclerViewAdapter.this.getClass().getSimpleName(), "onBindViewHolder called.");
        DataSnapshot data = snapshot.child("" + position);

        String costOfRent = Objects.requireNonNull(data.child(mContext.getString(
                R.string.data_rent_key)).getValue()).toString();
        holder.rent.setText(String.format("$%s", costOfRent));

        DataSnapshot bedBathSnapshot = getDataSnapshot(data, R.string.data_bed_bath_key);
        String bedCount = getDataStringValue(bedBathSnapshot, R.string.data_bed_count_key);
        String bathroomCount = getDataStringValue(bedBathSnapshot, R.string.data_bathroom_count_key);
        holder.bedBath.setText(String.format("%s Bed, %s Bath", bedCount, bathroomCount));

        DataSnapshot addressSnapshot = getDataSnapshot(data, R.string.data_address_key);
        String streetAddress = getDataStringValue(addressSnapshot, R.string.data_street_address_key);
        String city = getDataStringValue(addressSnapshot, R.string.data_city_key);
        String zipCode = getDataStringValue(addressSnapshot, R.string.data_zip_code_key);
        String stateInitial = mContext.getString(R.string.state_initial);
        holder.address.setText(String.format("%s, %s %s, %s", streetAddress, city, zipCode,
                stateInitial));

        holder.key = "" + position;
    }

    private DataSnapshot getDataSnapshot(DataSnapshot data, int stringResId) {
        return Objects.requireNonNull(data.child(mContext.getString(stringResId)));
    }

    private String getDataStringValue(DataSnapshot data, int stringResId) {
        return Objects.requireNonNull(data.child(mContext.getString(stringResId)).getValue())
                .toString();
    }

    @Override
    public int getItemCount() {
        return (int)snapshot.getChildrenCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView address;
        TextView bedBath;
        TextView rent;
        String key;

        public ViewHolder(View itemView) {
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

