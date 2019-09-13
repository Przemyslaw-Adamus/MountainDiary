package com.example.mountaindiary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

public class ItemMountainRangeAdapter extends RecyclerView.Adapter<ItemMountainRangeAdapter.ExampleViewHolder> {

    public ArrayList<MountainRange> exampleList;
    public OnItemClickListener onItemClickListener;
    public OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemMountainRangeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(ItemMountainRangeAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTitle;
        public TextView tvQantity;
        public ProgressBar progressBar;
        public ImageView imageView;

        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener, final OnItemLongClickListener onItemLongClickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textViewTitle);
            tvQantity = itemView.findViewById(R.id.textViewRange);
            progressBar = itemView.findViewById(R.id.progressBar);
            imageView = itemView.findViewById(R.id.imageViewN);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    if(onItemLongClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemLongClickListener.onItemLongClick(position);
                        }
                    }
                    return false;
                }
            });
        }
    }

    public ItemMountainRangeAdapter(ArrayList<MountainRange> exampleItemList){
        exampleList = exampleItemList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mountain_range_item,viewGroup,false);
        ExampleViewHolder evh = new ExampleViewHolder(v,onItemClickListener,onItemLongClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        MountainRange currentItem = exampleList.get(i);
        exampleViewHolder.tvTitle.setText(currentItem.getTitle());
        exampleViewHolder.tvQantity.setText(currentItem.getNoPeaksAcquired() + " / " + currentItem.getNoPeaks());
        float result = (((float)currentItem.getNoPeaksAcquired()) / (float)currentItem.getNoPeaks() * 100f);
        exampleViewHolder.progressBar.setProgress((int)result);
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    public void deleteItem(int position) {

    }

    public void editItem(int position) {

    }
}
