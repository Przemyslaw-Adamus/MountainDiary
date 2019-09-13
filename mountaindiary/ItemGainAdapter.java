package com.example.mountaindiary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

class ItemGainAdapter extends RecyclerView.Adapter<ItemGainAdapter.ExampleViewHolder>{

    public ArrayList<Gain> exampleList;
    public ItemGainAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewWinter;
        public ImageView imageViewSummer;
        public ImageView imageViewLocation;
        public ImageView imageViewFoto;
        public TextView tvDate;
        public TextView tvXCord;
        public TextView tvYCord;

        public ExampleViewHolder(@NonNull View itemView, final ItemGainAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.textViewDate);
            tvXCord = itemView.findViewById(R.id.textViewXCord);
            tvYCord = itemView.findViewById(R.id.textViewYCord);
            imageViewWinter = itemView.findViewById(R.id.imageViewWinter);
            imageViewSummer = itemView.findViewById(R.id.imageViewSummer);
            imageViewLocation = itemView.findViewById(R.id.imageViewLocation);
            imageViewFoto = itemView.findViewById(R.id.imageViewFoto);

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
        }
    }

    public ItemGainAdapter(ArrayList<Gain> exampleItemList){
        exampleList = exampleItemList;
    }

    @NonNull
    @Override
    public ItemGainAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.peak_light_item,viewGroup,false);
        ItemGainAdapter.ExampleViewHolder evh = new ItemGainAdapter.ExampleViewHolder(v,onItemClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemGainAdapter.ExampleViewHolder exampleViewHolder, int i) {
        Gain currentItem = exampleList.get(i);
        GlobalData gd = new GlobalData();
        Peak tmp = gd.getPeakNameAndHeight(currentItem.getPeakStringId()).get(0);
        if(currentItem.getDateSummer().trim().length()==0){
            exampleViewHolder.tvDate.setText(currentItem.getDateWinter());
            exampleViewHolder.imageViewWinter.setAlpha(1.0f);
        }
        else{
            exampleViewHolder.tvDate.setText(currentItem.getDateSummer());
            exampleViewHolder.imageViewSummer.setAlpha(1.0f);
        }
        exampleViewHolder.tvXCord.setText("Coord X: " + Float.toString(currentItem.getCoordX()) + " (" + Float.toString(tmp.getxCord()) + ")");
        exampleViewHolder.tvYCord.setText("Coord Y: " + Float.toString(currentItem.getCoordY()) + " (" + Float.toString(tmp.getyCord()) + ")");
        if(currentItem.getLocationOrFoto() == 1){
            exampleViewHolder.imageViewLocation.setAlpha(1.0f);
        }
        else{
            exampleViewHolder.imageViewFoto.setAlpha(1.0f);
        }

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
