package android.ohiostate.buckeyepartments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private DataSnapshot snapshot;

    public RecyclerViewAdapter(Context context, DataSnapshot snap) {
        mContext = context;
        snapshot = snap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_database_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(RecyclerViewAdapter.this.getClass().getSimpleName(), "onBindViewHolder called.");
        holder.rent.setText("$" + snapshot.child("" + position).child("costOfRent").getValue());
        holder.bedBath.setText(snapshot.child("" + position).child("bedBath").child("roomCount").getValue() + " Bed, " + snapshot.child("" + position).child("bedBath").child("bathroomCount").getValue() + " Bath");
        holder.address.setText(snapshot.child("" + position).child("address").child("streetAddress").getValue() + ", " + snapshot.child("" + position).child("address").child("city").getValue() + " OH, " + snapshot.child("" + position).child("address").child("zipCode").getValue());
    }

    @Override
    public int getItemCount() {
        return (int)snapshot.getChildrenCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView address;
        TextView bedBath;
        TextView rent;

        public ViewHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            bedBath = itemView.findViewById(R.id.bed_bath);
            rent = itemView.findViewById(R.id.rent);
        }
    }
}
