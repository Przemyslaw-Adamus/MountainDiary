package com.example.mountaindiary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

class ItemPeakLightRangeAdapter extends RecyclerView.Adapter<ItemPeakLightRangeAdapter.ExampleViewHolder>{

    public ArrayList<Gain> exampleList;
    public ItemPeakLightRangeAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemPeakLightRangeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageViewWinter;
        public ImageView imageViewSummer;
        public ImageView imageViewLocation;
        public ImageView imageViewFoto;
        public TextView tvDate;
        public TextView tvXCord;
        public TextView tvYCord;

        public ExampleViewHolder(@NonNull View itemView, final ItemPeakLightRangeAdapter.OnItemClickListener onItemClickListener) {
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

    public ItemPeakLightRangeAdapter(ArrayList<Gain> exampleItemList){
        exampleList = exampleItemList;
    }

    @NonNull
    @Override
    public ItemPeakLightRangeAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.peak_item,viewGroup,false);
        ItemPeakLightRangeAdapter.ExampleViewHolder evh = new ItemPeakLightRangeAdapter.ExampleViewHolder(v,onItemClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPeakLightRangeAdapter.ExampleViewHolder exampleViewHolder, int i) {
        Gain currentItem = exampleList.get(i);
        GlobalData gd = new GlobalData();
        Peak tmp = gd.getPeaksList().get(currentItem.getPeakId());

        if(currentItem.getDateSummer().trim().length()==0){
            exampleViewHolder.tvDate.setText(currentItem.getDateWinter());
            exampleViewHolder.imageViewWinter.setAlpha(1.0f);
        }
        else{
            exampleViewHolder.tvDate.setText(currentItem.getDateSummer());
            exampleViewHolder.imageViewSummer.setAlpha(1.0f);
        }

        if(currentItem.getLocationOrFoto() == 1){
            exampleViewHolder.tvXCord.setText("Coord X: " + Float.toString(currentItem.getCoordX()) + " (" + Float.toString(tmp.getxCord()) + ")");
            exampleViewHolder.tvYCord.setText("Coord Y: " + Float.toString(currentItem.getCoordY()) + " (" + Float.toString(tmp.getyCord()) + ")");
            exampleViewHolder.imageViewLocation.setAlpha(1.0f);
        }
        else{
            exampleViewHolder.tvXCord.setText("Coord X: FOTO "  + " (" + Float.toString(tmp.getxCord()) + ")");
            exampleViewHolder.tvYCord.setText("Coord Y: FOTO "  + " (" + Float.toString(tmp.getyCord()) + ")");
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
