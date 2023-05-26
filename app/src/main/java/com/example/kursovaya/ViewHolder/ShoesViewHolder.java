package com.example.kursovaya.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.Interface.ItemClickListner;
import com.example.kursovaya.R;

public class ShoesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtShoeName_List, txtShoeSize_List;
    public ImageView ShoesImage_List, likeIcon;
    public ItemClickListner Listner;


    public ShoesViewHolder(View itemView)
    {
        super(itemView);

        ShoesImage_List = itemView.findViewById(R.id.shoes_image_list);
        txtShoeSize_List = itemView.findViewById(R.id.shoes_size_list);
        txtShoeName_List = itemView.findViewById(R.id.shoes_name_list);
        likeIcon = itemView.findViewById(R.id.like_icon);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.Listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        Listner.onClick(view, getAdapterPosition(), false);
    }
}
