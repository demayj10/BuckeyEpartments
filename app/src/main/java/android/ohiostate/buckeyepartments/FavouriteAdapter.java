package android.ohiostate.buckeyepartments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.CustomViewHolder> {

    List<ListingCard> listingCards;
    Context context;
    private  onItemClickListener mListener;
    public  interface onItemClickListener{
        void  remove(int position);
        void  showFullDetails(int position);
    }
    public  void setOnItemClickListener(onItemClickListener listener){
        mListener=listener;
    }
    public static class  CustomViewHolder extends RecyclerView.ViewHolder{
        TextView address,bed_bath,rent;
        ImageView preview_image,imageViewFavouriteItemDelete;
        public CustomViewHolder(View itemView, final onItemClickListener listener) {
            super(itemView);
            address=itemView.findViewById(R.id.address);
            bed_bath=itemView.findViewById(R.id.bed_bath);
            rent=itemView.findViewById(R.id.rent);
            preview_image=itemView.findViewById(R.id.preview_image);
            imageViewFavouriteItemDelete=itemView.findViewById(R.id.imageViewFavouriteItemDelete);

            imageViewFavouriteItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.remove(position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position=getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.showFullDetails(position);
                        }
                    }
                }
            });
        }

    }
    public FavouriteAdapter(List<ListingCard> listingCards, Context context) {
        this.listingCards = listingCards;
        this.context = context;
    }
    @Override
    public int getItemViewType(int position) {
        return R.layout.view_favourite_item;
    }
    @Override
    public int getItemCount() {
        return  listingCards.size();
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false),mListener);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Picasso.get().load(listingCards.get(position).getPreviewImageUrl()).into(holder.preview_image);
        holder.rent.setText(String.format("$%s", listingCards.get(position).getRent()));

        holder.bed_bath.setText(String.format("%s Bed, %s Bath", listingCards.get(position).getBed(), listingCards.get(position).getBath()));

        holder.address.setText(String.format("%s, %s OH, %s", listingCards.get(position).getStreetAddress(),
                listingCards.get(position).getCity(), listingCards.get(position).getZip()));
    }
}
