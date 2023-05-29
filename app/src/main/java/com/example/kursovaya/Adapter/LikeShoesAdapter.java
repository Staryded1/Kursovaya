package com.example.kursovaya.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.Interface.onLikeClickListener;
import com.example.kursovaya.Model.Shoe;
import com.example.kursovaya.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LikeShoesAdapter extends RecyclerView.Adapter<LikeShoesAdapter.LikeShoesViewHolder> {

    private List<Shoe> likeShoesList;
    private onLikeClickListener onLikeClickListener;
    private String userId;

    public LikeShoesAdapter(List<Shoe> likeShoesList, com.example.kursovaya.Interface.onLikeClickListener onLikeClickListener, String userId) {
        this.likeShoesList = likeShoesList;
        this.onLikeClickListener = onLikeClickListener;
        this.userId = userId;
    }

    @NonNull
    @Override
    public LikeShoesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoes_item_layout, parent, false);
        return new LikeShoesViewHolder(itemView, onLikeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LikeShoesViewHolder holder, int position) {
        Shoe shoes = likeShoesList.get(position);

        holder.shoesName.setText(shoes.getName());
        holder.shoesSize.setText(shoes.getSize());

        Picasso.get().load(shoes.getImage()).into(holder.shoesImage);

        int likeIcon = shoes.isLike() ? R.drawable.unlike : R.drawable.like;
        Picasso.get().load(likeIcon).into(holder.likeIcon);
    }

    @Override
    public int getItemCount() {
        return likeShoesList.size();
    }

    public void removeLike(String userId, String likeId) {
        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Like");
        String likeKey = userId + "_" + likeId;
        likeRef.child(likeKey).removeValue();
    }


    public static class LikeShoesViewHolder extends RecyclerView.ViewHolder {
        ImageView likeIcon;
        TextView shoesName;
        ImageView shoesImage;
        TextView shoesSize;

        public LikeShoesViewHolder(View itemView, onLikeClickListener onLikeClickListener) {
            super(itemView);
            likeIcon = itemView.findViewById(R.id.like_icon);
            shoesName = itemView.findViewById(R.id.shoes_name_list);
            shoesImage = itemView.findViewById(R.id.shoes_image_list);
            shoesSize = itemView.findViewById(R.id.shoes_size_list);

            likeIcon.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onLikeClickListener.onLikeClick(position);
                }
            });
        }
    }
}
